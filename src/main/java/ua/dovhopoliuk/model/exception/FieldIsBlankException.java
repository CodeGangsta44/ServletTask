package ua.dovhopoliuk.model.exception;

public class FieldIsBlankException extends Exception {

    private String localizedMessage;

    public FieldIsBlankException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
