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
    public Conference extractFromResultSet(ResultSet rs) throws SQLException {

        Conference conference = new Conference();

        conference.setId(rs.getLong("conference_id"));
        conference.setTopic(rs.getString("conference_topic"));
        conference.setEventDateTime(rs.getTimestamp("event_date_time").toLocalDateTime());
        conference.setEventAddress(rs.getString("event_address"));
        conference.setDescription(rs.getString("description"));
        conference.setApproved(rs.getBoolean("approved"));
        conference.setFinished(rs.getBoolean("finished"));
        conference.setNumberOfVisitedGuests(rs.getLong("number_of_visited_guests"));

        return conference;
    }

    @Override
    public Conference makeUnique(Map<Long, Conference> cache, Conference entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
