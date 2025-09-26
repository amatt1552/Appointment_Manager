package scheduleapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles storing and getting all the Customers in the Application.
 * @author Austin Matthews
 */
public class CustomerManager
{
    
    private static final Map<Integer, Customer> customerMap = new HashMap<>();
    private static final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    
    /**
     * Updates customer map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "customers", "ORDER BY Customer_ID"))
        {
            
            while(result.next())
            {
                Customer customer = new Customer();
                customer.setCustomerID(result.getInt("Customer_ID"));
                customer.setName(result.getString("Customer_Name"));
                customer.setAddress(result.getString("Address"));
                customer.setPostalCode(result.getString("Postal_Code"));
                customer.setPhoneNumber(result.getString("Phone"));
                customer.setDivision(result.getInt("Division_ID"));
                customerMap.put(customer.getCustomerID(), customer);
                customerList.add(customer);
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
     * Updates Customer in the database and map.
     * @param customer 
     */
    protected static void updateCustomer(Customer customer)
    {
        if(customer == null)
        {
            return;
        }
        //bool to make sure update was successful
        boolean success;

        //set customer columns
        String[] customerColumns = {
            "Customer_ID",
            "Customer_Name",
            "Address",
            "Postal_Code",
            "Phone",
            "Division_ID"};

        //set new values
        String[] customerValues = {
            Integer.toString(customer.getCustomerID()), 
            "'" + customer.getName() + "'",
            "'" + customer.getAddress() + "'",
            "'" + customer.getPostalCode() + "'",
            "'" + customer.getPhoneNumber() + "'",
            Integer.toString(customer.getDivisionID())};

        //update
        success = DatabaseManager.updateRows("customers", customerColumns, customerValues, "WHERE Customer_ID = " + customer.getCustomerID());
        
        //adds into the map if successful
        if(success)
        {
            
            customerMap.put(customer.getCustomerID(), customer);
            customerList.set(customerList.indexOf(customer), customer);
                   
        }
        DatabaseManager.closeAllStatements();
        
    }
    
    /**
     * Adds Customer into the database and map.
     * @param customer 
     */
    protected static void addCustomer(Customer customer)
    {
        if(customer == null)
        {
            return;
        }
        //bool to make sure add was successful
        boolean success;

        //set customer columns
        String[] customerColumns = {
            "Customer_ID",
            "Customer_Name",
            "Address",
            "Postal_Code",
            "Phone",
            "Division_ID"};

        //set new values
        String[] customerValues = {
            Integer.toString(customer.getCustomerID()), 
            "'" + customer.getName() + "'",
            "'" + customer.getAddress() + "'",
            "'" + customer.getPostalCode() + "'",
            "'" + customer.getPhoneNumber() + "'",
            Integer.toString(customer.getDivisionID())};

        //insert
        success = DatabaseManager.InsertRow("customers", customerColumns, customerValues);
        
        //adds into the map if successful
        if(success)
        {
            //have to get the new id first
            try(ResultSet result = DatabaseManager.getRows("Customer_ID", "customers", "ORDER BY Customer_ID"))
            {
            
                if(result.last())
                {
                    customer.setCustomerID(result.getInt(1));
                    customerMap.put(customer.getCustomerID(), customer);
                    customerList.add(customer);
                }
                result.close();
                
            }
            catch(SQLException sqlE)
            {
                
                ExceptionManager.displayError(sqlE);
            }
            
        } 
        DatabaseManager.closeAllStatements();
    }
    
    /**
     * Deletes Customer from database and map.
     * @param deletedCustomer 
     * return result
     */
    protected static boolean deleteCustomer(Customer deletedCustomer)
    {
        if(deletedCustomer == null)
        {
            return false;
        }
        boolean success = DatabaseManager.deleteRows("customers", "WHERE Customer_ID = " + deletedCustomer.getCustomerID());
        if(success)
        {
            customerMap.remove(deletedCustomer.getCustomerID());
            customerList.remove(deletedCustomer);
        }
        return success;
    }
    
    /**
     * Gets Customer using Customer's ID.
     * @param id
     * @return 
     */
    protected static Customer getCustomer(int id)
    {
        return customerMap.get(id);
    }
    
    
    /**
     * Gets Division using Customer's ID.
     * @param customerID
     * @return 
     */
    protected static Division getDivision(int customerID)
    {
        if(customerMap.containsKey(customerID))
        {
            return getCustomer(customerID).getDivision();
        }
        return null;
    }    
    
     /**
     * Gets all Customers as a list.
     * @return 
     */
    protected static ObservableList<Customer> getCustomers()
    {
        
        return customerList;
    }
    
    /**
     * Returns count with no type attached.
     * @return 
     */
    protected static List<Integer> customerAppointmentsByType()
    {
        ResultSet result = DatabaseManager.useProcedure("count_type");
        List<Integer> returnedIntList = new ArrayList<>();
        System.out.println("Count  Type");
        try
        {
            while(result.next())
            {
                returnedIntList.add(result.getInt(1));
                System.out.println(result.getInt(1) + " " + result.getString(2));
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
        return returnedIntList;
    }
    /**
     * Returns count with no month attached.
     * @return 
     */
    protected static List<Integer> customerAppointmentsByMonth()
    {
        ResultSet result = DatabaseManager.useProcedure("count_months");
        List<Integer> returnedIntList = new ArrayList<>();
        System.out.println("Count  Month");
        try
        {
            if(result.next())
            {
                returnedIntList.add(result.getInt(1));
                System.out.println(result.getInt(1) + " " + result.getString(2));
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
        return returnedIntList;
    }
    /**
     * Gets count of appointments in customer.
     * @param customer
     * @return 
     */
    protected static int customerAppointmentCount(Customer customer)
    {
        return DatabaseManager.getCount("Customer_ID", "appointments", "WHERE Customer_ID = " + customer.getCustomerID());
    }
    
}
