package ua.dovhopoliuk.controller;

import ua.dovhopoliuk.controller.command.*;
import ua.dovhopoliuk.model.exception.RequestException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@WebServlet(name = "ApiServlet")
public class ApiServlet extends HttpServlet {
    private CommandJsonUtility<String> commandJsonUtility = new CommandJsonUtility<>(String.class);
    private Map<String, Command> postCommands = new HashMap<>();
    private Map<String, Command> getCommands = new HashMap<>();

    public void init(ServletConfig servletConfig){
        postCommands.put("users",
                new RegisterUserCommand());

        getCommands.put("users/me",
                new GetCurrentUserCommand());
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
        path = path.replaceAll(".*/api/" , "");
        Command command = commands.getOrDefault(path ,
                (r)->"/index.jsp");


        String message;
        try {
            message = command.execute(request);
        } catch (RequestException e) {
            response.setStatus(500);
            message = e.getMessage();
        }

        response.setContentType("application/json");
        response.getWriter().println(commandJsonUtility.toJson(message));
    }
}
