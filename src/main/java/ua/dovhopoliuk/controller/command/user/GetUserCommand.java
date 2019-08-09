package ua.dovhopoliuk.controller.command.user;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.user.get.*;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetUserCommand implements Command {
    private Map<String, Command> commands;
    private UserService userService;

    public GetUserCommand(UserService userService) {
        this.userService = userService;

        commands = new HashMap<>();

        commands.put("",
                new GetAllUsersCommand(userService));
        commands.put("me",
                new GetCurrentUserCommand(userService));
        commands.put("id",
                new GetUserByIdCommand(userService));
        commands.put("myRoles",
                new GetRolesOfCurrentUserCommand(userService));
        commands.put("speakerStatistics/id",
                new GetSpeakerStatisticsByUserId(userService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/users/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
