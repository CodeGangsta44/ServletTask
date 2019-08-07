package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.ReportRequestDao;
import ua.dovhopoliuk.model.entity.ReportRequest;

import java.sql.Connection;
import java.util.List;

public class JDBCReportRequestDao implements ReportRequestDao {
    private final Connection connection;

    JDBCReportRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(ReportRequest entity) {

    }

    @Override
    public ReportRequest findById(Long id) {
        return null;
    }

    @Override
    public List<ReportRequest> findAll() {
        return null;
    }

    @Override
    public void update(ReportRequest entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws Exception {

    }
}
