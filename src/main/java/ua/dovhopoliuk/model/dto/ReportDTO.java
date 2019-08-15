package ua.dovhopoliuk.model.dto;


import ua.dovhopoliuk.model.entity.Report;

public class ReportDTO {
    private Long id;
    private String topic;
    private UserDTO speaker;
    private ConferenceDTO conference;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.topic = report.getTopic();
        this.speaker = new UserDTO(report.getSpeaker());
        this.conference = new ConferenceDTO(report.getConference());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public UserDTO getSpeaker() {
        return speaker;
    }

    public void setSpeaker(UserDTO speaker) {
        this.speaker = speaker;
    }

    public ConferenceDTO getConference() {
        return conference;
    }

    public void setConference(ConferenceDTO conference) {
        this.conference = conference;
    }
}
