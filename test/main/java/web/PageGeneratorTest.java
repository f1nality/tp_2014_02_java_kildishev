package web;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author d.kildishev
 */
public class PageGeneratorTest {
    @Test
    public void testGetPage() throws Exception {
        HashMap<String, Object> pageVariables = new HashMap<String, Object>();

        pageVariables.put("userId", "1");
        testPageCorrect(PageGenerator.getPage("userId.tml", pageVariables));

        pageVariables.clear();
        pageVariables.put("error", "Wrong credentials");
        testPageCorrect(PageGenerator.getPage("signin.tml", pageVariables));
    }

    private void testPageCorrect(String page) {
        Assert.assertNotNull(page);
        Assert.assertNotSame(page, "");
    }
}
