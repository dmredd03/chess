package serverfacade;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {
    final private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

    public static ResponseException fromJson(InputStream stream) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        int status = 0;
        Object statusObj = map.get("status");
        if (statusObj instanceof Number) {
            status = ((Number) statusObj).intValue();
        } else if (statusObj != null) {
            try {
                status = Integer.parseInt(statusObj.toString());
            } catch (NumberFormatException ignored) {
                status = 0;
            }
        }
        String message = map.getOrDefault("message", "Unknown error").toString();
        return new ResponseException(status, message);
    }

}