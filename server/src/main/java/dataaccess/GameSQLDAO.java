package dataaccess;

import model.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameSQLDAO implements GameDAO {

    public void clear() {

    }

    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public Model.GameData getGame(int gameID) {
        return null;
    }

    public ArrayList<Model.PrintGameData> listGame() {
        return null;
    }

    public void updateGame(String playerColor, String username, int gameID) {

    }

    String gameTableCreation = """
            CREATE TABLE IF NOT EXISTS  gameData (
            gameID int NOT NULL PRIMARY KEY,
            whiteUsername varchar(256) DEFAULT NULL,
            blackUsername varchar(256) DEFAULT NULL,
            gameName varchar(256) NOT NULL,
            game TEXT NOT NULL
            )
            """;

    public void clearGameDAO() {
        try (var conn = DatabaseManager.getConnection()) {
            String drop = "TRUNCATE TABLE gameData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                dropStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            return ;
        } catch (SQLException e) {
            return ;
        }
    }


}

