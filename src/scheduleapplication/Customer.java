package scheduleapplication;


/**
 * Base class for Customers.
 * @author Austin Matthews
 */
public class Customer 
{
    private int customerID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
   
    //foreign keys
    private Division division;
    
    /**
     * Default Constructor.
     */
    protected Customer()
    {
        customerID = -1;
        name = "";
        address = "";
        postalCode = "";
        phoneNumber = "";
        division = null;
    }
    /*
    public Customer(int customerID, String name, String address, String postalCode, String phoneNumber, int divisionID)
    {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
    }
    */
    /**
     * Sets Customer's ID.
     * @param id 
     */
    protected void setCustomerID(int id)
    {
        customerID = id;
    }
    
    /**
     * Sets Customer's ID using a string.
     * @param id 
     * @return returns error message if not a number, returns "" if a number
     */
    protected String setCustomerID(String id)
    {
        try
        {
            int convertedID = Integer.parseInt(id);
            customerID = convertedID;
            return "";
        }
        catch(NumberFormatException nfE)
        {
            ExceptionManager.displayError(nfE);
            return "ID is not a number!";
        }
    }
    
    /**
     * Sets division using a division's ID.
     * @param id 
     * @return returns error message or ""
     */
    protected String setDivision(int id)
    {
        division = DivisionManager.getDivision(id);
        if(division == null)
        {
            return "Division ID is invalid!";
        } 
        return "";
    }
    
    /**
     * Sets Customer's name.
     * @param name 
     * @return returns error message if empty, "" if not.
     */
    protected String setName(String name)
    {
        if(name.equals(""))
        {
            return "Name not set!";
        }
        this.name = name;
        return "";
    }
    
    /**
     * Sets Customer's address.
     * @param address 
     * @return returns error message if empty, "" if not.
     */
    protected String setAddress(String address)
    {
        if(address.equals(""))
        {
            return "Address not set!";
        }
        this.address = address;
        return "";
    }
    
    /**
     * Sets Customer's postal code.
     * @param postalCode 
     * @return returns error message if empty, "" if not.
     */
    protected String setPostalCode(String postalCode)
    {
        if(postalCode.equals(""))
        {
            return "Postal Code not set!";
        }
        this.postalCode = postalCode;
        return "";
    }
    
    /**
     * Sets Customer's phone number.
     * @param number 
     * @return returns error message if empty, "" id not.
     */
    protected String setPhoneNumber(String number)
    {
        if(number.equals(""))
        {
            return "Phone Number not set!";
        }
        phoneNumber = number;
        return "";
    }
   
    /**
     * Gets Customer's ID.
     * @return id 
     */
    public int getCustomerID()
    {
        return customerID;
    } 
    
    /**
     * Gets Customer's division ID.
     * @return division 
     */
    public int getDivisionID()
    {
        return division.getDivisionID();
    }
    
    /**
     * Gets customer's name.
     * @return name 
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Gets Customer's address.
     * @return address 
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * Gets Customer's postal code.
     * @return postalCode 
     */
    public String getPostalCode()
    {
        return postalCode;
    }
    
    /**
     * Gets Customer's phone number.
     * @return number 
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    /**
     * Gets Division.
     * @return id 
     */
    public Division getDivision()
    {
        return division;
    }
    
    @Override
    public String toString()
    {
        return name + " ID: " + customerID;
    }

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 89 * hash + this.customerID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        return customerID == other.customerID;
    }

    
    
    
}
