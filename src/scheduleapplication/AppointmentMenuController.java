package scheduleapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class for Appointments.
 * @author Austin Matthews
 */
public class AppointmentMenuController implements Initializable {

    @FXML
    private Button deleteButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private RadioButton allRadio;
    @FXML
    private ToggleGroup orderBy;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private Button updateAppointmentButton;
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
    private TextArea messageArea;
    

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
        
        updateTable();
    }    

    @FXML
    private void returnPressed(ActionEvent event) 
    {
        SceneManager.mainMenu();
    }

    @FXML
    private void searchInputChanged(KeyEvent event) {
    }


    @FXML
    private void deleteAppointmentPressed(ActionEvent event) 
    {
        if(!messageArea.getText().equals(""))
        {
             messageArea.appendText("\n");
        }
        Appointment appointment = table.getSelectionModel().getSelectedItem();
        if(Dialog.displayConfirmDialogYN("Deleting Appointment..", "Are you sure you want to delete this Appointment?", "This cannot be undone.") == 1)
        {
            messageArea.setVisible(true);
            messageArea.appendText(AppointmentManager.deleteAppointment(appointment));
            updateTable();   
        }
        
    }

    @FXML
    private void addAppointmentPressed(ActionEvent event) 
    {
        SceneManager.addAppointment();
    }

    @FXML
    private void updateAppointmentPressed(ActionEvent event) 
    {
        Appointment appointment = table.getSelectionModel().getSelectedItem();
        if(appointment != null)
        {
            SceneManager.updateAppointment(appointment, null);
        }
    }
    
    @FXML
    private void appointmentSelectedCheck(MouseEvent event) 
    {
        boolean selected = table.getSelectionModel().getSelectedItem() == null;
        
        updateAppointmentButton.setDisable(selected);
        deleteButton.setDisable(selected);
        
    }
    
    @FXML
    private void toAll(ActionEvent event) 
    {
        updateTable();
    }

    @FXML
    private void toCurrentMonth(ActionEvent event) 
    {
        table.setItems(AppointmentManager.getAppointmentsInMonth());
    }

    @FXML
    private void toCurrentWeek(ActionEvent event) 
    {
        table.setItems(AppointmentManager.getAppointmentsInWeek());
    }
    
    /**
     * Sets Appointment's table values using the database.
     */
    protected void updateTable()
    {
        if(!allRadio.isSelected())
        {
            allRadio.setSelected(true);
        }
        table.setItems(AppointmentManager.getAppointments());
    }
    
    /**
     * Adds or updates Appointment's table values using passed appointment.
     * @param appointment 
     * @param menuEnum Sets Whether its adding or updating.
     */
    protected void updateTable(Appointment appointment, MenuEnum menuEnum)
    {
        if(!allRadio.isSelected())
        {
            allRadio.setSelected(true);
        }
        if(appointment != null)
        {
            switch(menuEnum)
            {
                case Add:
                    AppointmentManager.addAppointment(appointment);
                    break;
                case Update:
                    AppointmentManager.updateAppointment(appointment);
                    break;
            }
        }
        table.setItems(AppointmentManager.getAppointments());
    }

}
