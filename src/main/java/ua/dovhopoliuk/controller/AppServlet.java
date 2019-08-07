package ua.dovhopoliuk.controller;

import ua.dovhopoliuk.controller.command.*;
import ua.dovhopoliuk.model.exception.RequestException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppServlet extends HttpServlet {
    private CommandJsonUtility<String> commandJsonUtility = new CommandJsonUtility<>(String.class);
    private Map<String, Command> getCommands = new HashMap<>();
    private Map<String, Command> postCommands = new HashMap<>();

    public void init(ServletConfig servletConfig){
        getCommands.put("page",
                new PageCommand());

        postCommands.put("users",
                new UsersPostCommand());
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, postCommands);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, getCommands);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                Map<String, Command> commands) throws ServletException, IOException {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/app/" , "");

        if (path.startsWith("api/")) {
            path = path.replaceFirst(".*api/", "");
            path = path.split("/")[0];
        } else {
            path = "page";
        }

        Command command = commands.getOrDefault(path ,
                (r)->"/index.jsp");

        String message;

        try {
            message = command.execute(request);
        } catch (RequestException e) {
            response.setStatus(500);
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
        if (answer.contains("redirect")) {
            response.sendRedirect(answer.replace("redirect:", "/app"));
        } else {
            response.setContentType("application/json");
            response.getWriter().println(commandJsonUtility.toJson(answer));
        }
    }
}
