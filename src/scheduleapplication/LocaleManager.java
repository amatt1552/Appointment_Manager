package scheduleapplication;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Deals with translations.
 * @author Austin Matthews
 */
public class LocaleManager 
{
    private static Locale locale;
    private static ResourceBundle rb;
    public static void setLocale()
    {
        locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("Translate", locale);
    }
    
    /**
     * Gets text for title for login menu based on locale.
     * @return
     */
    public static String getLoginTitle()
    {
        return rb.getString("title");
    }
    
    /**
     * Gets text for username label for login menu based on locale.
     * @return
     */
    public static String getUsername()
    {
        return rb.getString("username");
    }
    
    /**
     * Gets text for password label for login menu based on locale.
     * @return
     */
    public static String getPassword()
    {
        return rb.getString("password");
    }
    
    /**
     * Gets error message for login menu based on locale.
     * @return
     */
    public static String[] getError()
    {
        String result = rb.getString("error");
        return result.split(":");
    }
    
     /**
     * Gets cancel text for login menu based on locale.
     * @return
     */
    public static String getCancel()
    {
        return rb.getString("cancel");
    }
    
     /**
     * Gets login text for login menu based on locale.
     * @return
     */
    public static String getLogin()
    {
        return rb.getString("login");
    }
}
