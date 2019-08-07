package ua.dovhopoliuk.controller.filter;


import ua.dovhopoliuk.controller.config.SecurityConfig;
import ua.dovhopoliuk.model.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User)request.getSession().getAttribute("principals");
        String method = request.getMethod();
        String url = request.getServletPath();

        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }


        if (!SecurityConfig.isUrlSecured(url, method)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (user == null) {
            if (url.equals("/app/login")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            response.sendRedirect("/app/login");
            return;
        }

        if (SecurityConfig.isAccessAllowed(url, method, user.getRoles())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.sendRedirect("/access-denied.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
