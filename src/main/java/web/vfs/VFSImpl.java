package web.vfs;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VFSImpl implements VFS {
	private String root;
	
	public VFSImpl(String root){
		this.root = root;
	}

	public boolean isDirectory(String path) {
		return new File(root + path).isDirectory();
	}

	public Iterator<String> getIterator(String startDir) {		
		return new FileIterator(startDir);
	}

	private class FileIterator implements Iterator<String> {
		private Queue<File> files = new LinkedList<File>();
		
		public FileIterator(String path){
			files.add(new File(root + path));
            peek();
		}
		
		public boolean hasNext() {			
			return !files.isEmpty();
		}

        private File peek() {
            File file = files.poll();

            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    files.add(subFile);
                }
            }

            return file;
        }

		public String next() {
			File file = peek();

            return file.getPath().substring(root.length());
		}

		public void remove() {			
			files.remove();
		}
	}

	@Override
	public String getAbsolutePath(String file) {
		return new File(root + file).getAbsolutePath();
	}

	@Override
	public boolean isExist(String path) {
        return new File(root + path).exists();
	}

    private byte[] getBytes(String file, String encoding) {
        InputStreamReader inputStream = null;
        byte[] result = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(root + file);

            if (encoding != null) {
                inputStream = new InputStreamReader(fileInputStream, "UTF-8");
            } else {
                inputStream = new InputStreamReader(fileInputStream);
            }

            result = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) { }
            }
        }

        return result;
    }

	@Override
	public byte[] getBytes(String file) {
        return getBytes(file, null);
    }

	@Override
	public String getUFT8Text(String file) {
        try {
            byte[] bytes = getBytes(file, "UTF-8");

            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
	}
	
}
