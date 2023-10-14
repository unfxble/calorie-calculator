package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.user.AbstractUserController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private AbstractUserController controller;
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = context.getBean(ProfileRestController.class);
    }

    @Override
    public void destroy() {
        context.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (Objects.isNull(action) ? "all" : action) {
            case "all":
            default:
                log.info("UserServlet::getAll");
                request.setAttribute("users", controller.getAll());
        }

        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("userId");
        SecurityUtil.setUserId(Integer.parseInt(userId));
        response.sendRedirect("meals");
    }
}
