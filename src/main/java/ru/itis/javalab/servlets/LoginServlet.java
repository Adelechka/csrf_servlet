package ru.itis.javalab.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.models.Client;
import ru.itis.javalab.services.ClientService;
import ru.itis.javalab.services.UsersService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

//@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private ClientService clientService;
    private PasswordEncoder passwordEncoder;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
        this.clientService = applicationContext.getBean(ClientService.class);
        this.passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("client");
        }
        req.getRequestDispatcher(req.getContextPath() + "/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        List<Client> clients = clientService.getByData(login);

        if (clients != null && passwordEncoder.matches(password, clients.get(0).getPassword())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("client", clients.get(0));
            Client client = clients.get(0);
            Cookie cookie = new Cookie("Auth", client.getUuid());
            cookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }

    }
}
