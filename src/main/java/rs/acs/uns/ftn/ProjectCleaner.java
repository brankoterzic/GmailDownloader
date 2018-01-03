package rs.acs.uns.ftn;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class ProjectCleaner {

	@SuppressWarnings("rawtypes")
	public void cleanProjects(String path){
		Collection<File> col = FileUtils.listFilesAndDirs(new File(path), TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);
		Iterator it = col.iterator();
	    while(it.hasNext()){
	    	File f = (File) it.next();
	    	if(f.isDirectory() && !f.getPath().equals(path) && f.exists()){
	    		Collection<File> col1 = FileUtils.listFilesAndDirs(new File(path + "\\" + f.getName()), TrueFileFilter.TRUE, DirectoryFileFilter.DIRECTORY);
	    		Iterator it1 = col1.iterator();
	    		while(it1.hasNext()){
	    			File f1 = (File) it1.next();
	    			if(!f1.getPath().equals(path + "\\" + f1.getName())){
		    			String fileName = f1.getName();
		    			if(!fileName.endsWith(".c")){
		    				if(!fileName.endsWith(".h")){
		    					if(f1.isDirectory()){
		    						try {
										FileUtils.deleteDirectory(f1);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    					}else{
		    						f1.delete();
		    					}
			    			}
		    			}
	    			}
	    		}
	    	}
	    }
	}
}
