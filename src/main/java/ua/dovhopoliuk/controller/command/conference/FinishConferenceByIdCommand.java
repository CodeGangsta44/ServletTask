package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class FinishConferenceByIdCommand implements Command {
    private CommandJsonUtility<Long> longCommandJsonUtility =
            new CommandJsonUtility<>(Long.class);
    private ConferenceService conferenceService;

    public FinishConferenceByIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences", "")
                .replace("/finish", ""));

        Long numberOfVisitedGuests = longCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        conferenceService.finish(conferenceId, numberOfVisitedGuests);

        return null;
    }
}
