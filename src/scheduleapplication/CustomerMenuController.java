package scheduleapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class for Customers.
 * @author Austin Matthews
 */
public class CustomerMenuController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<?, ?> ID;
    @FXML
    private TableColumn<?, ?> name;
    @FXML
    private TableColumn<?, ?> address;
    @FXML
    private TableColumn<?, ?> postalCode;
    @FXML
    private TableColumn<?, ?> phoneNumber;
    @FXML
    private TableColumn<?, ?> divisionID;
    @FXML
    private Button addCustomer;
    @FXML
    private Button updateCustomerButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextArea messageArea;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //set tables
        //customerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        //makes setting values for tables simpler
        ID.setCellValueFactory(new PropertyValueFactory("customerID"));
        name.setCellValueFactory(new PropertyValueFactory("name"));
        address.setCellValueFactory(new PropertyValueFactory("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory("postalCode"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        divisionID.setCellValueFactory(new PropertyValueFactory("divisionID"));
        
        customerTable.setItems(CustomerManager.getCustomers());
    }    

    @FXML
    private void returnPressed(ActionEvent event) 
    {
        SceneManager.mainMenu();
    }

    @FXML
    private void addCustomerPressed(ActionEvent event) 
    {
        SceneManager.addCustomerMenu();
    }

    @FXML
    private void updateCustomerPressed(ActionEvent event) 
    {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if(customer == null)
        {
            //Dialog.displayMessageConsole(label, message);
            return;
        }
        SceneManager.updateCustomerMenu(customer);
    }

    @FXML
    private void deleteCustomerPressed(ActionEvent event) 
    {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        String endlineCheck = "\n";
        
        if(customer == null)
        {
            //Dialog.displayMessageConsole(label, message);
            return;
        }
        if(Dialog.displayConfirmDialogYN("Delete", "Attempted to Delete Customer", "Are you sure you want to delete this customer?") == 1)
        {
            int count = CustomerManager.customerAppointmentCount(customer);
            if(count > 0)
            {
                
                if(Dialog.displayConfirmDialogYN("Appointments found..", "Customer has " + count + " appointment(s).", "Are you still sure you want to delete this Customer?") == 0)
                {
                    return;
                }
                //prevents a lack of an extra line
                if(!messageArea.getText().equals(""))
                {
                    messageArea.appendText(endlineCheck);
                }
                messageArea.appendText(AppointmentManager.deleteCustomerAppointments(customer.getCustomerID()));
                messageArea.setVisible(true);
                endlineCheck = "";
            }
            
            //prevents extra lines
            if(!messageArea.getText().equals(""))
            {
                messageArea.appendText(endlineCheck);
            }
            
            if(CustomerManager.deleteCustomer(customer))
            {
                customerTable.setItems(CustomerManager.getCustomers());
                messageArea.appendText("Customer of ID " + customer.getCustomerID() + " deleted.");
                messageArea.setVisible(true);
            }
            else
            {
                messageArea.appendText("Customer of ID " + customer.getCustomerID() + " deletion failed. Might have an appointment.");
            }
            //messageArea.setScrollTop();
        }
    }

    @FXML
    private void customerSelectedCheck(MouseEvent event) 
    {
        boolean selected = customerTable.getSelectionModel().getSelectedItem() == null;
        
        updateCustomerButton.setDisable(selected);
        deleteButton.setDisable(selected);
        
    }
    
}
