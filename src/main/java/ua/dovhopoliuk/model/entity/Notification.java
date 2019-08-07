package ua.dovhopoliuk.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Notification {

    private Long id;
    private User addressedUser;
    private LocalDateTime notificationDateTime;

    private String topicKey;
    private List<String> topicValues;
    private String messageKey;
    private List<String> messageValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAddressedUser() {
        return addressedUser;
    }

    public void setAddressedUser(User addressedUser) {
        this.addressedUser = addressedUser;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getTopicKey() {
        return topicKey;
    }

    public void setTopicKey(String topicKey) {
        this.topicKey = topicKey;
    }

    public List<String> getTopicValues() {
        return topicValues;
    }

    public void setTopicValues(List<String> topicValues) {
        this.topicValues = topicValues;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public List<String> getMessageValues() {
        return messageValues;
    }

    public void setMessageValues(List<String> messageValues) {
        this.messageValues = messageValues;
    }
}
