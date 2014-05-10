package web.resourcesystem;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

/**
 * @author d.kildishev
 */
public class XmlClassParser {
    public Object parse(String xml) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputSource source = new InputSource(new StringReader(xml));

            try {
                XmlClassParserHandler handler = new XmlClassParserHandler();

                parser.parse(source, handler);

                if (handler.getClassName() != null) {
                    try {
                        Class cls = Class.forName(handler.getClassName());
                        Object obj = cls.newInstance();

                        for (String fieldName : handler.getFields().keySet()) {
                            Field field = cls.getField(fieldName);

                            field.set(obj, handler.getFields().get(fieldName));
                        }

                        return obj;
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }
}


