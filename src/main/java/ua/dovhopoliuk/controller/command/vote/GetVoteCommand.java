package ua.dovhopoliuk.controller.command.vote;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.vote.get.GetVoteBySpeakerIdAndCurrentUserCommand;
import ua.dovhopoliuk.controller.command.vote.get.GetVotesBySpeakerIdCommand;
import ua.dovhopoliuk.model.service.VoteService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetVoteCommand implements Command {
    private Map<String, Command> commands;
    private VoteService voteService;

    public GetVoteCommand(VoteService voteService) {
        this.voteService = voteService;

        commands = new HashMap<>();

        commands.put("id",
                new GetVotesBySpeakerIdCommand(voteService));
        commands.put("id/me",
                new GetVoteBySpeakerIdAndCurrentUserCommand(voteService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/votes/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
