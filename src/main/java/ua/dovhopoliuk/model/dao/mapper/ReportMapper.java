package ua.dovhopoliuk.model.dao.mapper;

import ua.dovhopoliuk.model.entity.Report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ReportMapper implements ObjectMapper<Report> {
    @Override
    public Report extractFromResultSet(ResultSet resultSet) throws SQLException {
        Report report = new Report();

        report.setId(resultSet.getLong("report_id"));
        report.setTopic(resultSet.getString("report_topic"));

        return report;
    }

    @Override
    public Report makeUnique(Map<Long, Report> cache, Report entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }
}
