package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class GetAllConferencesCommand implements Command {
    private CommandJsonUtility<ConferenceDTO[]> conferenceDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(ConferenceDTO[].class);

    private ConferenceService conferenceService;

    public GetAllConferencesCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String page = request.getParameter("page");
        String capacity = request.getParameter("capacity");

        ConferenceDTO[] conferences;

        if (!Objects.isNull(page) && !Objects.isNull(capacity)) {
            conferences = conferenceService.getAllValidConferencesByPage(Integer.parseInt(page), Integer.parseInt(capacity)).stream()
                    .map(ConferenceDTO::new).toArray(ConferenceDTO[]::new);

        } else {
            conferences = conferenceService.getAllValidConferences().stream()
                    .map(ConferenceDTO::new).toArray(ConferenceDTO[]::new);
        }

        return conferenceDTOArrayCommandJsonUtility.toJson(conferences);
    }
}
