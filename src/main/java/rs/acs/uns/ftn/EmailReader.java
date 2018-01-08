/*based on http://www.codejava.net/java-ee/javamail/using-javamail-for-searching-e-mail-messages*/

package rs.acs.uns.ftn;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

/**
 * @author brankoterzic
 *
 */
public class EmailReader {
	
    private String saveDirectory;

    /**
     * Sets the directory where attached files will be stored.
     * 
     * @param dir
     * absolute path of the directory
     */
    public void setSaveDirectory(String dir) {
        this.saveDirectory = dir;
    }

    /**
     * Downloads new messages and saves attachments to disk if any.
     * 
     * @param host
     * @param port
     * @param userName
     * @param password
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void downloadEmailAttachments(String host, String port,
            String userName, String password, Date startDate, Date endDate) {
    	
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        
        List<String> senderData = new ArrayList();
        List<String> loggerData = new ArrayList();
        
        try {
        	
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            // ...
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            
            SearchTerm olderThan = new ReceivedDateTerm (ComparisonTerm.GT, startDate);
            SearchTerm newerThan = new ReceivedDateTerm (ComparisonTerm.LT, endDate);
            SearchTerm andTerm = new AndTerm(olderThan, newerThan);
            
            //Message[] arrayMessages = inbox.getMessages(); <--get all messages
            Message[] arrayMessages = inbox.search(andTerm);
            
            for (int i = arrayMessages.length; i > 0; i--) { //from newer to older
            	
                Message msg = arrayMessages[i-1];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String sentDate = msg.getSentDate().toString();
                String receivedDate = msg.getReceivedDate().toString();

                String contentType = msg.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart
                                .getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part
                                .getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(saveDirectory + File.separator + fileName);
                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0,
                                attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent: " + sentDate);
                System.out.println("\t Received: " + receivedDate);
                System.out.println("\t Message: " + messageContent);
                System.out.println("\t Attachments: " + attachFiles);
                
                //logger data
                String log = "Message #" + (i + 1) + ":" + "\t From: " + from + "\t Subject: " + subject + "\t Sent: " + sentDate +
                				"\t Received: " + receivedDate + "\t Message: " + messageContent + "\t Attachments: " + attachFiles + "\n";
                loggerData.add(log);
                Files.write(Paths.get(saveDirectory + File.separator + "log.txt"), loggerData, UTF_8, APPEND, CREATE);
                loggerData.clear();
                
                //sender data
                senderData.add(subject);
                Files.write(Paths.get(saveDirectory + File.separator + "SendersData.txt"), senderData, UTF_8, APPEND, CREATE);
                senderData.clear();
            }

            // disconnect
            inbox.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}