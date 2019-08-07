package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;

public class UserService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public void registerUser(User user) {
        daoFactory.createUserDAO().create(user);
    }

    public User getUserByLogin(String login) {
        return daoFactory.createUserDAO().findByLogin(login);
    }
}
