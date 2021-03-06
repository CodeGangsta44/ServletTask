package ua.dovhopoliuk.controller.command.report.get;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ReportDTO;
import ua.dovhopoliuk.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;

public class GetAllReportsOfCurrentUser implements Command {
    private CommandJsonUtility<ReportDTO[]> reportDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(ReportDTO[].class);

    private ReportService reportService;

    public GetAllReportsOfCurrentUser(ReportService reportService) {
        this.reportService = reportService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        ReportDTO[] reportRequests = reportService.getAllReportsOfCurrentUser(request).stream()
                .map(ReportDTO::new).toArray(ReportDTO[]::new);

        return reportDTOArrayCommandJsonUtility.toJson(reportRequests);
    }
}
