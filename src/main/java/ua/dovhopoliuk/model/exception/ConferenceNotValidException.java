package ua.dovhopoliuk.model.exception;

public class ConferenceNotValidException extends RuntimeException {
    public ConferenceNotValidException() {}

    public ConferenceNotValidException(String message) {
        super(message);
    }
}
