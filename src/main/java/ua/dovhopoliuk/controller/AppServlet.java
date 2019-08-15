package ua.dovhopoliuk.controller;

import ua.dovhopoliuk.controller.command.*;
import ua.dovhopoliuk.controller.command.conference.GetConferenceCommand;
import ua.dovhopoliuk.controller.command.conference.PostConferenceCommand;
import ua.dovhopoliuk.controller.command.conference.PutConferenceCommand;
import ua.dovhopoliuk.controller.command.notification.DeleteNotificationCommand;
import ua.dovhopoliuk.controller.command.notification.GetNotificationCommand;
import ua.dovhopoliuk.controller.command.page.PageCommand;
import ua.dovhopoliuk.controller.command.report.DeleteReportCommand;
import ua.dovhopoliuk.controller.command.report.GetReportCommand;
import ua.dovhopoliuk.controller.command.report.PutReportCommand;
import ua.dovhopoliuk.controller.command.reportRequest.GetReportRequestCommand;
import ua.dovhopoliuk.controller.command.reportRequest.PostReportRequestCommand;
import ua.dovhopoliuk.controller.command.user.DeleteUserCommand;
import ua.dovhopoliuk.controller.command.user.GetUserCommand;
import ua.dovhopoliuk.controller.command.user.PostUserCommand;
import ua.dovhopoliuk.controller.command.user.PutUserCommand;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.vote.GetVoteCommand;
import ua.dovhopoliuk.controller.command.vote.PostVoteCommand;
import ua.dovhopoliuk.model.exception.RequestException;
import ua.dovhopoliuk.model.service.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppServlet extends HttpServlet {
    private CommandJsonUtility<String> commandJsonUtility = new CommandJsonUtility<>(String.class);
    private Map<String, Command> getCommands = new HashMap<>();
    private Map<String, Command> postCommands = new HashMap<>();
    private Map<String, Command> putCommands = new HashMap<>();
    private Map<String, Command> deleteCommands = new HashMap<>();

    private UserService userService = new UserService();
    private ReportService reportService = new ReportService(userService);
    private ConferenceService conferenceService = new ConferenceService(userService);
    private ReportRequestService reportRequestService = new ReportRequestService(userService);
    private NotificationService notificationService = new NotificationService(userService);
    private VoteService voteService = new VoteService(userService);

    public void init(ServletConfig servletConfig) {
        getCommands.put("page", new PageCommand());
        getCommands.put("conferences", new GetConferenceCommand(conferenceService));
        getCommands.put("notifications", new GetNotificationCommand(notificationService));
        getCommands.put("reports", new GetReportCommand(reportService));
        getCommands.put("reportRequests", new GetReportRequestCommand(reportRequestService));
        getCommands.put("users", new GetUserCommand(userService));
        getCommands.put("votes", new GetVoteCommand(voteService));

        postCommands.put("conferences", new PostConferenceCommand(conferenceService));
        postCommands.put("reportRequests", new PostReportRequestCommand(reportRequestService));
        postCommands.put("users", new PostUserCommand(userService));
        postCommands.put("votes", new PostVoteCommand(voteService));

        putCommands.put("conferences", new PutConferenceCommand(conferenceService));
        putCommands.put("reports", new PutReportCommand(reportService));
        putCommands.put("users", new PutUserCommand(userService));

        deleteCommands.put("notifications", new DeleteNotificationCommand(notificationService));
        deleteCommands.put("reports", new DeleteReportCommand(reportService));
        deleteCommands.put("users", new DeleteUserCommand(userService));

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, postCommands);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, getCommands);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, putCommands);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, deleteCommands);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                Map<String, Command> commands) throws ServletException, IOException {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*app/" , "");

        if (path.startsWith("api/")) {
            path = path.replaceFirst(".*api/", "");
            path = path.split("/")[0];
        } else {
            path = "page";
        }

        Command command = commands.getOrDefault(path ,
                (r)->"/main.jsp");

        String message;

        try {
            message = command.execute(request);
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
            message = e.getMessage();
        }


        if (path.equals("page")) {
            processPageRequestAnswer(request, response, message);
        } else {
            processApiRequestAnswer(request, response, message);
        }

    }

    private void processPageRequestAnswer(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String answer) throws ServletException, IOException {
        if (answer.contains("redirect")){
            response.sendRedirect(answer.replace("redirect:", "/app"));
        } else {
            request.getRequestDispatcher(answer).forward(request, response);
        }
    }

    private void processApiRequestAnswer(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String answer) throws ServletException, IOException {
        if (!Objects.isNull(answer) && answer.contains("redirect")) {
            response.sendRedirect(answer.replace("redirect:", "/app"));
        } else {
            response.setContentType("application/json");
            response.getWriter().println(answer);
        }
    }
}
