package scheduleapplication;

import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Handles storing and getting all the First Level Divisions in the Application.
 * @author Austin Matthews
 */
public class DivisionManager 
{
    
    private static final Map<Integer, Division > divisionMap = new HashMap<>();
    
    /**
     * Updates division map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "first_level_divisions", ""))
        {
            
            while(result.next())
            {
                Division division = new Division();
                division.setDivisionID(result.getInt("Division_ID"));
                division.setDivisionName(result.getString("Division"));
                division.setCountry(result.getInt("COUNTRY_ID"));
                divisionMap.put(division.getDivisionID(), division);
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
     * Gets Division from map using division's ID.
     * @param id
     * @return 
     */
    protected static Division getDivision(int id)
    {
        
        return divisionMap.get(id);
    }
    /**
     * Gets Division from map using division.
     * @deprecated 
     * <p>This Method makes no sense to use. If you have the division already, it makes no sense to get it again.</p>
     * <p>I would have just deleted it, but I wanted to try @depreciated :P</p>
     * @param division
     * @return 
     */
    @Deprecated
    protected static Division getDivision(Division division)
    {
        
        return divisionMap.get(division.getDivisionID());
    }
    
    /**
     * Gets Country using division.
     * @param division
     * @return 
     */
    protected static Country getCountry(Division division)
    {
        if(divisionMap.containsKey(division.getDivisionID()))
        {
            return division.getCountry();
        }
        return null;
    }    
    
    /**
     * Gets Country using Division's ID.
     * @param divisionID
     * @return 
     */
    protected static Country getCountry(int divisionID)
    {
        if(divisionMap.containsKey(divisionID))
        {
            return getDivision(divisionID).getCountry();
        }
        return null;
    }    
    
    /**
     * Gets all divisions as a list with given CountryID.
     * @param countryID
     * @return 
     */
    
    protected static List<Division> getDivisions(int countryID)
    {
        List<Division> returnedList= new ArrayList<>();
        for(Map.Entry<Integer, Division> division : divisionMap.entrySet())
        {
            if(division.getValue().getCountryID() == countryID)
            {
                returnedList.add(division.getValue());
            }
        }
        returnedList.sort(new SortByName());
        return returnedList;
    }
    
    //used to sort list by name in ascending order
    //https://stackoverflow.com/questions/11804733/java-get-string-compareto-as-a-comparator-object/32528632
    private static class SortByName implements Comparator<Division>
    {
        @Override
        public int compare(Division a, Division b)
        {
            if (a == b) 
            {
                return 0;
            }
            if (a == null) 
            {
                return -1;
            }
            if (b == null) 
            {
                return 1;
            }

            return a.getDivisionName().compareTo(b.getDivisionName());
        }
    }
}


