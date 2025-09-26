package scheduleapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles storing and getting all the Contacts in the Application.
 * @author Austin Matthews
 */
public class ContactManager 
{
     private static final Map<Integer, Contact> contactMap = new HashMap<>();
     private static final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    
    /**
     * Updates contact map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "contacts", "ORDER BY Contact_ID"))
        {
            
            while(result.next())
            {
                Contact contact = new Contact();
                contact.setID(result.getInt("Contact_ID"));
                contact.setName(result.getString("Contact_Name"));
                contact.setEmail(result.getString("Email"));
                contactMap.put(contact.getID(), contact);
                contactList.add(contact);
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
     * Gets Contact using Contact's ID.
     * @param id
     * @return 
     */
    protected static Contact getContact(int id)
    {
        return contactMap.get(id);
    }
    
    /**
     * Gets all Contacts as an Observable List.
     * @return 
     */
    protected static ObservableList<Contact> getContacts()
    {
        return contactList;
    }
    
}
