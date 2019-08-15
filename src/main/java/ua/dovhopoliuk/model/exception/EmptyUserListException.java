package ua.dovhopoliuk.model.exception;

public class EmptyUserListException extends RuntimeException {
    private String localizedMessage;

    public EmptyUserListException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}