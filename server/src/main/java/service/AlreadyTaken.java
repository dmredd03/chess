package service;

public class AlreadyTaken extends RuntimeException {
    public AlreadyTaken(String message) {
        super(message);
    }
    public AlreadyTaken(String message, Throwable ex) {
        super(message, ex);
    }
}
