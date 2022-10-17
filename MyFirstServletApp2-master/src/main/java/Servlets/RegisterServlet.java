package Servlets;

import org.example.Account.UserCookies;
import org.example.Account.UserProfile;
import org.example.Account.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile user = UserService.USER_SERVICE.getUserByCookies(req.getCookies());
        if (user != null) {
            resp.sendRedirect("/main");
            return;
        }

        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if (UserService.USER_SERVICE.containsUserByLogin(login) || login == null || password == null || email == null) {
            return;
        }

        UserProfile user = new UserProfile(login, password, email);
        UserService.USER_SERVICE.addUser(user);
        UserService.USER_SERVICE.addUserBySession(UserCookies.getValue(req.getCookies(), "JSESSIONID"), user);
        resp.sendRedirect("/main");
    }
}
