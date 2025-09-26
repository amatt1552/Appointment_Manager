package scheduleapplication;

/**
 * Base class for Countries.
 * @author Austin Matthews
 */
public class Country 
{
    private int countryID;
    private String countryName;
    
    /**
     * Default Constructor.
     */
    protected Country()
    {
        countryID = -1;
        countryName = "undefined";
    }
    
    /**
     * Sets Country's id.
     * @param id 
     */
    protected void setID(int id)
    {
        countryID = id;
    }
    
    /**
     * Sets Country's name.
     * @param name 
     */
    protected void setName(String name)
    {
        countryName = name;
    }
    
    /**
     * Gets Country's id.
     * @return country id 
     */
    protected int getID()
    {
        return countryID;
    }
    
    /**
     * Gets Country's name. 
     * @return country name 
     */
    protected String getName()
    {
        return countryName;
    }
    
    @Override
    public String toString()
    {
        return countryName;
    }
}
