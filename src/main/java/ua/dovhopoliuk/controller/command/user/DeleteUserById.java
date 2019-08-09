package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class DeleteUserById implements Command
{
    private UserService userService;

    public DeleteUserById(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long userId = Long.parseLong(path.replaceFirst(".*/users/", ""));

        userService.deleteUser(userId);

        return null;
    }
}
