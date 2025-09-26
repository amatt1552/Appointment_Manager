package scheduleapplication;

/**
 * Base class for First Level Division.
 * @author Austin Matthews
 */
public class Division 
{
    private int divisionID;
    private String division;
    
    //foreign keys
    private Country country;
    
    /**
     * Default constructor.
     */
    protected Division()
    {
        divisionID = -1;
        division = "unknown";
        country = null;
    }
    
    /**
     * Sets Division's ID.
     * @param divID 
     */
    protected void setDivisionID(int divID)
    {
        divisionID = divID;
    }
    
    /**
     * Sets division.
     * @param division 
     */
    protected void setDivisionName(String division)
    {
        this.division = division;
    }
    
    /**
     * Sets Division's country using a country's ID.
     * @param id 
     */
    protected void setCountry(int id)
    {
        country = CountryManager.getCountry(id);
    }
    
    /**
     * Gets Division's ID.
     * @return divID 
     */
    protected int getDivisionID()
    {
        return divisionID;
    }
    
    /**
     * Gets division.
     * @return division 
     */
    protected String getDivisionName()
    {
        return division;
    }
    
    /**
     * Gets Division's country ID.
     * @return id 
     */
    protected int getCountryID()
    {
        return country.getID();
    }
    
    /**
     * Gets Division's Country.
     * @return country
     */
    protected Country getCountry()
    {
        return country;
    }
    
    @Override
    public String toString()
    {
        return division;
    }
    
}
