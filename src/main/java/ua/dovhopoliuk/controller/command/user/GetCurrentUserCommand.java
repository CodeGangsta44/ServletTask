package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetCurrentUserCommand implements Command {
    private CommandJsonUtility<UserDTO> UserDTOCommandJsonUtility =
            new CommandJsonUtility<>(UserDTO.class);

    private UserService userService;

    public GetCurrentUserCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        UserDTO user = new UserDTO(userService.getCurrentUser(request));
        return UserDTOCommandJsonUtility.toJson(user);
    }
}
