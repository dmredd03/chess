import spark.Spark;
import spark.Request;
import spark.Response;

public class server {
    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);
            Spark.staticFiles.location("web");

            // Register your endpoints and handle exceptions here.
            createRoutes();

            Spark.awaitInitialization();
            return Spark.port();
        } catch(ArrayIndexOutOfBoundsException ex) { // TODO: add more exceptions here
            return desiredPort;
        }
    }

    public void stop() {
        Spark.stop();
    }

    private void createRoutes() {
        Spark.post("/user", new handler());
    }

}
