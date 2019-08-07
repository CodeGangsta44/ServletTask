package ua.dovhopoliuk.controller.command;

import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetCurrentUserCommand implements Command {
    private final UserService userService = new UserService();
    private final CommandJsonUtility<UserDTO> commandJsonUtility = new CommandJsonUtility<>(UserDTO.class);

    @Override
    public String execute(HttpServletRequest request) {
        User currentUser = (User)request.getSession().getAttribute("principals");

        UserDTO userDTO = new UserDTO(currentUser);

        return commandJsonUtility.toJson(userDTO);
    }
}
