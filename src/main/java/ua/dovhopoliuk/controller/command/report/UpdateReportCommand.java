package ua.dovhopoliuk.controller.command.report;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.model.dto.ReportDTO;
import ua.dovhopoliuk.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;

public class UpdateReportCommand implements Command {
    private CommandJsonUtility<ReportDTO> reportDTOCommandJsonUtility =
            new CommandJsonUtility<>(ReportDTO.class);

    private ReportService reportService;

    public UpdateReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        ReportDTO reportDTO = reportDTOCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        reportService.updateReport(reportDTO);

        return null;
    }
}
