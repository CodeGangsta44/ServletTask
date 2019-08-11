package ua.dovhopoliuk.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass() || hashCode() != o.hashCode()){
            return false;
        }

        ReportRequest reportRequest = (ReportRequest)o;

        return new EqualsBuilder()
                .append(id, reportRequest.id)
                .append(topic, reportRequest.topic)
                .append(conference, reportRequest.conference)
                .append(speaker, reportRequest.speaker)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(topic)
                .append(conference)
                .append(speaker)
                .hashCode();
    }
}
