package ua.dovhopoliuk.model.dto;


import ua.dovhopoliuk.model.entity.ReportRequest;

public class ReportRequestDTO {
    private Long id;

    private String topic;

    private ConferenceDTO conference;

    private UserDTO speaker;

    public ReportRequestDTO(ReportRequest reportRequest) {
        this.id = reportRequest.getId();
        this.topic = reportRequest.getTopic();
        this.conference = new ConferenceDTO(reportRequest.getConference());
        this.speaker = new UserDTO(reportRequest.getSpeaker());
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

    public ConferenceDTO getConference() {
        return conference;
    }

    public void setConference(ConferenceDTO conference) {
        this.conference = conference;
    }

    public UserDTO getSpeaker() {
        return speaker;
    }

    public void setSpeaker(UserDTO speaker) {
        this.speaker = speaker;
    }
}
