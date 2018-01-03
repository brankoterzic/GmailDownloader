package rs.acs.uns.ftn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Unzipper {
	
	private final String[] extensions = {"zip","rar"};

	@SuppressWarnings("rawtypes")
	public void unzipZip(String path){
		Iterator it = FileUtils.iterateFiles(new File(path), extensions, false);
	    while(it.hasNext()){
	    	File f = (File) it.next();
	    	if(!f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("zip")){
	    		try {
					ZipFile zipFile = new ZipFile(path + "\\" + f.getName());
					
					Enumeration<?> enu = zipFile.entries();
		            while (enu.hasMoreElements()) {
		                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

		                String name = zipEntry.getName();
		                long size = zipEntry.getSize();
		                long compressedSize = zipEntry.getCompressedSize();
		                System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", 
		                        name, size, compressedSize);

		                // Do we need to create a directory ?
		                File file = new File(path + "\\" + name);
		                if (name.endsWith("/")) {
		                    file.mkdirs();
		                    continue;
		                }

		                File parent = file.getParentFile();
		                if (parent != null) {
		                    parent.mkdirs();
		                }

		                // Extract the file
		                InputStream is = zipFile.getInputStream(zipEntry);
		                FileOutputStream fos = new FileOutputStream(file);
		                byte[] bytes = new byte[1024];
		                int length;
		                while ((length = is.read(bytes)) >= 0) {
		                    fos.write(bytes, 0, length);
		                }
		                is.close();
		                fos.close();
		            }
					
		            zipFile.close();
		            
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	}
	
}
