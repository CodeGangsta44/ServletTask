package ua.dovhopoliuk.controller.command;

import ua.dovhopoliuk.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CommandSessionUtility {
    static void setUserForSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("principals", user);
    }

    static void logoutFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute("principals");
    }
}
