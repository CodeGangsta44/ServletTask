package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.conference.get.*;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetConferenceCommand implements Command {
    private Map<String, Command> commands;
    private ConferenceService conferenceService;

    public GetConferenceCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;

        commands = new HashMap<>();

        commands.put("",
                new GetAllConferencesCommand(conferenceService));
        commands.put("requests",
                new GetAllConferenceRequestsCommand(conferenceService));
        commands.put("finished",
                new GetAllFinishedConferencesCommand(conferenceService));
        commands.put("me",
                new GetAllConferencesByCurrentUserCommand(conferenceService));
        commands.put("id",
                new GetConferenceByIdCommand(conferenceService));
        commands.put("id/registeredGuests",
                new GetRegisteredUsersByConferenceIdCommand(conferenceService));
        commands.put("id/changeRegistration",
                new ChangeRegistrationOfCurrentUserByConferenceIdCommand(conferenceService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/conferences/?", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
