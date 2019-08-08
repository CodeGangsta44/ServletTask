package ua.dovhopoliuk.model.entity;

public class ReportRequest {
    private Long id;
    private String topic;
    private Conference conference;
    private User speaker;

    private boolean approvedBySpeaker;
    private boolean approvedByModerator;

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

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public User getSpeaker() {
        return speaker;
    }

    public void setSpeaker(User speaker) {
        this.speaker = speaker;
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
