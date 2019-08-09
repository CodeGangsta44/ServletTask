package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetUserByIdCommand implements Command {
    private CommandJsonUtility<UserDTO> userDTOCommandJsonUtility =
            new CommandJsonUtility<>(UserDTO.class);

    private UserService userService;

    public GetUserByIdCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long userId = Long.parseLong(path.replaceFirst(".*/users/", ""));

        UserDTO userDTO = new UserDTO(userService.getUserById(userId));

        return userDTOCommandJsonUtility.toJson(userDTO);
    }
}
