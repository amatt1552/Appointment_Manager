package scheduleapplication;

/**
 * Base class for Users.
 * @author Austin Matthews
 */
public class User 
{
    private int userID;
    private String username;
    private String password;
    
    /**
     * Default Constructor.
     */
    protected User()
    {
        userID = -1;
        username = "undefined";
        password = "undefined";
    }
    
    /**
     * Set User's id.
     * @param id 
     */
    protected void setID(int id)
    {
        userID = id;
    }
    
    /**
     * Sets User's username.
     * @param username 
     */
    protected void setUsername(String username)
    {
        this.username = username;
    }
    
    /**
     * Sets User's password.
     * @param password 
     */
    protected void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Authenticates the username and password.
     * @param name tested username
     * @param pass tested password
     * @return 
     */
    protected boolean Authenticate(String name, String pass)
    {
        //System.out.println(name);
        //System.out.println(pass);
        return username.equals(name) && password.equals(pass);
    }
    
    /**
     * Gets User's id.
     * @return 
     */
    protected int getID()
    {
        return userID;
    }
    
    /**
     * Gets username.
     * @return username 
     */
    protected String getUsername()
    {
        return username;
    }

    @Override
    public String toString() 
    {
        if(UserManager.getCurrentUserID() == userID)
        {
            return Integer.toString(userID) + " (Current)";
        }
        return Integer.toString(userID);
    }
    
}
