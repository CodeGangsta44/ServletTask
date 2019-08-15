package ua.dovhopoliuk.model.exception;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
