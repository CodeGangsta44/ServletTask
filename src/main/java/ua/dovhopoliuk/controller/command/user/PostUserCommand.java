package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.user.post.AuthenticateCommand;
import ua.dovhopoliuk.controller.command.user.post.RegisterUserCommand;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PostUserCommand implements Command {
    private Map<String, Command> commands;
    private UserService userService;

    public PostUserCommand(UserService userService) {
        this.userService = userService;

        commands = new HashMap<>();

        commands.put("",
                new RegisterUserCommand(userService));

        commands.put("authenticate",
                new AuthenticateCommand(userService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/users/?", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
