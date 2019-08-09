package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.user.delete.DeleteUserById;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class DeleteUserCommand implements Command {
    private Map<String, Command> commands;
    private UserService userService;

    public DeleteUserCommand(UserService userService) {
        this.userService = userService;

        commands = new HashMap<>();

        commands.put("id", new DeleteUserById(userService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/users/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
