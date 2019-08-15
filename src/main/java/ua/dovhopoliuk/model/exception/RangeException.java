package ua.dovhopoliuk.model.exception;

public class RangeException extends Exception {
    private String localizedMessage;

    public RangeException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
