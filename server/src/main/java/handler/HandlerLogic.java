package handler;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.model;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import service.UserService;

public class HandlerLogic implements Route {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public HandlerLogic(MemoryUserDAO userDAO, MemoryAuthDAO authDAO, MemoryGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }


    public Object handle(Request req, Response res) {
        if (req.pathInfo().equals("/user")) {
            RegisterHandler(req, res);
        } else {

        }

        return res;
    }

    private Object RegisterHandler(Request req, Response res) {
        // deserialize JSON request body to Java request object
        var serializer = new Gson();
        model.RegisterRequest myRequest = serializer.fromJson(req.body(), model.RegisterRequest.class);

        // Call service.service class to perform the requested function, passing it the request
        UserService service = new UserService(userDAO, authDAO, gameDAO);
        // Receive java response object from service.service
        model.RegisterResult registerResult = service.register(myRequest);
        // Serialize Java response object to JSON
        //Send HTTP response back to client with status code and response body
        return serializer.toJson(registerResult);
    }

}



/*class RegisterRequest {
    private RegisterRequest regReq;
    public void fromJSON(Request reqData) {
        var deserializer = new Gson();
        regReq = deserializer.fromJson(reqData.body(), RegisterRequest.class);
    }

    public RegisterRequest getRequest() {
        return regReq;
    }

    private Response toJSON(ArrayList<String> objData) {
        // IMPLEMENT
        return null;
    }
}*/
