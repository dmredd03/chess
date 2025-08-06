package websocketServer;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameSQLDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import server.Server;
import spark.Spark;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebSocket
public class WebSocketHandler {

    private final WebSocketConnManager connections = new WebSocketConnManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT:
                var connCommand = new Gson().fromJson(message, ConnectCommand.class);
                connect(connCommand, session);
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                var makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand, session);
                break;
            case UserGameCommand.CommandType.LEAVE:
                var exitCommand = new Gson().fromJson(message, ConnectCommand.class);
                leave(exitCommand);
                break;
            case UserGameCommand.CommandType.RESIGN:
                resign(command, session);
                break;
        }
    }

    private void connect(ConnectCommand command, Session session) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            ChessGame game = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();
            String color;
            if (command.getColor() == null) {
                color = autoAssign(username);
            } else {
                color = command.getColor();
            }

            connections.add(command.getGameID(), username, color, session);


            // send loadgame message to user
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, command.getColor());
            connections.directBroadcast(command.getGameID(), username, color, loadGameMessage);
            // notify other users
            String notificationConnection;
            if (color.equals("observer")) {
                notificationConnection = String.format("message: %s is now observing", username);
            } else if (color.equals("WHITE")) {
                notificationConnection = String.format("message: %s joined game as white", username);
            } else {
                notificationConnection = String.format("message: %s joined game as black", username);
            }
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationConnection);
            connections.broadcast(command.getGameID(), username, color, message);
        } catch (DataAccessException | SQLException | NullPointerException e) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad Authorization");
            Gson gson = new Gson();
            session.getRemote().sendString(gson.toJson(errorMessage));
            throw new IOException("Error: unable to connect");
        }
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException {

        try {
            // verify move is valid
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            String color = command.getColor();
            ChessGame currGameState = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();
            if (gameEndCheck(command.getGameID(), username, color, currGameState)) { return;  } // checks if game has ended already
            Collection<ChessMove> validMoves = currGameState.validMoves(command.getMove().getStartPosition());
            if (validMoves == null || !validMoves.contains(command.getMove())) { //not a valid move
                ErrorMessage invalidMoveError = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid move chosen");
                connections.directBroadcast(command.getGameID(), username, color, invalidMoveError);
                return;
            }
            // move is valid, try to make move, should throw if wrong turn, into check, etc.
            try {
                currGameState.makeMove(command.getMove());
            } catch (InvalidMoveException e) {
                ErrorMessage invalidMoveError = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid move chosen");
                connections.directBroadcast(command.getGameID(), username, color, invalidMoveError);
                return;
            }
            // update game state in db
            new dataaccess.GameSQLDAO().updateGameState(command.getGameID(), currGameState);

            // send Load_game message to all clients
            LoadGameMessage updatedBoardMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGameState, "ignore");
            connections.broadcast(command.getGameID(), "", "", updatedBoardMessage);

            // Send notification message to all other clients
            char startCol = (char) ('a' + command.getMove().getStartPosition().getColumn() - 1);
            int startRow = command.getMove().getStartPosition().getRow();
            char endCol = (char) ('a' + command.getMove().getEndPosition().getColumn() - 1);
            int endRow = command.getMove().getEndPosition().getRow();
            String notification = String.format("%s moved from %c%d to %c%d", username, startCol, startRow, endCol, endRow);
            NotificationMessage notifyMoveMade = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
            connections.broadcast(command.getGameID(), username, command.getColor(), notifyMoveMade);

            // if move results in check, checkmate, or stalemate, send notification to all clients
            String checkCheckmateStalemate = "";
            if (currGameState.isInStalemate(ChessGame.TeamColor.WHITE)) {
                checkCheckmateStalemate = "White is in STALEMATE\nGAME END";
            } else if (currGameState.isInStalemate(ChessGame.TeamColor.BLACK)) {
                checkCheckmateStalemate = "Black is in STALEMATE\nGAME END";
            } else if (currGameState.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                checkCheckmateStalemate = "White is in CHECKMATE\nGAME END";
            } else if (currGameState.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                checkCheckmateStalemate = "Black is in CHECKMATE\nGAME END";
            } else if (currGameState.isInCheck(ChessGame.TeamColor.WHITE)) {
                checkCheckmateStalemate = "White is in check\nGAME END";
            } else if (currGameState.isInCheck(ChessGame.TeamColor.BLACK)) {
                checkCheckmateStalemate = "Black is in check\n";
            }

            if (!checkCheckmateStalemate.isEmpty()) {
                NotificationMessage gameStatusNotify = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkCheckmateStalemate);
                connections.broadcast(command.getGameID(), "", "", gameStatusNotify);
            }

        } catch (SQLException | DataAccessException e) {
            throw new IOException("Error: unable to connect");
        }
    }

    private Boolean gameEndCheck(int gameID, String username, String color, ChessGame currGameState) throws IOException {
        if (currGameState.isInCheckmate(ChessGame.TeamColor.WHITE) ||
                currGameState.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                currGameState.isInStalemate(ChessGame.TeamColor.WHITE) ||
                currGameState.isInStalemate(ChessGame.TeamColor.BLACK) ||
                currGameState.getGameFinished()) {
            ErrorMessage gameOverError = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Game finished");
            connections.directBroadcast(gameID, username, color, gameOverError);
            return true;
        }
        return false;
    }

    private void leave(ConnectCommand command) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            String color = command.getColor();
            connections.remove(command.getGameID(), username, color);
            if (!color.equals("observer")) {
                new GameSQLDAO().removeUserFromGame(color, username, command.getGameID()); // should remove user from game
            }

            //notify
            String notificationLeave;
            if (command.getColor().equals("observer")) {
                notificationLeave = String.format("%s has stopped observing game", username);
            } else {
                notificationLeave = String.format("%s has stopped playing", username);
            }
            var newMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationLeave);
            connections.broadcast(command.getGameID(), username, color, newMessage);
        } catch (DataAccessException | SQLException e) {
            throw new IOException("Error: unable to leave game");
        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            ChessGame currGameState = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();
            currGameState.setGameFinished(true);
            new dataaccess.GameSQLDAO().updateGameState(command.getGameID(), currGameState);

            String resignText = String.format("%s has resigned\nGAME END", username);
            NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, resignText);
            connections.broadcast(command.getGameID(), "", "", message);


        } catch (DataAccessException | SQLException e) {
            throw new IOException("Error: bad resign request");
        }
    }

    private String autoAssign(String username) {
        if (username.contains("white")) {
            return "WHITE";
        } else if (username.contains("black")) {
            return "BLACK";
        } else {
            return "observer";
        }
    }


}
