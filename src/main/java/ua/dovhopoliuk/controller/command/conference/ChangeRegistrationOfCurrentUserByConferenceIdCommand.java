package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.FullConferenceDTO;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class ChangeRegistrationOfCurrentUserByConferenceIdCommand implements Command {
    private CommandJsonUtility<FullConferenceDTO> fullConferenceDTOCommandJsonUtility =
            new CommandJsonUtility<>(FullConferenceDTO.class);

    private ConferenceService conferenceService;

    public ChangeRegistrationOfCurrentUserByConferenceIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }


    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();

        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences", "")
                .replace("/changeRegistration", ""));

        System.out.println(conferenceId);

        conferenceService.changeRegistration(request, conferenceId);

        Conference conference = conferenceService.getConferenceById(conferenceId);
        boolean registered = conferenceService.isUserRegistered(request, conference);

        return fullConferenceDTOCommandJsonUtility.toJson(new FullConferenceDTO(conference, registered));
    }
}
