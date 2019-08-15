package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.dto.FullConferenceDTO;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class GetConferenceByIdCommand implements Command {
    private CommandJsonUtility<FullConferenceDTO> fullConferenceDTOCommandJsonUtility =
            new CommandJsonUtility<>(FullConferenceDTO.class);

    private ConferenceService conferenceService;

    public GetConferenceByIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences/", ""));

        Conference conference = conferenceService.getConferenceById(conferenceId);
        boolean isRegistered = conferenceService.isUserRegistered(request, conference);

        FullConferenceDTO fullConferenceDTO = new FullConferenceDTO(conference, isRegistered);

        return fullConferenceDTOCommandJsonUtility.toJson(fullConferenceDTO);
    }
}
