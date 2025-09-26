package scheduleapplication;

import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class for Adding and Updating Appointments. 
 *
 * @author Austin Matthews
 */
public class UpdateAppointmentMenuController implements Initializable {

    @FXML
    protected Label appointmentLabel;
    @FXML
    protected Button addUpdateButton;
    @FXML
    protected Button cancelButton;
    @FXML
    private TextField appointmentIDField;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private TextField typeField;    
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeField;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private ComboBox<User> userIDCombo;
    @FXML
    private Label appointmentIDError;
    @FXML
    private Label titleError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label locationError;
    @FXML
    private Label contactError;
    @FXML
    private Label typeError;
    @FXML
    private Label startDateError;
    @FXML
    private Label startTimeError;
    @FXML
    private Label endDateError;
    @FXML
    private Label endTimeError;
    @FXML
    private Label customerError;
    @FXML
    private Label userError;
    @FXML
    private Label workHoursLabel;
    
    ObservableList<Appointment> customerAppointmentList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        contactCombo.getItems().addAll(ContactManager.getContacts());
        userIDCombo.getItems().addAll(UserManager.getUsers());
        customerCombo.getItems().addAll(CustomerManager.getCustomers());
        if(SceneManager.getCurrentSceneName().equals("UpdateCustomerMenu"))
        {
            //if its from customer I really dont want them to change the customer. Just wouldn't make sense.
            customerCombo.setDisable(true);
        }
        workHoursLabel.setText("Work Hours: " + ZoneManager.getWorkHoursLocal());
        userIDCombo.setValue(UserManager.getUser(UserManager.getCurrentUserID()));
        
    }    
    
    @FXML
    private void addUpdatePressed(ActionEvent event) 
    {
        //set in scene manager
    }

    @FXML
    private void clearPressed(ActionEvent event) 
    {
        if(Dialog.clear())
        {
            //appointmentIDField.setText("");
            titleField.setText("");
            descriptionArea.setText("");
            locationField.setText("");
            typeField.setText("");
            startDatePicker.setValue(null);
            startTimeField.setText("");
            endDatePicker.setValue(null);
            endTimeField.setText("");
            if(!customerCombo.isDisabled()) customerCombo.setValue(null);
            userIDCombo.setValue(null);
            contactCombo.setValue(null);
        }
    }
    
    @FXML
    private void cancelPressed(ActionEvent event) 
    {
        //set in scene manager
    }
    
    /**
     * Used to set all the values and target scene for appointments. 
     * <p>Set appointment to null if you only want to set the tempList and vice versa.</p>
     * @param appointment 
     * @param tempList  
     */
    protected void updateFields(Appointment appointment, ObservableList<Appointment> tempList)
    {
        if(appointment != null)
        {
            if(appointment.getAppointmentID() != -1) appointmentIDField.setText(Integer.toString(appointment.getAppointmentID()));
            if(!appointment.getTitle().equals("")) titleField.setText(appointment.getTitle());
            if(!appointment.getDescription().equals("")) descriptionArea.setText(appointment.getDescription());
            if(!appointment.getLocation().equals("")) locationField.setText(appointment.getLocation());
            if(!appointment.getType().equals("")) typeField.setText(appointment.getType());
            if(appointment.getStartDateTime() != null)
            { 
                startDatePicker.setValue(appointment.getStartDateTimeZone().toLocalDate());
                startTimeField.setText(appointment.getStartDateTimeZone().toLocalTime().toString());
            }
            if(appointment.getEndDateTime() != null)
            { 
                endDatePicker.setValue(appointment.getEndDateTimeZone().toLocalDate());
                endTimeField.setText(appointment.getEndDateTimeZone().toLocalTime().toString());
            }
            if(appointment.getCustomer() != null) customerCombo.setValue(appointment.getCustomer());
            if(appointment.getUser() != null) userIDCombo.setValue(appointment.getUser());
            if(appointment.getContact() != null) contactCombo.setValue(appointment.getContact());
        }
        customerAppointmentList = tempList;
    }
    
    /**
     * Creates an appointment based on values in menu.
     * @return 
     */
    protected Appointment fieldsToAppointmentCustomer()
    {
        Appointment appointment = new Appointment();
        String message = "";
        //appointment
        if(!appointmentIDField.getText().equals(""))
        {
            appointmentIDError.setText(appointment.setAppointmentID(appointmentIDField.getText()));
            message += appointmentIDError.getText();
        }
        else
        {
            appointment.setAppointmentID(0);
        }
        //title
        titleError.setText(appointment.setTitle(titleField.getText()));
        message += titleError.getText();
        
        //description
        descriptionError.setText(appointment.setDescription(descriptionArea.getText()));
        message += descriptionError.getText();
        
        //location
        locationError.setText(appointment.setLocation(locationField.getText()));
        message += locationError.getText();
        
        //type
        typeError.setText(appointment.setType(typeField.getText()));
        message += typeError.getText();
        
        //set start date time
        startDateError.setText(appointment.setZonedStartDateTime(startDatePicker.getValue(), startTimeField.getText()));
        message += startDateError.getText();
        
        //test start date picker
        try 
        {
            startDatePicker.getConverter().fromString(startDatePicker.getEditor().getText());
        } 
        catch (DateTimeParseException dtpE) 
        {
            startDateError.setText("Date Format invalid. its d/m/yyyy or dd/mm/yyyy");
            message += "Date Format invalid. its d/m/yyyy or dd/mm/yyyy";
        }
        
        //set end date time
        
        endDateError.setText(appointment.setZonedEndDateTime(endDatePicker.getValue(), endTimeField.getText()));
        message += endDateError.getText();
        
        //test end date picker
        try 
        {
            endDatePicker.getConverter().fromString(endDatePicker.getEditor().getText());
        } 
        catch (DateTimeParseException dtpE) 
        {
            endDateError.setText("Date Format invalid. its d/m/yyyy or dd/mm/yyyy");
            message += "Date Format invalid. its d/m/yyyy or dd/mm/yyyy";
        }
        
        if(startDateError.getText().equals(""))
        {
            //check start date
            startDateError.setText(appointment.checkStartDate());
            message += startDateError.getText();

            //check start time
        
            startTimeError.setText(appointment.checkStartTime());
            message += startTimeError.getText();
        }
        if(endDateError.getText().equals(""))
        {
            //check end date
            endDateError.setText(appointment.checkEndDate());
            message += endDateError.getText();

            //check end time
        
            endTimeError.setText(appointment.checkEndTime());
            message += endTimeError.getText();
        }
        
        //check for overlaps
        String overlap = AppointmentManager.appointmentOverlap(appointment, customerAppointmentList);
        if(!overlap.equals(""))
        {
            Dialog.displayDialog("Appointment Overlap", "An appointment overlaps with set Appointment!", "Overlaping appointment times: " + overlap);
            message += overlap;
        }
        
        //customerID
        if(customerCombo.isDisabled())
        {
            if(customerCombo.getValue() != null)
            {
                customerError.setText(appointment.setCustomer(customerCombo.getValue().getCustomerID(), false));
                message += customerError.getText();
            }
            else
            {
                appointment.setCustomer(0, false);
            }
        }
        else
        {
            if(customerCombo.getValue() != null)
            {
                customerError.setText(appointment.setCustomer(customerCombo.getValue().getCustomerID(), false));
                message += customerError.getText();
            }
            else
            {
                customerError.setText("Customer Not Set");
                message += "Customer Not Set";
            }
        }
        
        //userID
        if(userIDCombo.getValue() != null)
        {
            userError.setText(appointment.setUser(userIDCombo.getValue().getID()));
            message += userError.getText();
        }
        else
        {
            userError.setText("User not set!");
            message += "User not set!";
        }
        
        //contact
        if(contactCombo.getValue() != null)
        {
            contactError.setText(appointment.setContact(contactCombo.getValue().getID()));
            message += contactError.getText();
        }
        else
        {
            contactError.setText("Contact not set!");
            message += "Contact not set.";
        }
        
        //result
        if(message.equals(""))
        {
            return appointment;
        }
        else
        {
            return null;
        }
    }
}
