package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.RegisteredGuestDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class GetRegisteredUsersByConferenceIdCommand implements Command {
    private CommandJsonUtility<RegisteredGuestDTO[]> registeredGuestDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(RegisteredGuestDTO[].class);

    private ConferenceService conferenceService;

    public GetRegisteredUsersByConferenceIdCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/conferences/", "")
                .replace("/registeredGuests", ""));

        RegisteredGuestDTO[] registeredGuest = conferenceService.getRegisteredUsers(conferenceId).stream()
                .map(RegisteredGuestDTO::new).toArray(RegisteredGuestDTO[]::new);

        return registeredGuestDTOArrayCommandJsonUtility.toJson(registeredGuest);
    }
}

