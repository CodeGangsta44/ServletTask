package ua.dovhopoliuk.controller.command.reportRequest.post;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.entity.ReportRequest;
import ua.dovhopoliuk.model.service.ReportRequestService;

import javax.servlet.http.HttpServletRequest;

public class ProcessReportRequestCommand implements Command {
    private CommandJsonUtility<Boolean> booleanCommandJsonUtility =
            new CommandJsonUtility<>(Boolean.class);

    private ReportRequestService reportRequestService;

    public ProcessReportRequestCommand(ReportRequestService reportRequestService) {
        this.reportRequestService = reportRequestService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        Boolean answer = booleanCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        String path = request.getRequestURI();
        Long reportRequestId = Long.parseLong(path.replaceFirst(".*/reportRequests/", ""));

        if (answer) {
            reportRequestService.approve(request, reportRequestId);
        } else {
            reportRequestService.reject(request, reportRequestId);
        }
        return null;
    }
}

