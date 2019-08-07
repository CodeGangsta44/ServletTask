package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.VoteDao;
import ua.dovhopoliuk.model.entity.Vote;

import java.sql.Connection;
import java.util.List;

public class JDBCVoteDao implements VoteDao {
    private final Connection connection;

    JDBCVoteDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Vote entity) {

    }

    @Override
    public Vote findById(Long id) {
        return null;
    }

    @Override
    public List<Vote> findAll() {
        return null;
    }

    @Override
    public void update(Vote entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws Exception {

    }
}
