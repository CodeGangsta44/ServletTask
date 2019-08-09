package ua.dovhopoliuk.controller.command.user.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetRolesOfCurrentUserCommand implements Command {
    private CommandJsonUtility<Role[]> roleArrayCommandJsonUtility =
            new CommandJsonUtility<>(Role[].class);

    private UserService userService;

    public GetRolesOfCurrentUserCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        Role[] roles = userService.getCurrentUser(request).getRoles().toArray(new Role[0]);
        return roleArrayCommandJsonUtility.toJson(roles);
    }
}
