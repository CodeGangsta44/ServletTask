package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.dto.RegNoteDTO;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class UpdateUserCommand implements Command {
    private CommandJsonUtility<UserDTO> userDTOCommandJsonUtility =
            new CommandJsonUtility<>(UserDTO.class);

    private UserService userService;

    public UpdateUserCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        UserDTO userDTO = userDTOCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        userService.updateUser(userDTO);

        return CommandBundleUtility
                .getMessage(request, "message", "updating.success");
    }
}

