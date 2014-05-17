package web.server;

import org.eclipse.jetty.server.Server;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author d.kildishev
 */
public class MainTest {
    @Test
    public void testRunWebServer() throws Exception {
        try {
            Server server = Main.runWebServer(80, "static");

            Assert.assertTrue(server.isRunning());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
