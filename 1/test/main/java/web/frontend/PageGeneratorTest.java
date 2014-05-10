package web.frontend;

import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author d.kildishev
 */
public class PageGeneratorTest {
    @Test
    public void testGetPage() throws Exception {
        HashMap<String, Object> pageVariables = new HashMap<String, Object>();

        pageVariables.put("userId", "2");
        pageVariables.put("serverTime", "08:11:34");
        assertPageCorrect("userId.tml", PageGenerator.getPage("userId.tml", pageVariables));

        pageVariables.put("userId", "5");
        assertPageIncorrect("userId.tml", PageGenerator.getPage("userId.tml", pageVariables));

        pageVariables.clear();
        pageVariables.put("error", "Wrong credentials");
        pageVariables.put("serverTime", "08:03:21");
        assertPageCorrect("signin.tml", PageGenerator.getPage("signin.tml", pageVariables));
    }

    private void assertPageCorrect(String template, String actualPage) {
        try {
            String expectedPage = new String(Files.readAllBytes(Paths.get(template + ".html")));
            Assert.assertTrue(expectedPage.replace("\r\n", "\n").equals(actualPage.replace("\r\n", "\n")));
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }

    private void assertPageIncorrect(String template, String actualPage) {
        try {
            String expectedPage = new String(Files.readAllBytes(Paths.get(template + ".html")));
            Assert.assertFalse(expectedPage.replace("\r\n", "\n").equals(actualPage.replace("\r\n", "\n")));
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }
}
