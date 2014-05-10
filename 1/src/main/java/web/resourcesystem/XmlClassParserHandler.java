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
    private final String CLASS_TAG = "class";
    private final String FIELDS_TAG = "fields";
    private final String FIELD_TAG = "field";
    private final String CLASS_NAME_ATTRIBUTE = "name";
    private Map<String, String> fields = new HashMap<>();
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

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (currentTag == CurrentTag.UNDEFINED && qName.equals(CLASS_TAG) && atts.getValue(CLASS_NAME_ATTRIBUTE) != null) {
            className = atts.getValue(CLASS_NAME_ATTRIBUTE);
            currentTag = CurrentTag.CLASS;
        } else if (currentTag == CurrentTag.CLASS && qName.equals(FIELDS_TAG)) {
            currentTag = CurrentTag.FIELDS;
        } else if (currentTag == CurrentTag.FIELDS && qName.equals(FIELD_TAG) && atts.getValue(CLASS_NAME_ATTRIBUTE) != null) {
            currentTag = CurrentTag.FIELD;
            currentFieldName = atts.getValue(CLASS_NAME_ATTRIBUTE);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (currentTag == CurrentTag.FIELD) {
            currentTag = CurrentTag.FIELDS;
        } else if (currentTag == CurrentTag.FIELDS) {
            currentTag = CurrentTag.CLASS;;
        } else if (currentTag == CurrentTag.CLASS) {
            currentTag = CurrentTag.UNDEFINED;;
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
