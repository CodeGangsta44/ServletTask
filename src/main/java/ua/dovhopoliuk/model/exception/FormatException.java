package ua.dovhopoliuk.model.exception;

public class FormatException extends Exception {
    private String localizedMessage;

    public FormatException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
