package scheduleapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles storing and getting all the Appointments in the Application.
 * @author Austin Matthews
 */
public class AppointmentManager 
{

    private static final Map<Integer, Appointment> appointmentMap = new HashMap<>();
    
    private static final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    
    /**
     * Updates appointment map with database values.
     */
    protected static void updateMap()
    {
        try(ResultSet result = DatabaseManager.getRows("*", "appointments", "ORDER BY Appointment_ID"))
        {
            while(result.next())
            {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(result.getInt("Appointment_ID"));
                appointment.setTitle(result.getString("Title"));
                appointment.setDescription(result.getString("Description"));
                appointment.setType(result.getString("Type"));
                appointment.setLocation(result.getString("Location"));
                appointment.setStartTime(result.getDate("Start"),result.getTime("Start"));
                appointment.setEndTime(result.getDate("End"),result.getTime("End"));
                appointment.setContact(result.getInt("Contact_ID"));
                appointment.setUser(result.getInt("User_ID"));
                appointment.setCustomer(result.getInt("Customer_ID"), true);
                appointmentMap.put(appointment.getAppointmentID(), appointment);
                appointmentList.add(appointment);
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
     * Updates or Inserts Appointment into the database and map.
     * <p>Updates if Appointment exists.</p>
     * <p>Inserts if Appointment does not exist.</p>
     * @param appointment 
     */
    protected static void updateAppointment(Appointment appointment)
    {
        if(appointment == null)
        {    
            return;
        } 
        
        //bool to make sure update was successful
        boolean success;
        
        //set customer columns
        String[] appointmentColumns = {
            "Appointment_ID",
            "Title",
            "Description",
            "Type",
            "Location",
            "Start",
            "End",
            "Contact_ID",
            "User_ID",
            "Customer_ID"};

        //set new values
        String[] appointmentValues = {
            Integer.toString(appointment.getAppointmentID()), 
            "'" + appointment.getTitle() + "'",
            "'" + appointment.getDescription() + "'",
            "'" + appointment.getType() + "'",
            "'" + appointment.getLocation() + "'",
            "'" + appointment.getStartDateTime() + "'",
            "'" + appointment.getEndDateTime() + "'",
            Integer.toString(appointment.getContactID()),
            Integer.toString(appointment.getUserID()),
            Integer.toString(appointment.getCustomerID())};
        
        //update
        success = DatabaseManager.updateRows("appointments", appointmentColumns, appointmentValues, "WHERE Appointment_ID = " + appointment.getAppointmentID());
       
        //updates and adds into the map if successful
        if(success)
        {
            appointmentMap.put(appointment.getAppointmentID(), appointment);
            appointmentList.set(appointmentList.indexOf(appointment), appointment);
                    
        }
        
        DatabaseManager.closeAllStatements();
    }
    
    /**
     * Adds Appointment into the database and map.
     * @param appointment 
     */
    protected static void addAppointment(Appointment appointment)
    {
        if(appointment == null)
        {    
            return;
        } 
        //bool to make sure update was successful
        boolean success;
        
        //set customer columns
        String[] appointmentColumns = {
            "Appointment_ID",
            "Title",
            "Description",
            "Type",
            "Location",
            "Start",
            "End",
            "Contact_ID",
            "User_ID",
            "Customer_ID"};

        //set new values
        String[] appointmentValues = {
            Integer.toString(appointment.getAppointmentID()), 
            "'" + appointment.getTitle() + "'",
            "'" + appointment.getDescription() + "'",
            "'" + appointment.getType() + "'",
            "'" + appointment.getLocation() + "'",
            "'" + appointment.getStartDateTime() + "'",
            "'" + appointment.getEndDateTime() + "'",
            Integer.toString(appointment.getContactID()),
            Integer.toString(appointment.getUserID()),
            Integer.toString(appointment.getCustomerID())};
        
        //insert
        success = DatabaseManager.InsertRow("appointments", appointmentColumns, appointmentValues);
        
        //adds into the map if successful
        if(success)
        {
            //have to get the new id first
            try(ResultSet result = DatabaseManager.getRows("Appointment_ID", "appointments", "ORDER BY Appointment_ID"))
            {
            
                if(result.last())
                {
                    appointment.setAppointmentID(result.getInt(1));
                    appointmentMap.put(appointment.getAppointmentID(), appointment);
                    appointmentList.add(appointment);
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
     * Handles adding and updating appointments when i am unsure which it would be. 
     * @param appointmentList 
     */
    protected static void addUpdateAppointments(ObservableList<Appointment> appointmentList)
    {
        for(Appointment appointment : appointmentList)
        {
            if(appointmentMap.containsKey(appointment.getAppointmentID()))
            {
                updateAppointment(appointment);
            }
            else
            {
                addAppointment(appointment);
            }
        }
    }
    
    /**
     * Deletes Appointment from database and map.
     * @param deletedAppointment 
     * @return returns result as a string.
     */
    protected static String deleteAppointment(Appointment deletedAppointment)
    {
        if(deletedAppointment == null)
        {
            return "No appointment Selected.";
        }
        if(appointmentMap.containsKey(deletedAppointment.getAppointmentID()))
        {
            boolean success = DatabaseManager.deleteRows("appointments", "WHERE Appointment_ID = " + deletedAppointment.getAppointmentID());

            if(success)
            {
                String message = "Appointment of ID " + deletedAppointment.getAppointmentID() + " and Type " + deletedAppointment.getType() + " deleted.";
                appointmentMap.remove(deletedAppointment.getAppointmentID());
                appointmentList.remove(deletedAppointment);
                return message;
            }
        }
        return "Deletion failed.";
    }
    
    /**
     * Gets Appointment using Appointment's ID.
     * @param id
     * @return 
     */
    protected static Appointment getAppointment(int id)
    {
        return appointmentMap.get(id);
    }
    
    
    /**
     * Gets Customer using Appointment's ID.
     * @param appointmentID
     * @return 
     */
    protected static Customer getCustomer(int appointmentID)
    {
        if(appointmentMap.containsKey(appointmentID))
        {
            return getAppointment(appointmentID).getCustomer();
        }
        return null;
    }
    
    /**
     * Deletes all appointments with customerID.
     * @param customerID 
     * @return message of what was deleted.
     */
    protected static String deleteCustomerAppointments(int customerID)
    {
        List<Appointment> custAppointments = getAppointments(customerID);
        if(!custAppointments.isEmpty())
        {
            boolean success = DatabaseManager.deleteRows("appointments", "WHERE Customer_ID = " + customerID);

            if(success)
            {
                String message = "";
                for(Appointment appointment : custAppointments)
                {
                    appointmentMap.remove(appointment.getAppointmentID());
                    appointmentList.remove(appointment);
                    message += "Appointment of ID " + appointment.getAppointmentID() + " and type " + appointment.getType() + " deleted.\n";
                }
                return message;
            }
        }
        return "";
    }
    
    /**
     * Gets Contact using Appointment.
     * @param appointment
     * @return 
     */
    protected static Contact getContact(Appointment appointment)
    {
        if(appointmentMap.containsKey(appointment.getAppointmentID()))
        {
            return appointment.getContact();
        }
        return null;
    }
    
    /**
     * Gets Contact using Appointment's ID.
     * @param appointmentID
     * @return 
     */
    protected static Contact getContact(int appointmentID)
    {
        if(appointmentMap.containsKey(appointmentID))
        {
            return getAppointment(appointmentID).getContact();
        }
        return null;
    }
    
    /**
     * Gets User using Appointment's ID.
     * @param appointmentID
     * @return 
     */
    protected static User getUser(int appointmentID)
    {
        if(appointmentMap.containsKey(appointmentID))
        {
            return getAppointment(appointmentID).getUser();
        }
        return null;
    }    
    
    
     /**
     * Gets all Appointments as a list.
     * @return 
     */
    protected static ObservableList<Appointment> getAppointments()
    {
        
        return appointmentList;
    }
    
     /**
     * Gets all Appointments with customerID as a list.
     * <p>From my research using stream for collections is more efficient due to parallels so i decided to use that for my lambda.</p>
     * <p>https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html</p>
     * <p>It being more efficient or implementing a Listener is all I would use a lambda for right now.</p>
     * @param customerID
     * @return 
     */
    protected static ObservableList<Appointment> getAppointments(int customerID)
    {
        ObservableList<Appointment> custAppointmentList = FXCollections.observableArrayList();
        /* doing it with a procedure. It was a bit slow.
        try(ResultSet result = DatabaseManager.useProcedure("customer_appointments", customerID);)
        {
            while(result.next())
            {
                custAppointmentList.add(AppointmentManager.getAppointment(result.getInt(1)));
            }
            result.close();
        }
        catch(SQLException sqlE)
        {

            DatabaseManager.displayError(sqlE);
        }
        finally
        {
            DatabaseManager.closeAllStatements();
        }
        */
        //simple old lambda. seems faster than using db for now.
        appointmentList.stream().filter(appointment -> (customerID == appointment.getCustomerID())).parallel().forEach(appointment -> {
            custAppointmentList.add(appointment);
        });
        
        return custAppointmentList;
    }
    
     /**
     * Gets all Appointments in Month as a list.
     * @return 
     */
    protected static ObservableList<Appointment> getAppointmentsInMonth()
    {
        ObservableList<Appointment> custAppointmentList = FXCollections.observableArrayList();
        System.out.println("zone id: " + ZoneId.systemDefault().toString());
        try(ResultSet result = DatabaseManager.useProcedure("appointments_month", ZoneManager.getLocalOffset());)
        {
            if(result != null)
            {
                while(result.next())
                {
                    custAppointmentList.add(AppointmentManager.getAppointment(result.getInt(1)));
                }
                result.close();
            }
        }
        catch(SQLException sqlE)
        {

            ExceptionManager.displayError(sqlE);
        }
        finally
        {
            DatabaseManager.closeAllStatements();
        }
        
        return custAppointmentList;
    }
    
    /**
     * Gets all Appointments in Week as a list.
     * @return 
     */
    protected static ObservableList<Appointment> getAppointmentsInWeek()
    {
        ObservableList<Appointment> custAppointmentList = FXCollections.observableArrayList();
        
        try(ResultSet result = DatabaseManager.useProcedure("appointments_week", ZoneManager.getLocalOffset());)
        {
            if(result != null)
            {
                while(result.next())
                {
                    custAppointmentList.add(AppointmentManager.getAppointment(result.getInt(1)));
                }
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
        
        return custAppointmentList;
    }
    
    /**
     * Get contact's appointments.
     * From my research using stream for collections is more efficient so i decided to use that for my lambda here as well.
     * @param contact
     * @return 
     */
    protected static ObservableList<Appointment> getContactSchedule(Contact contact)
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        appointmentList.stream().filter(appointment -> (appointment.getContactID() == contact.getID())).parallel().forEach(appointment -> {
            appointments.add(appointment);
        });
        return appointments;
    }
    
    /**
     * Displays a notification for appointments based on user's local time.
     * @param minutes 
     */
    protected static void appointmentNotification(int minutes)
    {
        //I dont want seconds.
        long longMinutes = (long)minutes;
        int appointmentCount = 0;
        
        for(Appointment appointment : appointmentList)
        {
            ZonedDateTime appointmentDateTime = appointment.getStartDateTimeZone();
            //adding a second so it still detects when its the exact same minute as the appointment
            if(ZonedDateTime.now().isAfter(appointmentDateTime.plusMinutes(1)))
            {
                System.out.println("past appointment");
            }
            else if(ZonedDateTime.now().isAfter(appointmentDateTime.minusMinutes(longMinutes).minusSeconds(1)))
            {
                System.out.println("appointment incoming!");
                Dialog.displayDialog("Upcoming Appointments!", 
                        "Appointment of ID " + appointment.getAppointmentID() + "\n for customer " + appointment.getCustomer().getName(), 
                        "Appointment is at " + ZoneManager.formatedDateTime(appointmentDateTime));
                appointmentCount++;
            }
            
            
        }
        if(appointmentCount == 0)
        {
            Dialog.displayDialog("Upcoming Appointments!", 
                    "No Upcoming Appointments.", 
                    "");
        }
    }
    
    /**
     * Ensures that an appointment time is not overlapping with other appointments.
     * @param checkedAppointment
     * @param tempAppointments
     * @return 
     */
    protected static String appointmentOverlap(Appointment checkedAppointment, ObservableList<Appointment> tempAppointments)
    {
        String message = "";
        
        if(checkedAppointment == null)
        {
            return "";
        }
        
        if(checkedAppointment.getStartDateTimeZone() == null)
        {
            return "";
        }
        
        if(checkedAppointment.getEndDateTimeZone() == null)
        {
            return "";
        }
        
        //for the temp appointments in update customer
        if(tempAppointments != null)
        {
            for(Appointment a : tempAppointments)
            {
                if(!contains(a)
                        && !a.equals(checkedAppointment)
                        && ((checkedAppointment.getStartDateTimeZone().isBefore(a.getEndDateTimeZone().plusSeconds(1)) 
                            && checkedAppointment.getStartDateTimeZone().isAfter(a.getStartDateTimeZone().minusSeconds(1)))
                            || (checkedAppointment.getEndDateTimeZone().isBefore(a.getEndDateTimeZone().plusSeconds(1)) 
                            && checkedAppointment.getEndDateTimeZone().isAfter(a.getStartDateTimeZone().minusSeconds(1)))))
                {
                   return "\nStart Date Time: " + a.getStartTimeFormatted() + "\nEnd Date Time: " + a.getEndTimeFormatted();
                }
            }
        }
        
        //for the appointments in the database
        for(Appointment a : appointmentList)
        {
            if(!a.equals(checkedAppointment)
                    && ((checkedAppointment.getStartDateTimeZone().isBefore(a.getEndDateTimeZone().plusSeconds(1)) 
                        && checkedAppointment.getStartDateTimeZone().isAfter(a.getStartDateTimeZone().minusSeconds(1)))
                        || (checkedAppointment.getEndDateTimeZone().isBefore(a.getEndDateTimeZone().plusSeconds(1)) 
                        && checkedAppointment.getEndDateTimeZone().isAfter(a.getStartDateTimeZone().minusSeconds(1)))))
            {
                return "\nStart Date Time: " + a.getStartTimeFormatted() + "\nEnd Date Time: " + a.getEndTimeFormatted();
            }
        }
        return "";
    }
    
    /**
     * Checks if appointment exists in the database.
     * @param appointment
     * @return 
     */
    protected static boolean contains(Appointment appointment) 
    {
        return appointmentMap.containsKey(appointment.getAppointmentID());
    }

}
