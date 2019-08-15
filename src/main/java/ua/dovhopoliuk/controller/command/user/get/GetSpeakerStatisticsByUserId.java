package ua.dovhopoliuk.controller.command.user.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.SpeakerStatisticsDTO;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetSpeakerStatisticsByUserId implements Command {
    private CommandJsonUtility<SpeakerStatisticsDTO> speakerStatisticsDTOCommandJsonUtility =
            new CommandJsonUtility<>(SpeakerStatisticsDTO.class);

    private UserService userService;

    public GetSpeakerStatisticsByUserId(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long userId = Long.parseLong(path.replaceFirst(".*/users/speakerStatistics/", ""));

        SpeakerStatisticsDTO speakerStatisticsDTO = userService.getSpeakerStatistics(userId);

        return speakerStatisticsDTOCommandJsonUtility.toJson(speakerStatisticsDTO);
    }
}