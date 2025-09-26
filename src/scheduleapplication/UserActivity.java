package scheduleapplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helps record user login attempts.
 * @author Austin Matthews
 */
public class UserActivity 
{
    private static int loginAttempts;
    //private DateTime attemptDateTime;
    private static boolean success;
    private final static String FILENAME = "./login_activity.txt";
    private final static ZoneId dbZoneID = ZoneId.of("UTC");
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    
    /**
     * Tries to create a file.
     */
    protected static void createFile()
    {
        try 
        {
            File myObj = new File(FILENAME);
            if (myObj.createNewFile()) 
            {
                System.out.println("File created: " + myObj.getName());
            } 
            else 
            {
                System.out.println("File already exists.");
            }
        } 
        catch (IOException e) 
        {
            ExceptionManager.displayError(e);
        }
    }
    
    /**
     * Logs to a text file.
     * @param success
     * @param username
     */
    protected static void addAttempt(boolean success, String username)
    {
        try(FileWriter writer = new FileWriter(FILENAME, true);)
        {
            ZonedDateTime zDateTime = ZonedDateTime.now();
            LocalDateTime lDateTime = zDateTime.withZoneSameInstant(dbZoneID).toLocalDateTime();
            if(success)
            {
                writer.write("User successfully logged in with username " + username + " at " + lDateTime.format(formatter) + " " + dbZoneID.getId() + "\n");
            }
            else
            {
                writer.write("User failed to log in with username " + username + " at " + lDateTime.format(formatter) + " " + dbZoneID.getId() + "\n");
            }
            writer.flush();
            writer.close();
        }
        catch(IOException ioE)
        {
            ExceptionManager.displayError(ioE);
        }

    }
}
