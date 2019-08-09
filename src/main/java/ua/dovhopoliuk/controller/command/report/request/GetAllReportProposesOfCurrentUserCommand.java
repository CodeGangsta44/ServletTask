package ua.dovhopoliuk.controller.command.report.request;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.ReportRequestDTO;
import ua.dovhopoliuk.model.service.ReportRequestService;

import javax.servlet.http.HttpServletRequest;

public class GetAllReportProposesOfCurrentUserCommand implements Command {
    private CommandJsonUtility<ReportRequestDTO[]> reportRequestArrayCommandJsonUtility =
            new CommandJsonUtility<>(ReportRequestDTO[].class);

    private ReportRequestService reportRequestService;

    public GetAllReportProposesOfCurrentUserCommand(ReportRequestService reportRequestService) {
        this.reportRequestService = reportRequestService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        ReportRequestDTO[] reportRequests = reportRequestService.getProposedReports(request).stream()
                .map(ReportRequestDTO::new).toArray(ReportRequestDTO[]::new);

        return reportRequestArrayCommandJsonUtility.toJson(reportRequests);
    }
}
