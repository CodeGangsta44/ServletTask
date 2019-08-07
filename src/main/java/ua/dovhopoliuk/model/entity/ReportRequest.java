package ua.dovhopoliuk.model.entity;

public class ReportRequest {
    private Long id;
    private Report report;

    private boolean approvedBySpeaker;
    private boolean approvedByModerator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public boolean isApprovedBySpeaker() {
        return approvedBySpeaker;
    }

    public void setApprovedBySpeaker(boolean approvedBySpeaker) {
        this.approvedBySpeaker = approvedBySpeaker;
    }

    public boolean isApprovedByModerator() {
        return approvedByModerator;
    }

    public void setApprovedByModerator(boolean approvedByModerator) {
        this.approvedByModerator = approvedByModerator;
    }
}
