package scheduleapplication;

import java.util.function.Predicate;
import javafx.application.Application;
import javafx.stage.Stage;

/**Main class for application.
 * @version 1.1
 * @author Austin Matthews
 */
public class ScheduleApplicationMain extends Application
{
    private final static Predicate<String> debug = String::isEmpty;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        LocaleManager.setLocale();
        Splash.splashInit("Getting data from database..", 0);
        
        //log("");
        
        UserActivity.createFile();
        
        DatabaseManager.openConnection();
        
        Splash.splashUpdate("Getting Country data..", 14);
        
        CountryManager.updateMap();
        
        Splash.splashUpdate("Getting Division data..", 28);
        
        DivisionManager.updateMap();
        
        Splash.splashUpdate("Getting Customer data..", 42);
        
        CustomerManager.updateMap();
        
        Splash.splashUpdate("Getting Contact data..", 56);
        
        ContactManager.updateMap();
        
        Splash.splashUpdate("Getting User data..", 70);
        
        UserManager.updateMap();
        
        Splash.splashUpdate("Getting Appointment data..", 84);
        
        AppointmentManager.updateMap();
        
        Splash.splashUpdate("Data Collected. Opening Menu..", 98);
        //CustomerManager.customerAppointmentsByType();//.forEach(tempInt -> {System.out.println(tempInt);});
        //CustomerManager.customerAppointmentsByMonth();//.forEach(tempInt -> {System.out.println(tempInt);});
        launch(args);
    }
    
    /*Assigning a method reference to an interface
    private static void log(String message)
    {
       
        System.out.println(debug.test(message));
        //this would also work:
        //System.out.println(message);
        //and looks cleaner..
    }
    */
    
    /**
     * Used to set the Stage.
     * <p>Is called in launch in main</p>
     * @param stage stage being set
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        //gives me control over how my stage closes
        //https://stackoverflow.com/questions/17003906/prevent-cancel-closing-of-primary-stage-in-javafx-2-2
        SceneManager.setMainStage(stage);
        Splash.splashProgress(100);
        Splash.closeSplash();
        
    }
    
}
