package ua.dovhopoliuk.model.dto;

public class SpeakerStatisticsDTO {
    private Long totalReports;
    private Long totalConferences;
    private Long totalPeople;

    public Long getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Long totalReports) {
        this.totalReports = totalReports;
    }

    public Long getTotalConferences() {
        return totalConferences;
    }

    public void setTotalConferences(Long totalConferences) {
        this.totalConferences = totalConferences;
    }

    public Long getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Long totalPeople) {
        this.totalPeople = totalPeople;
    }
}
