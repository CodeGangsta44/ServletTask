package ua.dovhopoliuk.model.exception;

public class UserNotAuthenticatedException extends RuntimeException{

    public UserNotAuthenticatedException() {}

    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
