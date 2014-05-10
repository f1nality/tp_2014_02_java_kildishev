package web.resourcesystem;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import web.accounts.AccountService;
import web.accounts.SignUpCode;
import web.db.AccountDAO;
import web.db.HibernateUtil;
import web.frontend.Frontend;
import web.messagesystem.MessageSystem;

/**
 * @author d.kildishev
 */
public class ResourceFactoryTest {
    ResourceFactory resourceFactory;

    @Before
    public void setUp() throws Exception {
        resourceFactory = ResourceFactory.instance("resources_test/");
    }

    @Test
    public void testConfigurationResource() throws Exception {
        Configuration configuration = (Configuration)resourceFactory.getResource("config.xml");

        Assert.assertNotNull(configuration);
        Assert.assertEquals(configuration.port, "80");
    }

    @Test
    public void testNonExistentResource() throws Exception {
        Configuration configuration = (Configuration)resourceFactory.getResource("nonexistent.xml");

        Assert.assertNull(configuration);
    }
}
