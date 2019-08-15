package ua.dovhopoliuk.model.dao.mapper;

import ua.dovhopoliuk.model.entity.ReportRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ReportRequestMapper implements ObjectMapper<ReportRequest> {
    @Override
    public ReportRequest extractFromResultSet(ResultSet resultSet) throws SQLException {
        ReportRequest reportRequest = new ReportRequest();

        reportRequest.setId(resultSet.getLong("report_request_id"));
        reportRequest.setTopic(resultSet.getString("topic"));
        reportRequest.setApprovedByModerator(resultSet.getBoolean("approved_by_moderator"));
        reportRequest.setApprovedBySpeaker(resultSet.getBoolean("approved_by_speaker"));

        return reportRequest;
    }

    @Override
    public ReportRequest makeUnique(Map<Long, ReportRequest> cache, ReportRequest entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
