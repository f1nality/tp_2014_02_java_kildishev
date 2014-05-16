package web.resourcesystem;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;


/**
 * @author d.kildishev
 */
public class XmlClassParserHandlerTest {
    @Test
    public void testParse() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<class name=\"className\">\n" +
                "<fields>\n" +
                "<field name=\"field1\">value1</field>\n" +
                "<field name=\"field2\">value2</field>\n" +
                "</fields>\n" +
                "</class>";

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputSource source = new InputSource(new StringReader(xml));

            try {
                XmlClassParserHandler handler = new XmlClassParserHandler();
                parser.parse(source, handler);

                Assert.assertEquals(handler.getClassName(), "className");
                Assert.assertEquals(handler.getFields().size(), 2);
                Assert.assertEquals(handler.getFields().get("field1"), "value1");
                Assert.assertEquals(handler.getFields().get("field2"), "value2");
            } catch (IOException e) {
                e.printStackTrace();
                Assert.assertTrue(false);
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
