package ua.dovhopoliuk.controller.command;

import ua.dovhopoliuk.model.dto.RegNoteDTO;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;
import ua.dovhopoliuk.model.exception.RequestException;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UsersPostCommand implements Command {
    private CommandJsonUtility<RegNoteDTO> regNoteDTOCommandJsonUtility = new CommandJsonUtility<>(RegNoteDTO.class);
    private final UserService userService = new UserService();
    private HashMap<String, Command> commands = new HashMap<>();

    {
        commands.put("", new RegisterUserCommand());
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
