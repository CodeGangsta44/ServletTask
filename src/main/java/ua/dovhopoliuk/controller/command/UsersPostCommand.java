package ua.dovhopoliuk.controller.command;

import ua.dovhopoliuk.controller.command.user.post.AuthenticateCommand;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.RegNoteDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class UsersPostCommand implements Command {
    private CommandJsonUtility<RegNoteDTO> regNoteDTOCommandJsonUtility = new CommandJsonUtility<>(RegNoteDTO.class);
//    private final UserService userService = new UserService();
    private HashMap<String, Command> commands = new HashMap<>();

    {
//        commands.put("", new RegisterUserCommand());
        commands.put("authenticate", new AuthenticateCommand());
    }

    @Override
    public String execute(HttpServletRequest request) {
        System.out.println();
        String path = request.getRequestURI();
        path = path.replaceAll(".*/api/users/" , "");
        System.out.println(path);
        Command command = commands.get(path);
        return command.execute(request);
    }

}
