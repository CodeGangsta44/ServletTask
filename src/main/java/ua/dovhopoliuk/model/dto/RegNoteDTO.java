package ua.dovhopoliuk.model.dto;

import ua.dovhopoliuk.controller.validation.Status;

import java.util.List;
import java.util.Map;

public class RegNoteDTO {

    private String surname;
    private String name;
    private String patronymic;
    private String login;

    private String email;
    private String password;

    private String isSpeaker;

    private Status status;
    private Map<String, List<String>> validationMessages;
    private String localizedMessage;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsSpeaker() {
        return isSpeaker;
    }

    public void setIsSpeaker(String isSpeaker) {
        this.isSpeaker = isSpeaker;
    }

    public Map<String, List<String>> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(Map<String, List<String>> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}