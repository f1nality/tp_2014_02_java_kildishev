package web.frontend;

import web.messagesystem.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author d.kildishev
 */
public class Frontend extends HttpServlet implements Abonent, Runnable {
    private MessageSystem ms;
    private Address address = new Address();
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private int handleCount = 0;
    private Object lock = new Object();

    public Frontend(MessageSystem ms) {
        this.ms = ms;

        ms.addService(this);
        ms.getAddressService().setFrontendService(getAddress());
    }

    public Address getAddress() {
        return address;
    }

    public void setId(String sessionId, Long userId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);

        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }

        if (userId == -1) {
            sessionIdToUserSession.remove(sessionId);
            return;
        }

        userSession.setUserId(userId);
    }

    public void signUpUser(String sessionId, int code) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);

        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }

        userSession.setRegistrationCode(code);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                System.out.println("handleCount=" + handleCount);
            }

            try {
                ms.execForAbonent(this);
                Thread.sleep(5000);
            } catch (InterruptedException e) { }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getRequestURI()) {
            case "/signin":
                doSignInPage(request, response);
                break;
            case "/signup":
                doSignUpPage(request, response);
                break;
            case "/signupStatus":
                doSignUpStatusPage(request, response);
                break;
            case "/signout":
                doSignOutPage(request, response);
                break;
            case "/userId":
                doUserIdPage(request, response);
                break;
            default:
                doNotFoundError(response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getRequestURI()) {
            case "/signin":
                doSignIn(request, response);
                break;
            case "/signup":
                doSignUp(request, response);
                break;
            default:
                doNotFoundError(response);
        }
    }

    private void doSignInPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = getUserId(request);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        if (userId != null) {
            response.sendRedirect("/userId");
        } else {
            renderPage(response, "signin.tml", pageVariables);
        }
    }

    private void doSignUpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (userSession != null) {
            if (userSession.getUserId() != null) {
                response.sendRedirect("/userId");
                return;
            }

            if (userSession.isRegistering()) {
                switch (userSession.getRegistrationCode()) {
                    case 0:
                        response.sendRedirect("/signin");
                        return;
                    case 1:
                        pageVariables.put("error", "Account with such login already exists");
                        sessionIdToUserSession.remove(session.getId());
                        break;
                    case 2:
                        pageVariables.put("error", "Server Maintenance");
                        sessionIdToUserSession.remove(session.getId());
                        break;
                }

                userSession.setRegistering(false);
            }
        }

        renderPage(response, "signup.tml", pageVariables);
    }

    private void doSignUpStatusPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (userSession == null) {
            response.sendRedirect("/signup");
            return;
        }

        if (userSession.isRegistering() && userSession.getRegistrationCode() == -1) {
            pageVariables.put("status", "waiting for registration...");
            pageVariables.put("serverTime", new SimpleDateFormat("HH:mm:ss").format(new Date()));

            renderPage(response, "signupStatus.tml", pageVariables);
        } else {
            response.sendRedirect("/signup");
        }
    }

    private void doSignOutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (sessionIdToUserSession.containsKey(session.getId())) {
            sessionIdToUserSession.remove(session.getId());
        }

        response.sendRedirect("/signin");
    }

    private void doUserIdPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (userSession != null) {
            if (userSession.getUserId() == null) {
                pageVariables.put("userId", "waiting for authorization...");
            } else if (userSession.getUserId() == -2) {
                pageVariables.put("userId", "Server Maintenance");
                sessionIdToUserSession.remove(session.getId());
            } else  {
                pageVariables.put("userId", userSession.getUserId());
            }
        }

        pageVariables.put("serverTime", new SimpleDateFormat("HH:mm:ss").format(new Date()));

        renderPage(response, "userId.tml", pageVariables);
    }

    private void doSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());

        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));

        synchronized (lock) {
            ++handleCount;
        }

        response.sendRedirect("/userId");
    }

    private void doSignUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password-repeat");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        if (!password.equals(passwordRepeat)) {
            pageVariables.put("error", "Passwords do not match");
            renderPage(response, "signup.tml", pageVariables);
        } else {
            String sessionId = request.getSession().getId();
            UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());

            userSession.setRegistering(true);
            userSession.setRegistrationCode(-1);
            sessionIdToUserSession.put(sessionId, userSession);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAccountService();

                ms.sendMessage(new MsgSignUpUser(frontendAddress, accountServiceAddress, login, password, sessionId));

            synchronized (lock) {
                ++handleCount;
            }

            response.sendRedirect("/signupStatus");
        }
    }

    private void doNotFoundError(HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("errorMsg", "404 - Not found");
        renderPage(response, "httpError.tml", pageVariables);
    }

    private void renderPage(HttpServletResponse response, String template, Map<String, Object> pageVariables) throws ServletException, IOException {
        response.getWriter().println(PageGenerator.getPage(template, pageVariables));
    }

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (userSession == null) {
            return null;
        }

        return userSession.getUserId();
    }
}
