package ua.dovhopoliuk.model.dto;

import ua.dovhopoliuk.model.entity.User;

public class RegisteredGuestDTO {
    private Long id;
    private String login;
    private String surname;
    private String name;

    public RegisteredGuestDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.surname = user.getSurname();
        this.name = user.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

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
}
