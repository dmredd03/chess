package server;
import com.google.gson.Gson;

import model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public Model.RegisterResult register(Model.RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, Model.RegisterResult.class);
    }

    public Model.LoginResult login(Model.LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, Model.LoginResult.class);
    }

    public Model.LogoutResult logout(Model.LogoutRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, Model.LogoutResult.class);
    }

    public Model.ListGameResult listGame(Model.ListGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, request, Model.ListGameResult.class);
    }

    public Model.CreateGameResult createGame(Model.CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, Model.CreateGameResult.class);
    }

    public Model.JoinGameResult joinGame(Model.JoinGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, request, Model.JoinGameResult.class);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            if (!isSuccessful(http.getResponseCode())) {
                try (InputStream respErr = http.getErrorStream()) {
                    if (respErr != null) { throw ResponseException.fromJson(respErr); }
                }
            }

            return readBody(http, responseClass);
        }  catch (ResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
