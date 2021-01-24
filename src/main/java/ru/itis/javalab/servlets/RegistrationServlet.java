package ru.itis.javalab.servlets;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.models.Client;
import ru.itis.javalab.services.ClientService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

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
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");

        configuration.setTemplateLoader(new FileTemplateLoader(new File("D:\\Projects\\Simple WebApp\\src\\main\\webapp\\temp")));
        Template template = configuration.getTemplate("template_for_registration.ftlh");

        Map<String, Object> attributes = new HashMap<>();
        try {
            template.process(attributes, resp.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        Client client = new Client(login, password);

        HttpSession session = req.getSession(true);
        session.setAttribute("client", client);
        Cookie cookie = new Cookie("Auth", client.getUuid());
        cookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(cookie);

        clientService.saveClient(client);

        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
