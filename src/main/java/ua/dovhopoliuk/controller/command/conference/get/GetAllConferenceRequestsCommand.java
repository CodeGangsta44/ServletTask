package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class GetAllConferenceRequestsCommand implements Command {
    private CommandJsonUtility<ConferenceDTO[]> conferenceDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(ConferenceDTO[].class);

    private ConferenceService conferenceService;

    public GetAllConferenceRequestsCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        ConferenceDTO[] conferenceRequests = conferenceService.getAllNotApprovedConferences().stream()
                .map(ConferenceDTO::new).toArray(ConferenceDTO[]::new);

        return conferenceDTOArrayCommandJsonUtility.toJson(conferenceRequests);
    }
}

