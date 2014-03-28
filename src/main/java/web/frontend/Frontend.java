package web.frontend;

import web.db.AccountService;
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
public class Frontend extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getRequestURI())
        {
            case "/signin":
                doSignInPage(request, response);
                break;
            case "/signup":
                doSignUpPage(request, response);
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
        switch (request.getRequestURI())
        {
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
        Integer userId = getUserId(request);

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
        Integer userId = getUserId(request);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        if (userId != null) {
            response.sendRedirect("/userId");
        } else {
            renderPage(response, "signup.tml", pageVariables);
        }
    }

    private void doSignOutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        session.removeAttribute("userId");
        response.sendRedirect("/signin");
    }

    private void doUserIdPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = getUserId(request);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("userId", userId);
        pageVariables.put("serverTime", new SimpleDateFormat("HH:mm:ss").format(new Date()));
        renderPage(response, "userId.tml", pageVariables);
    }

    private void doSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Integer userId = AccountService.getUserId(login, password);

        if (userId != -1) {
            HttpSession session = request.getSession();

            session.setAttribute("userId", userId);
            response.sendRedirect("/userId");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();

            pageVariables.put("error", "Wrong credentials");
            renderPage(response, "signin.tml", pageVariables);
        }
    }

    private void doSignUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password-repeat");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        if (AccountService.accountExists(login)) {
            pageVariables.put("error", "Account with such login already exists");
            renderPage(response, "signup.tml", pageVariables);
        } else if (!password.equals(passwordRepeat)) {
            pageVariables.put("error", "Passwords do not match");
            renderPage(response, "signup.tml", pageVariables);
        } else if (!AccountService.signUp(login, password)) {
            pageVariables.put("error", "Query error");
            renderPage(response, "signup.tml", pageVariables);
        } else {
            response.sendRedirect("/signin");
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

    private Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (Integer)session.getAttribute("userId");
    }
}
