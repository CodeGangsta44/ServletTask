package ua.dovhopoliuk.model.dao.mapper;

import ua.dovhopoliuk.model.entity.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class VoteMapper implements ObjectMapper<Vote> {
    @Override
    public Vote extractFromResultSet(ResultSet resultSet) throws SQLException {
        Vote vote = new Vote();

        vote.setId(resultSet.getLong("vote_id"));
        vote.setMark(resultSet.getInt("mark"));

        return vote;
    }

    @Override
    public Vote makeUnique(Map<Long, Vote> cache, Vote entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
