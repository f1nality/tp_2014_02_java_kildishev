package web.resourcesystem;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * @author d.kildishev
 */
public class XmlClassParserTest {
    @Test
    public void testParse() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><class></class>";

        Map<String, String> fields = new HashMap<>();

        fields.put("integerVar", "1337");
        fields.put("intVar", "1");
        fields.put("longVar", "2");
        fields.put("shortVar", "3");
        fields.put("booleanVar", "True");
        fields.put("doubleVar", "12.2");
        fields.put("floatVar", "12.3");
        fields.put("charVar", "a");
        fields.put("stringVar", "String");

        XmlClassParserHandler handler = mock(XmlClassParserHandler.class);

        when(handler.getClassName()).thenReturn("web.resourcesystem.XmlClassParserTest$TestClass");
        when(handler.getFields()).thenReturn(fields);

        Object object = new XmlClassParser().parse(xml, handler);

        Assert.assertNotNull(object);

        TestClass testObject = (TestClass)object;

        Assert.assertEquals(testObject.integerVar, new Integer(1337));
        Assert.assertEquals(testObject.intVar, 1);
        Assert.assertEquals(testObject.longVar, new Long(2L));
        Assert.assertEquals(testObject.shortVar, new Short((short) 3));
        Assert.assertEquals(testObject.booleanVar, true);
        Assert.assertEquals(testObject.doubleVar, new Double(12.2));
        Assert.assertEquals(testObject.floatVar, new Float(12.3));
        Assert.assertEquals(testObject.charVar, 'a');
        Assert.assertEquals(testObject.stringVar, "String");
    }

    public static class TestClass {
        public Integer integerVar;
        public int intVar;
        public Long longVar;
        public Short shortVar;
        public Boolean booleanVar;
        public Double doubleVar;
        public Float floatVar;
        public char charVar;
        public String stringVar;
    }
}


