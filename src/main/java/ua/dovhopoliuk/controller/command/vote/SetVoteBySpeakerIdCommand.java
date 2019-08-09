package ua.dovhopoliuk.controller.command.vote;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.service.VoteService;

import javax.servlet.http.HttpServletRequest;

public class SetVoteBySpeakerIdCommand implements Command {
    private CommandJsonUtility<Integer> integerCommandJsonUtility =
            new CommandJsonUtility<>(Integer.class);

    private VoteService voteService;

    public  SetVoteBySpeakerIdCommand(VoteService voteService) {
        this.voteService = voteService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long speakerId = Long.parseLong(path.replaceFirst(".*/votes/", ""));

        Integer mark = integerCommandJsonUtility.fromJson(CommandRequestBodyReaderUtility.readRequestBody(request));

        voteService.saveVote(request, speakerId, mark);
        return null;
    }
}

//    @PostMapping(value = "/{speaker}")
//    public void setVoteForSpeaker(@PathVariable User speaker, @RequestBody int mark) {
//        voteService.saveVote(speaker, mark);
//    }
