package org.example.Account;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    public static final UserService USER_SERVICE = new UserService();
    private final Map<String, UserProfile> usersByLogin = new HashMap<>();
    private final Map<String, UserProfile> usersBySession = new HashMap<>();

    public void addUser(UserProfile userProfile){
        usersByLogin.put(userProfile.getLogin(), userProfile);
    }

    public UserProfile getUserByLogin(String login){
        return usersByLogin.get(login);
    }

    public void addUserBySession(String session, UserProfile userProfile) {
        usersBySession.put(session, userProfile);
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return usersBySession.get(sessionId);
    }

    public void removeUserBySession(String session) {
        usersBySession.remove(session);
    }

    public UserProfile getUserByCookies(Cookie[] cookies) {
        String session;
        UserProfile user;
        if ((session = UserCookies.getValue(cookies, "JSESSIONID")) == null || (user = usersBySession.get(session)) == null) {
            return null;
        }
        return user;
    }

    public boolean containsUserByLogin(String login) {
        return usersByLogin.containsKey(login);
    }
}
