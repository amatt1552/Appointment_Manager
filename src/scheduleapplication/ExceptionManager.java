package scheduleapplication;

/**
 * Helps deal with Exceptions.
 * @author Austin Matthews
 */
public class ExceptionManager 
{

    /**
     * Displays an exception message and a stack trace.
     * @param e
     */
    protected static void displayError(Exception e) 
    {
        System.out.println("ERROR: " + e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements) 
        {
            System.out.println("\t".concat(element.toString()));
        }
    }
    
}
