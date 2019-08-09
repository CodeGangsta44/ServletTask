package ua.dovhopoliuk.controller.command.vote;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.entity.Vote;
import ua.dovhopoliuk.model.service.VoteService;

import javax.servlet.http.HttpServletRequest;

public class GetVoteBySpeakerIdAndCurrentUserCommand implements Command {
    private CommandJsonUtility<Integer> integerCommandJsonUtility =
            new CommandJsonUtility<>(Integer.class);

    private VoteService voteService;

    public  GetVoteBySpeakerIdAndCurrentUserCommand(VoteService voteService) {
        this.voteService = voteService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long speakerId = Long.parseLong(path.replaceFirst(".*/votes/", "")
                .replace("/me", ""));

        Integer mark = voteService.getVoteOfCurrentUser(request, speakerId);
        return integerCommandJsonUtility.toJson(mark);
    }
}

