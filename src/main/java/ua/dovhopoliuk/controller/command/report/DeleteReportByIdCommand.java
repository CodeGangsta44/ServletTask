package ua.dovhopoliuk.controller.command.report;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;

public class DeleteReportByIdCommand implements Command {
    private ReportService reportService;

    public DeleteReportByIdCommand(ReportService reportService) {
        this.reportService = reportService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long reportId = Long.parseLong(path.replaceFirst(".*/reports/", ""));

        reportService.deleteReport(reportId);

        return null;
    }
}
