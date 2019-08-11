package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.entity.ReportRequest;

import java.util.List;

public interface ReportRequestDao extends GenericDao<ReportRequest> {
    List<ReportRequest> findAllByApprovedByModeratorIsTrueAndSpeakerId(Long id);
}
