package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class GetConferenceByIdCommand implements Command {
    private CommandJsonUtility<ConferenceDTO> conferenceDTOCommandJsonUtility =
            new CommandJsonUtility<>(ConferenceDTO.class);

    private ConferenceService conferenceService;

    public GetConferenceByIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences/", ""));

        ConferenceDTO conference = new ConferenceDTO(conferenceService.getConferenceById(conferenceId));

        return conferenceDTOCommandJsonUtility.toJson(conference);
    }
}
