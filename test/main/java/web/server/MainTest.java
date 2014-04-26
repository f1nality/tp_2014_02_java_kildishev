package web.server;

import junit.framework.Assert;
import org.eclipse.jetty.server.Server;
import org.junit.Test;

/**
 * @author d.kildishev
 */
public class MainTest {
    @Test
    public void testRunWebServer() throws Exception {
        try {
            Server server = Main.runWebServer();

            Assert.assertTrue(server.isRunning());
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
