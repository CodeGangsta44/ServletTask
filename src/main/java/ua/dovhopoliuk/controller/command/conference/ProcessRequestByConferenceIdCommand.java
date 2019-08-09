package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class ProcessRequestByConferenceIdCommand implements Command {
    private CommandJsonUtility<Boolean> booleanCommandJsonUtility =
            new CommandJsonUtility<>(Boolean.class);

    private ConferenceService conferenceService;

    public ProcessRequestByConferenceIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences", "")
                .replace("/processRequest", ""));

        Boolean answer = booleanCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        System.out.println(answer);

        if (answer) {
            conferenceService.approve(conferenceId);
        } else {
            conferenceService.reject(conferenceId);
        }

        return null;
    }
}
