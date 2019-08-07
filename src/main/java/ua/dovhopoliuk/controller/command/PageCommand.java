package ua.dovhopoliuk.controller.command;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageCommand implements Command {
    private Map<String, String> pages = new HashMap<>();

    {
        pages.put("registration",
                "/registration.jsp");

        pages.put("logout",
                "redirect:/login?logout");

        pages.put("home",
                "/home.jsp");

        pages.put("login",
                "/login.jsp");
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/app/" , "");
        System.out.println(path);
        return pages.getOrDefault(path ,
                "/index.jsp");
    }
}
