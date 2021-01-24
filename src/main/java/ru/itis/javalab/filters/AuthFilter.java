package ru.itis.javalab.filters;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.models.Client;
import ru.itis.javalab.services.ClientService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


//@WebFilter("/login")
public class AuthFilter implements Filter {

    private ClientService clientService;
    private PasswordEncoder passwordEncoder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        this.clientService = (ClientService) servletContext.getAttribute("clientsService");
        this.passwordEncoder = (PasswordEncoder) servletContext.getAttribute("passwordEncoder");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        for (Cookie cookie : request.getCookies()) {
            if (cookie != null && cookie.getName() != null && cookie.getName().equals("Auth")) {
                if (clientService.containsUuid(cookie.getValue())) {
                    String uuid = cookie.getValue();
                    List<Client> client = clientService.getByUuid(uuid);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("client", client.get(0));
                    request.getRequestDispatcher(request.getContextPath() + "/home").forward(request, response);
 //                   response.sendRedirect(request.getContextPath() + "/home");
                } else {
 //                   response.sendRedirect(request.getContextPath() + "/login");
                    request.getRequestDispatcher(request.getContextPath() + "/login").forward(request, response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
