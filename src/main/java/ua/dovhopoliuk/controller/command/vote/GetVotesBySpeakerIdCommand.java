package ua.dovhopoliuk.controller.command.vote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.entity.Vote;
import ua.dovhopoliuk.model.service.VoteService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class GetVotesBySpeakerIdCommand implements Command {

    private VoteService voteService;

    public  GetVotesBySpeakerIdCommand(VoteService voteService) {
        this.voteService = voteService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long speakerId = Long.parseLong(path.replaceFirst(".*/votes/", ""));

        Map<Integer, Long> votes = voteService.getAllVotesBySpeaker(speakerId).stream()
                .collect(Collectors.groupingBy(Vote::getMark, Collectors.counting()));

        return new Gson().toJson(votes);
    }
}
