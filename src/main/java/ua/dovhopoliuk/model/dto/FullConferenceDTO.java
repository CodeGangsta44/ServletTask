package ua.dovhopoliuk.model.dto;


import ua.dovhopoliuk.model.entity.Conference;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class FullConferenceDTO {
    private Long id;
    private String topic;
    private String eventDateTime;
    private String eventAddress;

    private Set<UserDTO> registeredGuests;
    private Set<ReportDTO> reports;

    private String description;
    private boolean registered;

    public FullConferenceDTO(Conference conference, boolean registered) {
        this.id = conference.getId();
        this.topic = conference.getTopic();
        this.eventDateTime = conference.getEventDateTime().toString();
        this.eventAddress = conference.getEventAddress();

        this.registeredGuests = conference.getRegisteredGuests()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toSet());

        this.reports = conference.getReports()
                .stream()
                .map(ReportDTO::new)
                .collect(Collectors.toSet());

        this.description = conference.getDescription();
        this.registered = registered;
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

    public Set<UserDTO> getRegisteredGuests() {
        return registeredGuests;
    }

    public void setRegisteredGuests(Set<UserDTO> registeredGuests) {
        this.registeredGuests = registeredGuests;
    }

    public Set<ReportDTO> getReports() {
        return reports;
    }

    public void setReports(Set<ReportDTO> reports) {
        this.reports = reports;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
