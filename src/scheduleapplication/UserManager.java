
package scheduleapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles storing and getting all the Users in the Application.
 * @author Austin Matthews
 */
public class UserManager 
{
    private static final Map<Integer, User> userMap = new HashMap<>();
    private static final ObservableList<User> userList = FXCollections.observableArrayList();
    private static User currentUser;
    
    /**
     * Updates user map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "users", "ORDER BY User_ID"))
        {
            
            while(result.next())
            {
                User user = new User();
                user.setID(result.getInt("User_ID"));
                user.setUsername(result.getString("User_Name"));
                user.setPassword(result.getString("Password"));
                userMap.put(user.getID(), user);
                userList.add(user);
            }
            result.close();
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        finally
        {
            DatabaseManager.closeAllStatements();
        }
    }
    
    /**
     * Gets User using User's ID.
     * @param id
     * @return 
     */
    protected static User getUser(int id)
    {
        return userMap.get(id);
    }
    
    /**
     * Returns a User if username and password exist in User Map.
     * @param name username
     * @param pass password
     * @return a user
     */
    protected static User getAuthenticatedUser(String name, String pass)
    {
        for(Map.Entry<Integer, User> user : userMap.entrySet())
        {
            if(user.getValue().Authenticate(name, pass))
            {
                UserActivity.addAttempt(true, name);
                return user.getValue();
            }
        }
        UserActivity.addAttempt(false, name);
        return null;
    }
    
    /**
     * Gets Users as an ObservableList.
     * @return 
     */
    protected static ObservableList<User> getUsers()
    {
        return userList;
    }
    
    /**
     * Sets the current user
     * @param user
     */
    protected static void setCurrentUser(User user)
    {
        currentUser = user;
    }
    
    /**
     * gets the current user id
     * @return 
     */
    protected static int getCurrentUserID()
    {
        if(currentUser != null)
        {
            return currentUser.getID();
        }
        return -1;
    }
}

