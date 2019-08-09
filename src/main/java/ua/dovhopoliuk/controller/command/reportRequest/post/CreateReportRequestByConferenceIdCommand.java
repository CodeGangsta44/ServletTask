package ua.dovhopoliuk.controller.command.reportRequest.post;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.entity.ReportRequest;
import ua.dovhopoliuk.model.service.ReportRequestService;

import javax.servlet.http.HttpServletRequest;

public class CreateReportRequestByConferenceIdCommand implements Command {
    private CommandJsonUtility<ReportRequest> reportRequestCommandJsonUtility =
            new CommandJsonUtility<>(ReportRequest.class);

    private ReportRequestService reportRequestService;

    public CreateReportRequestByConferenceIdCommand(ReportRequestService reportRequestService) {
        this.reportRequestService = reportRequestService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long conferenceId = Long.parseLong(path.replaceFirst(".*/reportRequests/request/", ""));

        ReportRequest reportRequest = reportRequestCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        reportRequestService.createReportRequest(request, reportRequest, conferenceId);

        return null;
    }
}

