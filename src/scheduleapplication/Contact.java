package scheduleapplication;

/**
 * Base class for Contacts.
 * @author Austin Matthews
 */
public class Contact 
{
    private int contactID;
    private String contactName;
    private String contactEmail;
    
    /**
     * Default constructor.
     */
    protected Contact()
    {
        contactID = -1;
        contactName = "undefined";
        contactEmail = "undefined";
    }
    
    /**
     * Sets Contact's ID.
     * @param id
     */
    protected void setID(int id)
    {
        contactID = id;
    }
    
    
    /**
     * Sets Contact's name.
     * @param name 
     */
    protected void setName(String name)
    {
        contactName = name;
    }
    
    /**
     * Sets Contact's email.
     * @param email 
     */
    protected void setEmail(String email)
    {
        contactEmail = email;
    }
    
    /**
     * Gets Contact's ID
     * @return contact ID 
     */
    protected int getID()
    {
        return contactID;
    }
    
    /**
     * Gets Contact's name.
     * @return contact name 
     */
    protected String getName()
    {
        return contactName;
    }
    
    /**
     * Gets Contact's email.
     * @return email 
     */
    protected String getEmail()
    {
        return contactEmail;
    }

    @Override
    public String toString() 
    {
        return contactName + " ID: " + contactID;
    }
    
    
}
