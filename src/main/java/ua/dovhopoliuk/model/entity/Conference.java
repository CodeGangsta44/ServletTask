package ua.dovhopoliuk.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.util.Set;

public class Conference {
    private Long id;
    private String topic;
    private LocalDateTime eventDateTime;
    private String eventAddress;
    private String description;

    private Set<Report> reports;
    private Set<User> registeredGuests;

    private boolean approved;
    private boolean finished;
    private Long numberOfVisitedGuests;

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

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<User> getRegisteredGuests() {
        return registeredGuests;
    }

    public void setRegisteredGuests(Set<User> registeredGuests) {
        this.registeredGuests = registeredGuests;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Long getNumberOfVisitedGuests() {
        return numberOfVisitedGuests;
    }

    public void setNumberOfVisitedGuests(Long numberOfVisitedGuests) {
        this.numberOfVisitedGuests = numberOfVisitedGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass() || hashCode() != o.hashCode()){
            return false;
        }

        Conference conference = (Conference)o;

        return new EqualsBuilder()
                .append(id, conference.id)
                .append(topic, conference.topic)
                .append(eventDateTime, conference.eventDateTime)
                .append(eventAddress, conference.eventAddress)
                .append(description, conference.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(topic)
                .append(eventDateTime)
                .append(eventAddress)
                .append(description)
                .hashCode();
    }
}
