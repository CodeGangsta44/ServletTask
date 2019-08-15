package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.entity.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteDao extends GenericDao<Vote> {
    List<Vote> findAllBySpeakerId(Long id);
    Optional<Vote> findBySpeakerAndUser(Long speakerId, Long userId);
}
