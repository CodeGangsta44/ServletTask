package ua.dovhopoliuk.model.exception;

public class VoteNotFoundException extends RuntimeException {
    public VoteNotFoundException() {}

    public VoteNotFoundException(String message) {
        super(message);
    }
}
