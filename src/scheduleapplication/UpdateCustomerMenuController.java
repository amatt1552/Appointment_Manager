package scheduleapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


/**
 * FXML Controller class for updating and adding customers.
 *
 * @author Austin Matthews
 */
public class UpdateCustomerMenuController implements Initializable {

    @FXML
    private Button addUpdateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label addUpdateLabel;
    @FXML
    protected TextField iDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ComboBox<Country> countryCombo;
    @FXML
    private ComboBox<Division> divisionCombo;
    @FXML
    private TableView<Appointment> table;
    @FXML
    private TableColumn<?, ?> appointmentID;
    @FXML
    private TableColumn<?, ?> title;
    @FXML
    private TableColumn<?, ?> description;
    @FXML
    private TableColumn<?, ?> location;
    @FXML
    private TableColumn<?, ?> contact;
    @FXML
    private TableColumn<?, ?> type;
    @FXML
    private TableColumn<?, ?> startTime;
    @FXML
    private TableColumn<?, ?> endTime;
    @FXML
    private TableColumn<?, ?> customerID;
    @FXML
    private TableColumn<?, ?> userID;
    @FXML
    private Button removeAppointmentButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Label customerIDError;
    @FXML
    private Label customerNameError;
    @FXML
    private Label addressError;
    @FXML
    private Label postalCodeError;
    @FXML
    private Label phoneError;
    @FXML
    private Label countryError;
    @FXML
    private Label divisionError;
    
    private MenuEnum customerEnum = MenuEnum.Add;
    private Country oldCountryValue = new Country();
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    @FXML
    private TextArea appointmentTextArea;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        appointmentID.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory("title"));
        description.setCellValueFactory(new PropertyValueFactory("description"));
        location.setCellValueFactory(new PropertyValueFactory("location"));
        contact.setCellValueFactory(new PropertyValueFactory("contactName"));
        type.setCellValueFactory(new PropertyValueFactory("type"));
        startTime.setCellValueFactory(new PropertyValueFactory("startTimeFormatted"));
        endTime.setCellValueFactory(new PropertyValueFactory("endTimeFormatted"));
        customerID.setCellValueFactory(new PropertyValueFactory("customerID"));
        userID.setCellValueFactory(new PropertyValueFactory("userID"));
        
        //CountryManager.getCountries().forEach( country -> {countryCombo.getItems().add(country);});
        countryCombo.getItems().addAll(CountryManager.getCountries());
        if(countryCombo.getValue() != null) 
        {
            oldCountryValue = countryCombo.getValue();
        }
        updateDivisions();
        
    }    

    @FXML
    private void addUpdatePressed(ActionEvent event) 
    {
        String message = "";
        Customer customer = new Customer();
 
        customerNameError.setText(customer.setName(nameField.getText()));
        message +=  customerNameError.getText();
        
        addressError.setText(customer.setAddress(addressField.getText()));
        message +=  addressError.getText();
        
        postalCodeError.setText(customer.setPostalCode(postalCodeField.getText()));
        message +=  postalCodeError.getText();
        
        phoneError.setText(customer.setPhoneNumber(phoneNumberField.getText()));
        message += phoneError.getText();
        
        if(countryCombo.getValue() != null)
        {
            countryError.setText("");
        }
        else
        {
            countryError.setText("No Country selected!");
            message += "No Country selected!";
        }
        
        if(divisionCombo.getValue() != null)
        {
            divisionError.setText(customer.setDivision(divisionCombo.getValue().getDivisionID()));
            message +=  divisionError.getText();
        }
        else
        {
            divisionError.setText("No Division selected!");
            message += "No Division selected!";
        }
        
        switch(customerEnum)
        {
            case Add:
                customer.setCustomerID(0);
                if(!message.equals(""))
                {
                    return;
                }
                CustomerManager.addCustomer(customer);
                break;
            case Update:
                message += customer.setCustomerID(iDField.getText());
                if(!message.equals(""))
                {
                    return;
                }
                CustomerManager.updateCustomer(customer);
                break;
        }
        
        appointmentList.forEach(appointment ->{appointment.setCustomer(customer.getCustomerID(), false);});
        AppointmentManager.addUpdateAppointments(appointmentList);
        SceneManager.customerMenu();
    }

    @FXML
    private void cancelPressed(ActionEvent event) 
    {
        close();
    }
    
    
    @FXML
    private void countryChanged(ActionEvent event) 
    {
        //System.out.println(countryCombo.getValue() + "wooooo");
        //System.out.println(oldCountryValue + "weeeeee");
        if(countryCombo.getValue() == null)
        {
            return;
        }
        if(!oldCountryValue.equals(countryCombo.getValue()))
        {
            oldCountryValue = countryCombo.getValue();
            updateDivisions();
        }
    }
    
    @FXML
    private void clearPressed(ActionEvent event) 
    {
        if(Dialog.clear())
        {
            nameField.setText("");
            addressField.setText("");
            postalCodeField.setText("");
            phoneNumberField.setText("");
            countryCombo.setValue(null);
            updateDivisions();
            divisionCombo.setValue(null);
        }
    }


    @FXML
    private void removeAppointment(ActionEvent event) 
    {
        Appointment appointment = table.getSelectionModel().getSelectedItem();
        if(Dialog.displayConfirmDialog("Delete Appointment", "Are you sure you want to delete this Appointment?", "It will also be removed from the database and cannot be undone.") == 1)
        {
            String message;
            //If its not in the map then its not in the database.
            //I might make a more custom message for this instance later.
            if(!AppointmentManager.contains(appointment))
            {
                message = "Appointment of ID " + appointment.getAppointmentID() + " and Type " + appointment.getType() + " deleted";
            }
            else
            {
                message = AppointmentManager.deleteAppointment(appointment);
            }
            if(!appointmentTextArea.getText().equals(""))
            {
                appointmentTextArea.appendText("\n");
            }
            appointmentTextArea.appendText(message);
            appointmentList.remove(appointment);
        }
        
    }

    @FXML
    private void updateAppointment(ActionEvent event) 
    {
        Appointment appointment = table.getSelectionModel().getSelectedItem();
        if(appointment == null)
        {
            //Dialog.displayMessageConsole(label, message);
            return;
        }
        SceneManager.updateAppointment(appointment, appointmentList);
    }

    @FXML
    private void addAppointment(ActionEvent event) 
    {
        SceneManager.addAppointment();
    }
    
    @FXML
    private void appointmentSelectedCheck(MouseEvent event) 
    {
        boolean selected = table.getSelectionModel().getSelectedItem() == null;
        
        updateAppointmentButton.setDisable(selected);
        removeAppointmentButton.setDisable(selected);
        
    }
    
    /**
     * Updates fields and sets whether updating or adding.
     * Set customer to null if only changing if adding or updating.
     * @param customer
     * @param customerEnum 
     */
    protected void updateCustomer(Customer customer, MenuEnum customerEnum)
    {
        if(customer != null)
        {
            
            iDField.setText(Integer.toString(customer.getCustomerID()));
            nameField.setText(customer.getName());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            phoneNumberField.setText(customer.getPhoneNumber());

            Division division = customer.getDivision();
            countryCombo.setValue(division.getCountry());
            updateDivisions();
            divisionCombo.setValue(division);
            
            updateTable(AppointmentManager.getAppointments(customer.getCustomerID()));
        }
        this.customerEnum = customerEnum;
        switch(customerEnum)
        {
            case Add:
                addUpdateButton.setText("Add");
                addUpdateLabel.setText("Adding Customers:");
                appointmentTextArea.appendText("Appointments added will not be saved until you press the add button at the bottom. Appointment ID will change afterwards. \nRemoving cannot be undone.");
                break;
            case Update:
                addUpdateButton.setText("Update");
                addUpdateLabel.setText("Updating Customers:");
                appointmentTextArea.appendText("Appointments added will not be saved until you press the update button at the bottom. Appointment ID will change afterwards. \nRemoving cannot be undone.");
                break;
        }
    }

    /**
     * Adds or updates Appointments and sets whether updating or adding.
     * Set customer to null if only changing if adding or updating.
     * @param appointment
     * @param menuEnum 
     */
    protected void updateTable(Appointment appointment, MenuEnum menuEnum)
    {
        switch(menuEnum)
        {
            case Add:
                addToTable(appointment);
                break;
            case Update:
                updateTableValue(appointment);
                break;
        }
    }
    
    /**
     * Returns to customerMenu.
     */
    protected void close()
    {
        if(Dialog.cancel())
        {
            SceneManager.customerMenu();
        }
    }
    
    private void updateDivisions()
    {
        Country country = countryCombo.getValue();
        if(country == null)
        {
            divisionCombo.getItems().clear();
            return;
        }
       
        //FirstLevelDivisionManager.getDivisions(country.getID()).forEach(division -> {divisionCombo.getItems().add(division);});
        divisionCombo.getItems().clear();
        divisionCombo.getItems().addAll(DivisionManager.getDivisions(country.getID()));
        
    }
    
    private void updateTable()
    {
        table.setItems(appointmentList);
    }
    private void updateTable(ObservableList<Appointment> newAppointmentList)
    {
        
        appointmentList.addAll(newAppointmentList);
        table.setItems(appointmentList);
    }
    
    private void addToTable(Appointment appointment)
    {
        
        appointmentList.add(appointment);
        table.setItems(appointmentList);
    }
    private void updateTableValue(Appointment appointment)
    {
        if(appointmentList.contains(appointment))
        {
            appointmentList.set(appointmentList.indexOf(appointment),appointment);
            table.setItems(appointmentList);
        }
        else
        {
            System.out.println("whaaaaa");
        }
    }
    
}
