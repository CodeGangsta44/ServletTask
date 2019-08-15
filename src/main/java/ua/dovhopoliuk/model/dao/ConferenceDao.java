package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.entity.Conference;

import java.util.List;

public interface ConferenceDao extends GenericDao<Conference> {
    List<Conference> findAllByApprovedIsTrueAndFinishedIsFalse();
    List<Conference> findAllByApprovedIsTrueAndFinishedIsFalse(Integer startPosition, Integer finishPosition);
    List<Conference> findAllByApprovedIsFalse();
    List<Conference> findAllByFinishedIsTrue();
    List<Conference> findAllByRegisteredGuestsContainsAndApprovedIsTrueAndFinishedIsFalse(Long userId);
    Integer getTotalNumberOfConferencesByApprovedIsTrueAndFinishedIsFalse();
}
