package ua.dovhopoliuk.model.dto;


import ua.dovhopoliuk.model.entity.Conference;

public class FinishedConferenceDTO {
    private Long id;
    private String topic;
    private String eventDateTime;
    private String eventAddress;
    private String description;

    private Long numberOfRegisteredGuests;
    private Long numberOfVisitedGuests;

    public FinishedConferenceDTO(Conference conference) {
        this.id = conference.getId();
        this.topic = conference.getTopic();
        this.eventDateTime = conference.getEventDateTime().toString();
        this.eventAddress = conference.getEventAddress();
        this.description = conference.getDescription();

        this.numberOfRegisteredGuests = (long) conference.getRegisteredGuests().size();
        this.numberOfVisitedGuests = conference.getNumberOfVisitedGuests();
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

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
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

    public Long getNumberOfRegisteredGuests() {
        return numberOfRegisteredGuests;
    }

    public void setNumberOfRegisteredGuests(Long numberOfRegisteredGuests) {
        this.numberOfRegisteredGuests = numberOfRegisteredGuests;
    }

    public Long getNumberOfVisitedGuests() {
        return numberOfVisitedGuests;
    }

    public void setNumberOfVisitedGuests(Long numberOfVisitedGuests) {
        this.numberOfVisitedGuests = numberOfVisitedGuests;
    }
}
