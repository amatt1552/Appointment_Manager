package scheduleapplication;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class for Reports Menu.
 *
 * @author Austin Matthews
 */
public class ReportsMenuController implements Initializable {

    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private TableView<Appointment> contactTable;
    @FXML
    private TableColumn<?, ?> appointmentID;
    @FXML
    private TableColumn<?, ?> title;
    @FXML
    private TableColumn<?, ?> typeContact;
    @FXML
    private TableColumn<?, ?> description;
    @FXML
    private TableColumn<?, ?> startTime;
    @FXML
    private TableColumn<?, ?> endTime;
    @FXML
    private TableColumn<?, ?> customerID;
    @FXML
    private TableView<CountMonthType> typeMonthTable;
    @FXML
    private TableColumn<?, ?> count;
    @FXML
    private TableColumn<?, ?> month;
    @FXML
    private TableColumn<?, ?> typeCustomer;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private TextField customerCountField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //contact appointments
        appointmentID.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory("title"));
        description.setCellValueFactory(new PropertyValueFactory("description"));
        typeContact.setCellValueFactory(new PropertyValueFactory("type"));
        startTime.setCellValueFactory(new PropertyValueFactory("startTimeFormatted"));
        endTime.setCellValueFactory(new PropertyValueFactory("endTimeFormatted"));
        customerID.setCellValueFactory(new PropertyValueFactory("customerID"));
        
        //type month count 
        count.setCellValueFactory(new PropertyValueFactory("count"));
        month.setCellValueFactory(new PropertyValueFactory("month"));
        typeCustomer.setCellValueFactory(new PropertyValueFactory("type"));
        
        updateMonthType();
        contactCombo.setItems(ContactManager.getContacts());
        customerCombo.setItems(CustomerManager.getCustomers());       
    }    
    
    private void updateMonthType()
    {
        ObservableList<CountMonthType> countList = FXCollections.observableArrayList();
        ResultSet result = DatabaseManager.useProcedure("count_month_type");
        try
        {
            while(result.next())
            {
                CountMonthType newCount = new CountMonthType(result.getInt("Count"), 
                        result.getInt("Month"),
                        result.getString("Type"));
                countList.add(newCount);
            }
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        DatabaseManager.closeAllStatements();
        typeMonthTable.setItems(countList);
    }

    @FXML
    private void contactChanged(ActionEvent event) 
    {
        if(contactCombo.getValue() == null)
        {
            return;
        }
        contactTable.setItems(AppointmentManager.getContactSchedule(contactCombo.getValue()));
    }

    @FXML
    private void mainMenuPressed(ActionEvent event) 
    {
        SceneManager.mainMenu();
    }

    @FXML
    private void customerChanged(ActionEvent event) 
    {
        if(customerCombo.getValue() == null)
        {
            return;
        }
        customerCountField.setText(Integer.toString(CustomerManager.customerAppointmentCount(customerCombo.getValue())));
    }
    
    /**
     * Sets the appointment count for each appointment.
     */
    protected class CountMonthType
    {
        private final int count;
        private final int month;
        private final String type;
        
        /**
         * Constructor.
         * @param newCount
         * @param newMonth
         * @param newType 
         */
        public CountMonthType(int newCount, int newMonth, String newType)
        {
            count = newCount;
            month = newMonth;
            type = newType;
        }
        
        /**
         * Gets appointment Count.
         * @return 
         */
        public int getCount()
        {
            return count;
        }
        
        /**
         * Gets appointment Month.
         * @return 
         */
        public int getMonth()
        {
            return month;
        }
        
        /**
         * Gets appointment Type.
         * @return 
         */
        public String getType()
        {
            return type;
        }
        
    }
    
    
}
