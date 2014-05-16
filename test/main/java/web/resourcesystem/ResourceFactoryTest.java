package web.resourcesystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals(configuration.port, 7777);
    }

    @Test
    public void testNonExistentResource() throws Exception {
        Configuration configuration = (Configuration)resourceFactory.getResource("nonexistent.xml");

        Assert.assertNull(configuration);
    }
}
