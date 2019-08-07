package ua.dovhopoliuk.model.entity;

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
}
