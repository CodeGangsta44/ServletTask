package ua.dovhopoliuk.controller.command.conference.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class GetNumberOfAllValidConferences implements Command {
    private CommandJsonUtility<Integer> conferenceDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(Integer.class);

    private ConferenceService conferenceService;

    public GetNumberOfAllValidConferences(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return conferenceDTOArrayCommandJsonUtility.toJson(conferenceService.getTotalNumberOfValidConferences());
    }
}
