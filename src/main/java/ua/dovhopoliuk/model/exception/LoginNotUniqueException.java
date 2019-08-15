package ua.dovhopoliuk.model.exception;


import ua.dovhopoliuk.model.dto.RegNoteDTO;

public class LoginNotUniqueException extends RuntimeException {
    private RegNoteDTO note;
    private String localizedMessage;

    public LoginNotUniqueException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    public void setNote(RegNoteDTO note) {
        this.note = note;
    }

    public RegNoteDTO getNote() {
        return note;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}
