package rs.acs.uns.ftn;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import rs.acs.uns.ftn.ProjectCleanerRecursive;

public class App {

    /**
     * Runs this program with Gmail POP3 server
     * @throws ParseException 
     */	
    public static void main(String[] args) throws ParseException {
        String host = "pop.gmail.com";
        String port = "995";
        String userName = "baze1.projekti@gmail.com";
        String password = "";
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-31");
        String saveDirectory = "C:" + File.separator + "temp"; // add a directory path of directory where mail attachments should be saved
        String checkPlagiarism = saveDirectory + File.separator + "checkPlagiarism"; // checkPlagiarism directory path

        EmailReader receiver = new EmailReader();
        receiver.setSaveDirectory(saveDirectory);
        receiver.downloadEmailAttachments(host, port, userName, password, startDate, endDate);
        
        Unzipper unz = new Unzipper();
        unz.unzipZip(saveDirectory);
    	
        if(makedir(new File(checkPlagiarism))) {
        	ProjectCleanerRecursive pcr = new ProjectCleanerRecursive(checkPlagiarism);
        	pcr.extractFiles(new File(saveDirectory));
        }
    }
    
    private static boolean makedir(File dir) {
		if(!dir.exists()) {
			return dir.mkdir();
		}
		
		return true;
    }

}
