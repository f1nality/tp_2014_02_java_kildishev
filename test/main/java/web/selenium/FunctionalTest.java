package web.selenium;

import org.eclipse.jetty.server.ServerConnector;
import org.junit.Assert;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import web.server.Main;

/**
 * @author d.kildishev
 */
public class FunctionalTest {
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = Main.runWebServer();

        Assert.assertNotNull(server);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testSignInWithSelenium() throws Exception {
        WebDriver driver = new HtmlUnitDriver();
        int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();

        driver.get("http://localhost:" + port + "/signin");

        WebElement element = driver.findElement(By.name("login"));
        driver.findElement(By.name("password")).sendKeys("test");

        element.sendKeys("test");
        element.submit();

        String userId = null;

        while (userId == null || userId.equals("waiting for authorization...")) {
            WebElement userIdElement = driver.findElement(By.id("userId"));

            if (userIdElement == null) {
                break;
            }

            userId = userIdElement.getText();

            Thread.sleep(1000);
            driver.navigate().refresh();
        }

        try {
            int parsedUserId = Integer.parseInt(userId);
            Assert.assertTrue(parsedUserId > 0);
        } catch (NumberFormatException e) {
            Assert.assertTrue(false);
        }
    }
}
