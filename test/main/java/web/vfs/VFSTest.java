package web.vfs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author d.kildishev
 */
public class VFSTest {
    VFS vfs;

    @Before
    public void setUp() throws Exception {
        vfs = new VFSImpl("resources_test/");
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<String> iterator = vfs.getIterator("");
        List<String> expectedContents = new ArrayList<>();
        int i = 0;

        expectedContents.add("config.xml");
        expectedContents.add("directory");
        expectedContents.add("directory" + File.separator + "dummy.txt");

        Assert.assertNotNull(iterator);

        while (iterator.hasNext()) {
            String object = iterator.next();

            Assert.assertEquals(object, expectedContents.get(i++));
        }

        Assert.assertEquals(i, expectedContents.size());
    }

    @Test
    public void testIsExist() throws Exception {
        Assert.assertTrue(vfs.isExist("config.xml"));
        Assert.assertFalse(vfs.isExist("nonexistent.xml"));
    }

    @Test
    public void testIsDirectory() throws Exception {
        Assert.assertTrue(vfs.isDirectory("directory"));
        Assert.assertFalse(vfs.isDirectory("nonexistent.xml"));
    }

    @Test
    public void testGetUFT8Text() throws Exception {
        Assert.assertEquals("dummy", vfs.getUFT8Text("directory" + File.separator + "dummy.txt"));
    }
}
