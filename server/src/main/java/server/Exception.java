package server;


public class Exception extends RuntimeException {

    private int statusCode;

    public Exception(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

