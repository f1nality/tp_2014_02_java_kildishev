package web;

import junit.framework.Assert;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author d.kildishev
 */
public class FunctionalTest {
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = Main.runWebServer();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testSignInWithSelenium() throws Exception {
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:8080/signin");

        WebElement element = driver.findElement(By.name("login"));
        driver.findElement(By.name("password")).sendKeys("test");

        element.sendKeys("test");
        element.submit();

        Assert.assertEquals(driver.getTitle(), "Java - userId");
    }
}
