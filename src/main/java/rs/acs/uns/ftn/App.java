package rs.acs.uns.ftn;

import java.io.File;
import java.text.ParseException;

public class App {

    /**
     * Runs this program with Gmail POP3 server
     * @throws ParseException 
     */	
    public static void main(String[] args) throws ParseException {
//        String host = "pop.gmail.com";
//        String port = "995";
//        String userName = "terza91.zl@gmail.com";
//        String password = "alinpotok123";
//        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01");
//        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-31");
        String saveDirectory = "C:\\Users\\brankoterzic\\Desktop\\testCB";
//
//        EmailReader receiver = new EmailReader();
//        receiver.setSaveDirectory(saveDirectory);
//        receiver.downloadEmailAttachments(host, port, userName, password, startDate, endDate);
//        
//        Unzipper unz = new Unzipper();
//        unz.unzipZip(saveDirectory);
//
//        ProjectCleaner cl = new ProjectCleaner();
//        cl.cleanProjects(saveDirectory);
    	
    	ProjectCleanerRecursive pcr = new ProjectCleanerRecursive();
    	pcr.extractFiles(new File(saveDirectory));
    }

}
