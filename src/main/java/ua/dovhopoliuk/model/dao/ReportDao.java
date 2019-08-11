package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.entity.Report;

import java.util.List;

public interface ReportDao extends GenericDao<Report> {
    List<Report> findAllBySpeakerId(Long id);
    List<Report> findAllBySpeakerIdAndConferenceIsFinished(Long id);
}
