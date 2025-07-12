import com.google.gson.GsonBuilder;
import spark.Spark;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

class handler implements Route {

    public Object handle(Request req, Response res) {
        if (req.pathInfo().equals("/user")) {
            RegisterHandler(req, res);
        } else {

        }

        return res;
    }

    private Object RegisterHandler(Request req, Response res) {
        // deserialize JSON request body to Java request object
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.fromJSON(req);

        // Call service class to perform the requested function, passing it the request
        RegisterService service = new RegisterService();
        service.register(registerRequest.getRequest());
        // Receive java response object from service

        // Serialize Java response object to JSON

        //Send HTTP response back to client with status code and response body
        return serializer.toJson(); // Change later to RegisterResult
    }

}



class RegisterRequest {
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
    }
}
