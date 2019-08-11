package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.ReportDao;
import ua.dovhopoliuk.model.dto.ReportDTO;
import ua.dovhopoliuk.model.entity.Report;

import java.util.List;

public class ReportService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public List<Report> getAllReports() {
        try (ReportDao reportDao = daoFactory.createReportDao()) {
            return reportDao.findAll();
        }
    }

    public Report getReportById(Long id) {
        try (ReportDao reportDao = daoFactory.createReportDao()) {
            return reportDao.findById(id);
        }
    }

    public void saveReport(Report report) {
        try (ReportDao reportDao = daoFactory.createReportDao()) {
            reportDao.create(report);
        }
    }

    public void updateReport(ReportDTO reportDTO) {

        Report report = getReportById(reportDTO.getId());

        report.setTopic(reportDTO.getTopic());

        try (ReportDao reportDao = daoFactory.createReportDao()) {
            reportDao.update(report);
        }
    }

    public void deleteReport(Long reportId) {
        try (ReportDao reportDao = daoFactory.createReportDao()) {
            reportDao.delete(reportId);
        }
    }
}
