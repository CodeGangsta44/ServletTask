package ua.dovhopoliuk.controller.command.reportRequest;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.reportRequest.post.CreateReportRequestByConferenceIdCommand;
import ua.dovhopoliuk.controller.command.reportRequest.post.ProcessReportRequestCommand;
import ua.dovhopoliuk.model.service.ReportRequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PostReportRequestCommand implements Command {
    private Map<String, Command> commands;
    private ReportRequestService reportRequestService;

    public PostReportRequestCommand(ReportRequestService reportRequestService) {
        this.reportRequestService = reportRequestService;

        commands = new HashMap<>();

        commands.put("request/id",
                new CreateReportRequestByConferenceIdCommand(reportRequestService));
        commands.put("id",
                new ProcessReportRequestCommand(reportRequestService));


    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/reportRequests/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
