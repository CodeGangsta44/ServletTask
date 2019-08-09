package ua.dovhopoliuk.controller.command.conference;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.dto.ConferenceDTO;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.service.ConferenceService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNewConferenceCommand implements Command {
    private CommandJsonUtility<ConferenceDTO> conferenceDTOCommandJsonUtility = new CommandJsonUtility<>(ConferenceDTO.class);
    private ConferenceService conferenceService;

    public AddNewConferenceCommand(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        ConferenceDTO conferenceDTO = conferenceDTOCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility.readRequestBody(request));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        Conference conference = new Conference();

        conference.setTopic(conferenceDTO.getTopic());
        conference.setEventDateTime(LocalDateTime.parse(conferenceDTO.getEventDateTime(), formatter));
        conference.setEventAddress(conferenceDTO.getEventAddress());
        conference.setDescription(conferenceDTO.getDescription());

        conferenceService.addNewConference(conference);

        return null;
    }
}
