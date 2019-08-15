package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.conference.post.AddNewConferenceCommand;
import ua.dovhopoliuk.controller.command.conference.post.ProcessRequestByConferenceIdCommand;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PostConferenceCommand implements Command {
    private Map<String, Command> commands;
    private ConferenceService conferenceService;

    public PostConferenceCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;

        commands = new HashMap<>();

        commands.put("",
                new AddNewConferenceCommand(conferenceService));
        commands.put("id/processRequest", new ProcessRequestByConferenceIdCommand(conferenceService));

    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/conferences/?", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e) -> "redirect:/");
        return command.execute(request);
    }
}
