package org.example.Account;

import javax.servlet.http.Cookie;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserService {
    public static final UserService USER_SERVICE = new UserService();

    public UserProfile getUserByCookies(Cookie[] cookies) {
        UserProfile user;
        if ((user = getUser(UserCookies.getValue(cookies, "login"))) == null || !user.getPassword().equals(UserCookies.getValue(cookies, "password"))) {
            return null;
        }

        return user;
    }

    public UserProfile getUser(String login) {
        Session session = Hibernate.getSessionFactory().openSession();
        UserProfile user = session.byNaturalId(UserProfile.class).using("login", login).load();
        session.close();
        return user;
    }

    public void addUser(UserProfile user) {
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();
    }
}
