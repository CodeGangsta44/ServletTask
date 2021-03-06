package ua.dovhopoliuk.controller.command.user.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetAllUsersCommand implements Command {
    private CommandJsonUtility<UserDTO[]> userDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(UserDTO[].class);

    private UserService userService;

    public GetAllUsersCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        UserDTO[] users = userService.getAllUsers().stream()
                .map(UserDTO::new).toArray(UserDTO[]::new);

        return userDTOArrayCommandJsonUtility.toJson(users);
    }
}
