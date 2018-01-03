package rs.acs.uns.ftn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ProjectCleanerRecursive {
	
	private static String rootPath;
	private static File forCheck;
	private static List<File> cPath;
	private static List<File> hPath;
	
	public ProjectCleanerRecursive() {
		rootPath = "";
		forCheck = null;
		cPath = new ArrayList<File>();
		hPath = new ArrayList<File>();
		
	}


	
	public void extractFiles(File dir) {
		try {
		   	if(dir.exists()) {
	    		for(File f : dir.listFiles()) {
	    			if(f.isDirectory()) {
						rootPath = f.getCanonicalPath();
						forCheck = new File(f.getCanonicalPath() + File.separator + "checkPlagiarism");
						
						if(!forCheck.exists()) {
							forCheck.mkdir();
						}
						
						cPath.clear();
						hPath.clear();
						
	    				extractFile(f);
	    			}
	    		}
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void extractFile(File dir) {
		try {
			
			File[] files = dir.listFiles();
			
			for (File file : files) {
				if (file.isDirectory()) {
					extractFile(file);
				} else {
					if(file.getName().endsWith(".c")) {
						if(!cPath.contains(file)){
							cPath.add(file);
						}
					}
					
					if(file.getName().endsWith(".h")) {
						if(!hPath.contains(file)){
							hPath.add(file);
						}					
					}
				}
			}
			
			if(!rootPath.equals("") && forCheck != null) {
				for(File file : cPath) {
					if(!new File(forCheck.getCanonicalPath() + File.separator + file.getName()).exists()) {
						FileUtils.copyFileToDirectory(file, forCheck);
					}
				}
				
				for(File file : hPath) {
					if(!new File(forCheck.getCanonicalPath() + File.separator + file.getName()).exists()) {
						FileUtils.copyFileToDirectory(file, forCheck);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
