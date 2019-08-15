package ua.dovhopoliuk.controller.command.user.post;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandBCryptUtility;
import ua.dovhopoliuk.controller.command.utility.CommandSessionUtility;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class AuthenticateCommand implements Command {
    private UserService userService;

    public AuthenticateCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = userService.getUserByLogin(login);

        if (user == null || !CommandBCryptUtility.isPasswordMatches(password, user.getPassword())) {
            return "redirect:/login?error";
        } else {
            CommandSessionUtility.setUserForSession(request, user);
            return "redirect:/";
        }
    }
}
