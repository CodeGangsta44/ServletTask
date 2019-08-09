package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dto.ReportDTO;
import ua.dovhopoliuk.model.entity.Report;

import java.util.List;
import java.util.Objects;

public class ReportService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public List<Report> getAllReports() {
        return daoFactory.createReportDao().findAll();
    }

    public Report getReportById(Long id) {
        return daoFactory.createReportDao().findById(id);
    }

    public void saveReport(Report report) {
        daoFactory.createReportDao().create(report);
    }

    public void updateReport(ReportDTO reportDTO) {

        Report report = daoFactory.createReportDao().findById(reportDTO.getId());

        report.setTopic(reportDTO.getTopic());

        daoFactory.createReportDao().update(report);
    }

    public void deleteReport(Report report) {

        if (!Objects.isNull(report.getConference())) {
            report.getConference().getReports().remove(report);
        }

        daoFactory.createReportDao().delete(report.getId());
    }
}
