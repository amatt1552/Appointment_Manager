package scheduleapplication;

import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Handles storing and getting all the Countries in the Application.
 * @author Austin Matthews
 */
public class CountryManager 
{
    private static final Map<Integer, Country> countryMap = new HashMap<>();
    
    /**
     * Updates country map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "countries", ""))
        {
            
            while(result.next())
            {
                Country country = new Country();
                country.setID(result.getInt("Country_ID"));
                country.setName(result.getString("Country"));
                countryMap.put(country.getID(), country);
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
     * Gets Country based on country's ID.
     * @param id
     * @return 
     */
    protected static Country getCountry(int id)
    {
        return countryMap.get(id);
    }
    
     /**
     * Gets all Countries as a list.
     * @return 
     */
    protected static List<Country> getCountries()
    {
        List<Country> returnedList = new ArrayList<>(countryMap.values());
        returnedList.sort(new SortByName());
        return returnedList;
    }
    
    //used to sort list by name in ascending order
    //https://stackoverflow.com/questions/11804733/java-get-string-compareto-as-a-comparator-object/32528632
    private static class SortByName implements Comparator<Country>
    {
        @Override
        public int compare(Country a, Country b)
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

            return a.getName().compareTo(b.getName());
        }
    }
}
