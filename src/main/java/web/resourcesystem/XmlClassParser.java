package web.resourcesystem;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
    public Object parse(String xml, XmlClassParserHandler handler) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputSource source = new InputSource(new StringReader(xml));

            try {
                parser.parse(source, handler);

                if (handler.getClassName() != null) {
                    try {
                        Class cls = Class.forName(handler.getClassName());
                        Object obj = cls.newInstance();

                        for (String fieldName : handler.getFields().keySet()) {
                            Field field = cls.getField(fieldName);
                            String str = handler.getFields().get(fieldName);

                            if (field.getType() == int.class || field.getType() == Integer.class) {
                                field.set(obj, Integer.parseInt(str));
                            } else if (field.getType() == long.class || field.getType() == Long.class) {
                                field.set(obj, Long.parseLong(str));
                            } else if (field.getType() == short.class || field.getType() == Short.class) {
                                field.set(obj, Short.parseShort(str));
                            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                field.set(obj, Boolean.parseBoolean(str));
                            } else if (field.getType() == double.class || field.getType() == Double.class) {
                                field.set(obj, Double.parseDouble(str));
                            } else if (field.getType() == float.class || field.getType() == Float.class) {
                                field.set(obj, Float.parseFloat(str));
                            } else if (field.getType() == char.class && str.length() > 0) {
                                field.set(obj, str.charAt(0));
                            } else if (field.getType() == String.class) {
                                field.set(obj, str);
                            }
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


