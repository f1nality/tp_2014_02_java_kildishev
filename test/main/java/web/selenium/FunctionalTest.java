package web.selenium;

import junit.framework.Assert;
import org.eclipse.jetty.server.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import web.db.AccountDAO;
import web.db.HibernateUtil;
import web.server.Main;

/**
 * @author d.kildishev
 */
public class FunctionalTest {
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = Main.runWebServer();

        SessionFactory factory = HibernateUtil.getSessionFactory("hibernate-test.cfg.xml");
        Session session = factory.openSession();

        session.beginTransaction();
        session.createSQLQuery("TRUNCATE TABLE accounts").executeUpdate();
        session.getTransaction().commit();

        AccountDAO dao = new AccountDAO();

        dao.setFactory(factory);
        dao.addAccount("test", "test");
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
