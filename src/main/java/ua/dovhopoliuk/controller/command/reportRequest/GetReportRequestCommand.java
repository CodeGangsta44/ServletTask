package ua.dovhopoliuk.controller.command.reportRequest;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.reportRequest.get.GetAllReportProposesOfCurrentUserCommand;
import ua.dovhopoliuk.controller.command.reportRequest.get.GetAllReportRequestsCommand;
import ua.dovhopoliuk.model.service.ReportRequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetReportRequestCommand implements Command {
    private Map<String, Command> commands;
    private ReportRequestService reportRequestService;

    public GetReportRequestCommand(ReportRequestService reportRequestService) {
        this.reportRequestService = reportRequestService;

        commands = new HashMap<>();

        commands.put("",
                new GetAllReportRequestsCommand(reportRequestService));
        commands.put("me",
                new GetAllReportProposesOfCurrentUserCommand(reportRequestService));

    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/reportRequests/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
