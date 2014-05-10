package web.frontend;

import web.vfs.VFS;
import web.vfs.VFSImpl;

import java.util.Iterator;

public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    private VFS vfs;

    public static ResourceFactory instance(){
        if (resourceFactory == null){
            resourceFactory = new ResourceFactory();
        }

        return resourceFactory;
    }

    ResourceFactory() {
        vfs = new VFSImpl("resources/");

        Iterator<String> iterator = vfs.getIterator("");

        while (iterator.hasNext()) {
            String file = iterator.next();
            System.out.println(file);
        }

        byte[] bytes = vfs.getBytes("strings.xml");

        System.out.println(bytes);
    }
}


