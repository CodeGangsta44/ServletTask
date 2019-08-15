package ua.dovhopoliuk.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Report {
    private Long id;
    private String topic;
    private Conference conference;
    private User speaker;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass() || hashCode() != o.hashCode()){
            return false;
        }

        Report report = (Report)o;

        return new EqualsBuilder()
                .append(id, report.id)
                .append(topic, report.topic)
                .append(conference, report.conference)
                .append(speaker, report.speaker)
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
