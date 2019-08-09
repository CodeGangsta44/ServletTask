package ua.dovhopoliuk.controller.command.report;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.report.get.GetAllReportsCommand;
import ua.dovhopoliuk.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetReportCommand implements Command {
    private Map<String, Command> commands;
    private ReportService reportService;

    public GetReportCommand(ReportService reportService) {
        this.reportService = reportService;

        commands = new HashMap<>();

        commands.put("", new GetAllReportsCommand(reportService));
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/reports/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
