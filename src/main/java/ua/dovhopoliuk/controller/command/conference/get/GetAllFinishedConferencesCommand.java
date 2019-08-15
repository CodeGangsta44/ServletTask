package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.dto.FinishedConferenceDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;

public class GetAllFinishedConferencesCommand implements Command {
    private CommandJsonUtility<FinishedConferenceDTO[]> finishedConferenceDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(FinishedConferenceDTO[].class);

    private ConferenceService conferenceService;

    public GetAllFinishedConferencesCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        FinishedConferenceDTO[] conferences = conferenceService.getAllFinishedConferences().stream()
                .map(FinishedConferenceDTO::new).toArray(FinishedConferenceDTO[]::new);

        return finishedConferenceDTOArrayCommandJsonUtility.toJson(conferences);
    }
}
