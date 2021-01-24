package ru.itis.javalab.servlets;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.context.ApplicationContext;
import ru.itis.javalab.models.Client;
import ru.itis.javalab.services.ClientService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private ClientService clientService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
        this.clientService = applicationContext.getBean(ClientService.class);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");

        configuration.setTemplateLoader(new FileTemplateLoader(new File("D:\\Projects\\Simple WebApp\\src\\main\\webapp\\temp")));
        Template template = configuration.getTemplate("template_for_home.ftlh");
        Map<String, Object> attributes = new HashMap<>();

        Client client = (Client) req.getSession().getAttribute("client");
        attributes.put("client", client);
        if (client != null) {
            attributes.put("login", client.getLogin());
            attributes.put("password", client.getPassword());
            for (Cookie cookie : req.getCookies()) {
                if (cookie != null && cookie.getName() != null && cookie.getName().equals("Auth")) {
                    attributes.put("cookie_name", cookie.getName());
                    attributes.put("cookie_value", cookie.getValue());
                }
            }
        }

        try {
            template.process(attributes, resp.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
