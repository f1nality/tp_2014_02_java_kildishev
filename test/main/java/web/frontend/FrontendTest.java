package web.frontend;

import junit.framework.Assert;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import web.db.AccountService;
import web.frontend.Frontend;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

/**
 * @author d.kildishev
 */
public class FrontendTest {
    private Frontend frontend = new Frontend();
    private String notFoundPage = null;

    @Before
    public void setUp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        notFoundPage = sendRequest(request, response, "/nonexistantpage", HttpMethod.GET);
    }

    @Test
    public void testDoGet() throws Exception {
        testRouting();
    }

    @Test
    public void testDoPost() throws Exception {
        testAuthorizationSuccess();
        testAuthorizationFail();
        testRegistrationSuccess();
        testRegistrationFail();
    }

    private void testRouting() throws Exception {
        Assert.assertTrue(routeExists("/userId"));
        Assert.assertTrue(routeExists("/signin"));
        Assert.assertTrue(routeExists("/signup"));
    }

    private boolean routeExists(String url) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute("userId")).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        String output = sendRequest(request, response, url, HttpMethod.GET);

        return output != null && !output.equals(notFoundPage);
    }

    private void testAuthorizationSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");

        sendRequest(request, response, "/signin", HttpMethod.POST);
        verify(response).sendRedirect(anyString());
    }

    private void testAuthorizationFail() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("wrong_password");

        sendRequest(request, response, "/signin", HttpMethod.POST);
        verify(response, never()).sendRedirect(anyString());
    }

    private void testRegistrationSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test2");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("password-repeat")).thenReturn("test");

        sendRequest(request, response, "/signup", HttpMethod.POST);
        verify(response).sendRedirect("/signin");

        AccountService.removeAccount("test2");
    }

    private void testRegistrationFail() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("password-repeat")).thenReturn("test");

        sendRequest(request, response, "/signup", HttpMethod.POST);
        verify(response, never()).sendRedirect(anyString());
    }

    private String sendRequest(HttpServletRequest request, HttpServletResponse response, String url, HttpMethod method) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
            when(request.getRequestURI()).thenReturn(url);

            if (method == HttpMethod.GET) frontend.doGet(request, response);
            else if (method == HttpMethod.POST) frontend.doPost(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }

        return stringWriter.toString();
    }
}
