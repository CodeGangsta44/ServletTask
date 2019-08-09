package ua.dovhopoliuk.model.dto;


import ua.dovhopoliuk.model.entity.Conference;

public class ConferenceDTO {
    private Long id;
    private String topic;
    private String eventDateTime;
    private String eventAddress;
    private String description;

    public ConferenceDTO(Conference conference) {
        this.id = conference.getId();
        this.topic = conference.getTopic();
        this.eventDateTime = conference.getEventDateTime().toString();
        this.eventAddress = conference.getEventAddress();
        this.description = conference.getDescription();
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
}
