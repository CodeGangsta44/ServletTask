package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class UpdateConferenceByIdCommand implements Command {
    private CommandJsonUtility<Conference> ConferenceCommandJsonUtility =
            new CommandJsonUtility<>(Conference.class);

    private ConferenceService conferenceService;

    public UpdateConferenceByIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences", ""));

        Conference conference = ConferenceCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        conferenceService.updateConferenceById(conferenceId, conference);

        return null;
    }
}
