
package scheduleapplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.sql.Date;
import java.sql.Time;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Base class for appointments.
 * @author Austin Matthews
 */
public class Appointment 
{
    private int appointmentID;
    private String title;
    private String description;
    private String type;
    private String location;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    
    //"foreign keys" 
    private Contact contact;
    private User user;
    private Customer customer;
    
    
    /**
     * Default constructor.
     * 
     */
    protected Appointment()
    {
        appointmentID = -1;
        title = "";
        description = "";
        type = "";
        location = "";
        contact = null;
        user = null;
        customer = null;
        startDateTime = null;
        endDateTime = null;
    }
    
    /**
     * Sets Appointment's id.
     * @param id 
     */
    protected void setAppointmentID(int id)
    {
        appointmentID = id;
    }
    /**
     * Sets Appointment's ID using a string.
     * @param id 
     * @return returns error message if not a number, returns "" if a number
     */
    protected String setAppointmentID(String id)
    {
        try
        {
            int convertedID = Integer.parseInt(id);
            appointmentID = convertedID;
            return "";
        }
        catch(NumberFormatException nfE)
        {
            ExceptionManager.displayError(nfE);
            return "Appointment ID is not a number!";
        }
    }
    
    /**
     * Sets Appointment's customer using a Customer's id.
     * @param id Customer id
     * @param safeSet If true will not check if customer exists in database
     * @return returns error message or ""
     */
    protected String setCustomer(int id, boolean safeSet)
    {
        if(safeSet)
        {
            customer = CustomerManager.getCustomer(id);
        }
        else
        {
            customer = new Customer();
            customer.setCustomerID(id);
        }
        if(customer == null)
        {
            return "Customer ID is invalid!";
        }
        return "";
    }
    /**
     * Sets Appointment's Customer using a Customer's id as a string.
     * @param id Customer id
     * @param safeSet If true will not check if customer exists in database
     * @return returns error message if not a number, returns "" if a number
     */
    protected String setCustomer(String id, boolean safeSet)
    {
        int convertedID;
        try
        {
            convertedID = Integer.parseInt(id);
        }
        catch(NumberFormatException nfE)
        {
            ExceptionManager.displayError(nfE);
            return "Customer ID is not a number!";
        }
        return setCustomer(convertedID, safeSet);
    }
    
    /**
     * Sets Appointment's contact using a contact's id.
     * @param id 
     * @return returns error message or ""
     */
    protected String setContact(int id)
    {
        contact = ContactManager.getContact(id);
        if(contact == null)
        {
            return "Contact ID is invalid!";
        }
        return "";
    }
    /**
     * Sets Appointment's contact using a contact's id as a string.
     * @param id 
     * @return returns error message or ""
     */
    protected String setContact(String id)
    {
        int convertedID;
        try
        {
            convertedID = Integer.parseInt(id);
        }
        catch(NumberFormatException nfE)
        {
            ExceptionManager.displayError(nfE);
            return "Contct ID is not a number!";
        }
        return setContact(convertedID);
    }
    
    /**
     * Sets user using a user's id.
     * @param id 
     * @return returns error message or ""
     * 
     */
    protected String setUser(int id)
    {
        user = UserManager.getUser(id);
        if(user == null)
        {
            return "User ID is invalid!";
        }
        return "";
    }
    /**
     * Sets user using a user's id as a string.
     * @param id 
     * @return returns error message or ""
     * 
     */
    protected String setUser(String id)
    {
        int convertedID;
        try
        {
            convertedID = Integer.parseInt(id);
        }
        catch(NumberFormatException nfE)
        {
            ExceptionManager.displayError(nfE);
            return "User ID is not a number!";
        }
        return setUser(convertedID);
    }
    
    /**
     * Sets Appointment's title.
     * @param title
     * @return returns error message or ""
     */
    protected String setTitle(String title)
    {
        if(title.equals(""))
        {
            return "Title not set!";
        }
        this.title = title;
        return "";
    }
    
    /**
     * Sets Appointment's description.
     * @param desc 
     * @return returns error message or ""
     */
    protected String setDescription(String desc)
    {
        if(desc.equals(""))
        {
            return "Description not set!";
        }
        description = desc;
        return "";
    }
    
    /**
     * Sets Appointment's location.
     * @param address 
     * @return returns error message or ""
     */
    protected String setLocation(String address)
    {
        if(address.equals(""))
        {
            return "Location not set!";
        }
        location = address;
        return "";
    }
    
    /**
     * Sets Appointment's type.
     * @param type 
     * @return returns error message or ""
     */
    protected String setType(String type)
    {
        if(type.equals(""))
        {
            return "Type not set!";
        }
        this.type = type;
        return "";
    }
    
    /**
     * Sets start time.
     * @param date
     * @param time
     * @return returns error message or ""
     */
    protected String setStartTime(Date date, Time time)
    {
        try
        {
            LocalDate localDate = date.toLocalDate();
            LocalTime localTime = time.toLocalTime();
            startDateTime = LocalDateTime.of(localDate, localTime);
            //startTime = LocalDateTime.of(date);
            return "";
        }
        catch(DateTimeParseException dtpE)
        {
            return "Start Time format invalid!";
        }
    }
     /**
     * Sets start time using localDate for the date and String for time.
     * @param date
     * @param time
     * @return returns error message or ""
     */
    protected String setStartDateTime(LocalDate date, String time)
    {
        try
        {
            //LocalDate localDate = LocalDate.parse(date);
            LocalTime localTime = LocalTime.parse(time);
            startDateTime = LocalDateTime.of(date, localTime);
            return "";
        }
        catch(DateTimeParseException dtpE)
        {
            return "Start Time format invalid! Its yyyy/MM/dd hh:mm.";
        }
    }
    /**
     * Sets start time using ZonedlocalDate for the date and String for time.
     * @param date
     * @param zonedTime
     * @return returns error message or ""
     */
    protected String setZonedStartDateTime(LocalDate date, String zonedTime)
    {
        
        
            //LocalDate localDate = LocalDate.parse(date);
            if(date == null)
            {
                return "Start Date not set!";
            }
            
            LocalTime localTime;
            try
            {
                localTime = LocalTime.parse(zonedTime, DateTimeFormatter.ofPattern("H:mm"));
            }
            catch(DateTimeParseException dtpE)
            {
                try
                {
                    localTime = LocalTime.parse(zonedTime);
                }
                catch(DateTimeParseException dtpE2)
                {
                    return "Start Time format invalid! Its hh:mm or h:mm.";
                }
            }
            
            ZonedDateTime zonedDateTime = ZonedDateTime.of(date, localTime, ZoneId.systemDefault());
            startDateTime = ZoneManager.toDBHours(zonedDateTime);
            return "";
    }
    
    /**
     * Sets end time.
     * @param date
     * @param time
     * @return returns error message or ""
     */
    protected String setEndTime(Date date, Time time)
    {
        try
        {
            LocalDate localDate = date.toLocalDate();
            LocalTime localTime = time.toLocalTime();
            endDateTime = LocalDateTime.of(localDate, localTime);
            return "";
        }
        catch(DateTimeParseException dtpE)
        {
            return "End Date Time format invalid!";
        }
    }
    /**
     * Sets end time using localDate for the date and String for time.
     * @param date
     * @param time
     * @return returns error message or ""
     */
    protected String setEndDateTime(LocalDate date, String time)
    {
        try
        {
            //LocalDate localDate = LocalDate.parse(date);
            LocalTime localTime = LocalTime.parse(time);
            endDateTime = LocalDateTime.of(date, localTime);
            return "";
        }
        catch(DateTimeParseException dtpE)
        {
            return "End Time format invalid! Its yyyy/MM/dd hh:mm.";
        }
    }
    /**
     * Sets start time using ZonedlocalDate for the date and String for time.
     * @param date
     * @param zonedTime
     * @return returns error message or ""
     */
    protected String setZonedEndDateTime(LocalDate date, String zonedTime)
    {
        
            //LocalDate localDate = LocalDate.parse(date);
            if(date == null)
            {
                return "End Date not set!";
            }
            LocalTime localTime;
            try
            {
                localTime = LocalTime.parse(zonedTime);
            }
            catch(DateTimeParseException dtpE)
            {
                try
                {
                    localTime = LocalTime.parse(zonedTime, DateTimeFormatter.ofPattern("H:mm"));
                }
                catch(DateTimeParseException dtpE2)
                {
                    return "End Time format invalid! Its hh:mm or h:mm.";
                }
            }
            
            ZonedDateTime zonedDateTime = ZonedDateTime.of(date, localTime, ZoneId.systemDefault());
            endDateTime = ZoneManager.toDBHours(zonedDateTime);
            return "";
        
    }
    
    /**
     * Gets Appointment's id.
     * @return the appointment id  
     */
    public int getAppointmentID()
    {
        return appointmentID;
    }
    
    /**
     * Gets Appointment's customer id.
     * @return the customer id 
     */
    public int getCustomerID()
    {
        return customer.getCustomerID();
    }
    
    /**
     * Gets Appointment's contact id.
     * @return contact id 
     */
    public int getContactID()
    {
        return contact.getID();
    }
    
    /**
     * Gets Appointment's user id.
     * @return the user id 
     */
    public int getUserID()
    {
        return user.getID();
    }
    
    /**
     * Gets Appointment's title.
     * @return title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Gets Appointment's description.
     * @return a description  
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Gets Appointment's location.
     * @return an address
     */
    public String getLocation()
    {
        return location;
    }
    
    /**
     * Gets Appointment's type.
     * @return a type
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * Gets start date and time.
     * @return DateTime 
     */
    public LocalDateTime getStartDateTime()
    {
        
        return startDateTime;
        
    }
    /**
     * Gets zoned start date and time.
     * @return DateTime 
     */
    public ZonedDateTime getStartDateTimeZone()
    {
        
        return ZoneManager.dbToLocalHours(startDateTime);
        
    }
    /**
     * Gets start date and time formatted.
     * @return formatted DateTime 
     */
    public String getStartTimeFormatted()
    {
        
        return ZoneManager.formatedDateTime(ZoneManager.dbToLocalHours(startDateTime));
        
    }
    
    /**
     * Gets end date and time.
     * @return DateTime 
     */
    public LocalDateTime getEndDateTime()
    {
        if(endDateTime != null)
        {
            return endDateTime;
        }
        return null;
    }
    /**
     * Gets zoned end date and time.
     * @return DateTime 
     */
    public ZonedDateTime getEndDateTimeZone()
    {
        return ZoneManager.dbToLocalHours(endDateTime);
    }
    /**
     * Gets end date and time formatted.
     * @return formatted time 
     */
    public String getEndTimeFormatted()
    {
        return ZoneManager.formatedDateTime(ZoneManager.dbToLocalHours(endDateTime));
    }
    
    /**
     * Gets Appointment's Customer.
     * @return the customer 
     */
    public Customer getCustomer()
    {
        return customer;
    }
    
    /**
     * Gets Appointment's Contact.
     * @return the contact
     */
    public Contact getContact()
    {
        return contact;
    }
    
    /**
     * Gets Appointment's Contact Name.
     * @return the contact
     */
    public String getContactName()
    {
        return contact.getName();
    }
    
    /**
     * Gets Appointment's User. 
     * @return the user
     */
    public User getUser()
    {
        return user;
    }
    
    /**
     * Checks for errors in start time.
     * @return error message
     */
    protected String checkStartTime()
    {
        if(startDateTime == null)
        {
            
            return "Start Date and/or Time not set!";
        }
        
        if(!ZoneManager.officeHourCheck(ZoneManager.dbToLocalHours(startDateTime)))
        {
            return "Time is outside of office hours!";
        }
        
        if(endDateTime == null)
        {
            
            return "End Date and/or Time not set!";
        }
        
        if(startDateTime.toLocalTime().isAfter(endDateTime.toLocalTime()) && startDateTime.isAfter(endDateTime))
        {
            if(startDateTime.toLocalDate().isAfter(endDateTime.toLocalDate()))
            {
                return "Start Date and Time is after End Time.";
            }
            else
            {
                return "Start Time is after End Time.";
            }
        }
        return "";
    }
    
    /**
     * Checks for errors in start date.
     * @return error message
     */
    protected String checkStartDate()
    {
        if(startDateTime == null)
        {
            
            return "Start Date and/or Time not set!";
        }
        
        if(endDateTime == null)
        {
            
            return "End Date and/or Time not set!";
        }
        
        if(endDateTime != null)
        {
            if(endDateTime.toLocalDate() == null)
            {
                return "Set End Date.";
            }
        }
        else
        {
            return "End Date and/or Time not set!";
        }
        
        if(startDateTime.toLocalDate().isAfter(endDateTime.toLocalDate()))
        {
            return "Start Date is after End Date.";
        }
        return "";
        
    }
    
    /**
     * Checks for errors in end time.
     * @return error message
     */
    protected String checkEndTime()
    {
        
        if(endDateTime == null)
        {
            
            return "End Date and/or Time not set!";
        }
        
        if(!ZoneManager.officeHourCheck(ZoneManager.dbToLocalHours(endDateTime)))
        {
            return "Time is outside of office hours!";
        }
        
        if(startDateTime == null)
        {
            
            return "Start Date and/or Time not set!";
        }
        
        if(startDateTime.toLocalTime().isAfter(endDateTime.toLocalTime()) && startDateTime.isAfter(endDateTime))
        {
            if(startDateTime.toLocalDate().isAfter(endDateTime.toLocalDate()))
            {
                return "End Date and Time is before Start Time.";
            }
            else
            {
                return "End Time is before Start Time.";
            }
        }
        return "";
    }
    
    /**
     * Checks for errors in end date.
     * @return error message
     */
    protected String checkEndDate()
    {
        if(startDateTime == null)
        {
            
            return "Start Date and/or Time not set!";
        }
        
        if(endDateTime == null)
        {
            
            return "End Date and/or Time not set!";
        }
        
        if(startDateTime.toLocalDate().isAfter(endDateTime.toLocalDate()))
        {
            return "End Date is before Start Date.";
        }
        return "";
        
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + appointmentID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Appointment other = (Appointment) obj;
        return appointmentID == other.appointmentID;
    }
}
