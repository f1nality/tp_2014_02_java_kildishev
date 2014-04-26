package web.frontend;

import junit.framework.Assert;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import web.messagesystem.MessageSystem;

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
    private MessageSystem ms = new MessageSystem();
    private Frontend frontend = new Frontend(ms);
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

    private void testRouting() throws Exception {
        Assert.assertTrue(routeExists("/userId"));
        Assert.assertTrue(routeExists("/signin"));
        Assert.assertTrue(routeExists("/signup"));
        Assert.assertFalse(routeExists("/nonexistantpage"));
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
