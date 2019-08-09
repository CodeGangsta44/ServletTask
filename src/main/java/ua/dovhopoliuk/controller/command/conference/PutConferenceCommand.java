package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.conference.put.FinishConferenceByIdCommand;
import ua.dovhopoliuk.controller.command.conference.put.UpdateConferenceByIdCommand;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PutConferenceCommand implements Command {
    private Map<String, Command> commands;
    private ConferenceService conferenceService;

    public PutConferenceCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;

        commands = new HashMap<>();

        commands.put("id",
                new UpdateConferenceByIdCommand(conferenceService));
        commands.put("id/finish",
                new FinishConferenceByIdCommand(conferenceService));

    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/conferences/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
