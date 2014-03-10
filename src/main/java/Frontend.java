import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author d.kildishev
 */
public class Frontend extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String template;
        Map<String, Object> pageVariables = new HashMap<>();

        switch (request.getRequestURI())
        {
            case "/signin": {
                    Integer userId = getUserId(request);

                    if (userId != null) {
                        response.sendRedirect("/userId");
                        return;
                    } else {
                        template = "signin.tml";
                    }
                }

                break;
            case "/signup": {
                    Integer userId = getUserId(request);

                    if (userId != null) {
                        response.sendRedirect("/userId");
                        return;
                    } else {
                        template = "signup.tml";
                    }
                }

                break;
            case "/signout":
                HttpSession session = request.getSession();

                session.removeAttribute("userId");
                response.sendRedirect("/signin");

                return;
            case "/userId":
                Integer userId = getUserId(request);

                template = "userId.tml";
                pageVariables.put("userId", userId);

                break;
            default:
                template = "httpError.tml";
                pageVariables.put("errorMsg", "404 - Not Found");
        }

        response.getWriter().println(PageGenerator.getPage(template, pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String template;
        Map<String, Object> pageVariables = new HashMap<>();

        switch (request.getRequestURI())
        {
            case "/signin": {
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    Account account = AccountDAO.getAccountByLogin(login);

                    if (account != null && account.getPassword().equals(password)) {
                        HttpSession session = request.getSession();

                        session.setAttribute("userId", account.getId());
                        response.sendRedirect("/userId");

                        return;
                    } else {
                        template = "signin.tml";
                        pageVariables.put("error", "Wrong credentials");
                    }
                }
                break;
            case "/signup": {
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    String passwordRepeat = request.getParameter("password-repeat");
                    Account account = AccountDAO.getAccountByLogin(login);

                    if (account != null) {
                        template = "signup.tml";
                        pageVariables.put("error", "Account with such login already exists");
                    } else if (!password.equals(passwordRepeat)) {
                        template = "signup.tml";
                        pageVariables.put("error", "Passwords do not match");
                    } else if (!AccountDAO.addAccount(login, password)) {
                        template = "signup.tml";
                        pageVariables.put("error", "Query error");
                    } else {
                        response.sendRedirect("/signin");

                        return;
                    }
                }
                break;
            default:
                template = "httpError.tml";
                pageVariables.put("errorMsg", "404 - Not found");
        }

        response.getWriter().println(PageGenerator.getPage(template, pageVariables));
    }

    private Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (Integer)session.getAttribute("userId");
    }
}
