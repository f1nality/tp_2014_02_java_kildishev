package web.resourcesystem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author d.kildishev
 */
public class XmlClassParserHandler extends DefaultHandler {
    private CurrentTag currentTag = CurrentTag.UNDEFINED;
    private String currentFieldName = null;
    private static final String CLASS_TAG = "class";
    private static final String FIELDS_TAG = "fields";
    private static final String FIELD_TAG = "field";
    private static final String CLASS_NAME_ATTRIBUTE = "name";
    private static final String FIELD_NAME_ATTRIBUTE = "name";
    private final Map<String, String> fields = new HashMap<>();
    private String className = null;
    private enum CurrentTag {
        UNDEFINED,
        CLASS,
        FIELDS,
        FIELD
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getFields() {
        return fields;
    }
//todo:add test
    @Override
    public void startElement(String namespaceURI, String localName, String tag, Attributes attributes) throws SAXException {
        if (isValidClassTag(tag, attributes)) {
            className = attributes.getValue(CLASS_NAME_ATTRIBUTE);
            currentTag = CurrentTag.CLASS;
        } else if (isValidFieldsTag(tag)) {
            currentTag = CurrentTag.FIELDS;
        } else if (isValidFieldTag(tag, attributes)) {
            currentTag = CurrentTag.FIELD;
            currentFieldName = attributes.getValue(FIELD_NAME_ATTRIBUTE);
        }
    }

    private boolean isValidClassTag(String tag, Attributes attributes) {
        return currentTag == CurrentTag.UNDEFINED && tag.equals(CLASS_TAG) && attributes.getValue(CLASS_NAME_ATTRIBUTE) != null;
    }

    private boolean isValidFieldsTag(String tag) {
        return currentTag == CurrentTag.CLASS && tag.equals(FIELDS_TAG);
    }

    private boolean isValidFieldTag(String tag, Attributes attributes) {
        return currentTag == CurrentTag.FIELDS && tag.equals(FIELD_TAG) && attributes.getValue(FIELD_NAME_ATTRIBUTE) != null;
    }

    @Override
    public void endElement(String namespaceURI, String localName, String tag) throws SAXException {
        switch (currentTag) {
            case FIELD:
                currentTag = CurrentTag.FIELDS;
                break;
            case FIELDS:
                currentTag = CurrentTag.CLASS;
                break;
            case CLASS:
                currentTag = CurrentTag.UNDEFINED;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentTag == CurrentTag.FIELD) {
            String contents = new String(ch, start, length);

            fields.put(currentFieldName, contents);
        }
    }
}
