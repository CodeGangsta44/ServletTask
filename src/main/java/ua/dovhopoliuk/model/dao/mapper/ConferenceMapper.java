package ua.dovhopoliuk.model.dao.mapper;

import ua.dovhopoliuk.model.entity.Conference;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

public class ConferenceMapper implements ObjectMapper<Conference> {
    @Override
    public Conference extractFromResultSet(ResultSet resultSet) throws SQLException {

        Conference conference = new Conference();

        conference.setId(resultSet.getLong("conference_id"));
        conference.setTopic(resultSet.getString("conference_topic"));
        conference.setEventDateTime(resultSet.getTimestamp("event_date_time").toLocalDateTime());
        conference.setEventAddress(resultSet.getString("event_address"));
        conference.setDescription(resultSet.getString("description"));
        conference.setApproved(resultSet.getBoolean("approved"));
        conference.setFinished(resultSet.getBoolean("finished"));
        conference.setNumberOfVisitedGuests(resultSet.getLong("number_of_visited_guests"));

        return conference;
    }

    @Override
    public Conference makeUnique(Map<Long, Conference> cache, Conference entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
