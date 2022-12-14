package org.example.Servlets;

import org.example.Account.UserCookies;
import org.example.Account.UserProfile;
import org.example.Account.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile user = UserService.USER_SERVICE.getUserByCookies(req.getCookies());
        if (user != null) {
            resp.sendRedirect("./");
            return;
        }

        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login == null || password == null) {
            return;
        }

        UserProfile user = UserService.USER_SERVICE.getUser(login);
        if (user == null || !user.getPassword().equals(password)) {
            resp.sendRedirect("./auth");
            return;
        }
        UserCookies.addCookie(resp, "login", login);
        UserCookies.addCookie(resp, "password", password);
        resp.sendRedirect("./");
    }
}