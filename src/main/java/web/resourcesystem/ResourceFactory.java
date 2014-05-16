package web.resourcesystem;

import web.vfs.VFS;
import web.vfs.VFSImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author d.kildishev
 */
public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    private VFS vfs;
    private Map<String, Object> resources = new HashMap<>();

    public static ResourceFactory instance(String root) {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory(root);
        }

        return resourceFactory;
    }

    ResourceFactory(String root) {
        vfs = new VFSImpl(root);

        Iterator<String> iterator = vfs.getIterator("");

        while (iterator.hasNext()) {
            String filePath = iterator.next();

            if (filePath.endsWith(".xml")) {
                loadXmlClassResource(filePath);
            }
        }
    }

    private boolean loadXmlClassResource(String filePath) {
        if (!vfs.isExist(filePath)) {
            return false;
        }

        String str = vfs.getUFT8Text(filePath);

        if (str == null) {
            return false;
        }

        XmlClassParserHandler handler = new XmlClassParserHandler();
        Object resource = new XmlClassParser().parse(str, handler);

        if (resource == null) {
            return false;
        }

        resources.put(filePath, resource);

        return true;
    }

    public Object getResource(String filePath) {
        return resources.get(filePath);
    }
}


