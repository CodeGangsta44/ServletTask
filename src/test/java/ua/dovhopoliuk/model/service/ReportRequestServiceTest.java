package ua.dovhopoliuk.model.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.dovhopoliuk.model.dao.NotificationDao;
import ua.dovhopoliuk.model.dao.ReportDao;
import ua.dovhopoliuk.model.dao.ReportRequestDao;
import ua.dovhopoliuk.model.dao.implementation.TransactionalJDBCDaoFactory;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.ReportRequest;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.HashSet;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportRequestService.class)
public class ReportRequestServiceTest {

    @Test
    public void approve() throws Exception {
        UserService userServiceMock = mock(UserService.class);
        ReportRequestService reportRequestService = new ReportRequestService(userServiceMock);

        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        TransactionalJDBCDaoFactory transactionalJDBCDaoFactoryMock = PowerMockito.mock(TransactionalJDBCDaoFactory.class);
        whenNew(TransactionalJDBCDaoFactory.class).withNoArguments().thenReturn(transactionalJDBCDaoFactoryMock);

        NotificationDao notificationDaoMock = mock(NotificationDao.class);
        ReportDao reportDaoMock = mock(ReportDao.class);
        ReportRequestDao reportRequestDaoMock = mock(ReportRequestDao.class);

        when(transactionalJDBCDaoFactoryMock.createNotificationDao()).thenReturn(notificationDaoMock);
        when(transactionalJDBCDaoFactoryMock.createReportDao()).thenReturn(reportDaoMock);
        when(transactionalJDBCDaoFactoryMock.createReportRequestDao()).thenReturn(reportRequestDaoMock);

        User speaker = new User();
        speaker.setRoles(Collections.singleton(Role.MODER));

        Conference conference = new Conference();
        conference.setReports(new HashSet<>());

        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setId(1L);
        reportRequest.setTopic("Testing report request");
        reportRequest.setConference(conference);
        reportRequest.setSpeaker(speaker);

        when(userServiceMock.getCurrentUser(requestMock)).thenReturn(speaker);

        reportRequestService.approveRequest(reportRequest);

        Mockito.verify(reportDaoMock, Mockito.times(1)).create(Mockito.any());
        Mockito.verify(notificationDaoMock, Mockito.times(1)).create(Mockito.any());
        Mockito.verify(reportRequestDaoMock, Mockito.times(1)).delete(reportRequest.getId());

    }

    @Test
    public void reject() throws Exception {
        UserService userServiceMock = mock(UserService.class);
        ReportRequestService reportRequestService = new ReportRequestService(userServiceMock);

        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        TransactionalJDBCDaoFactory transactionalJDBCDaoFactoryMock = PowerMockito.mock(TransactionalJDBCDaoFactory.class);
        whenNew(TransactionalJDBCDaoFactory.class).withNoArguments().thenReturn(transactionalJDBCDaoFactoryMock);

        NotificationDao notificationDaoMock = mock(NotificationDao.class);
        ReportDao reportDaoMock = mock(ReportDao.class);
        ReportRequestDao reportRequestDaoMock = mock(ReportRequestDao.class);

        when(transactionalJDBCDaoFactoryMock.createNotificationDao()).thenReturn(notificationDaoMock);
        when(transactionalJDBCDaoFactoryMock.createReportDao()).thenReturn(reportDaoMock);
        when(transactionalJDBCDaoFactoryMock.createReportRequestDao()).thenReturn(reportRequestDaoMock);

        User speaker = new User();
        speaker.setRoles(Collections.singleton(Role.MODER));

        Conference conference = new Conference();
        conference.setReports(new HashSet<>());

        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setId(1L);
        reportRequest.setTopic("Testing report request");
        reportRequest.setConference(conference);
        reportRequest.setSpeaker(speaker);

        when(userServiceMock.getCurrentUser(requestMock)).thenReturn(speaker);

        reportRequestService.reject(requestMock, reportRequest);

        Mockito.verify(reportDaoMock, Mockito.never()).create(Mockito.any());
        Mockito.verify(notificationDaoMock, Mockito.times(1)).create(Mockito.any());
        Mockito.verify(reportRequestDaoMock, Mockito.times(1)).delete(reportRequest.getId());
    }
}