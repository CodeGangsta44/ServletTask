package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.User;

public class UserService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public void registerUser(User user) {
        daoFactory.createUserDao().create(user);
    }

    public User getUserByLogin(String login) {
        return daoFactory.createUserDao().findByLogin(login);
    }
}
