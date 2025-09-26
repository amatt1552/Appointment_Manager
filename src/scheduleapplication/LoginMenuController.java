package scheduleapplication;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class for Login.
 *
 * @author Austin Matthews
 */
public class LoginMenuController implements Initializable {

    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField passwordField;
    @FXML
    private Label zoneIDLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        usernameLabel.setText(LocaleManager.getUsername());
        passwordLabel.setText(LocaleManager.getPassword());
        cancelButton.setText(LocaleManager.getCancel());
        loginButton.setText(LocaleManager.getLogin());
        zoneIDLabel.setText(zoneIDLabel.getText() + " " + ZoneId.systemDefault().toString());
    }    

    @FXML
    private void CancelButton(ActionEvent event) 
    {
        SceneManager.closeStageNoPrompt();
    }

    @FXML
    private void LoginButton(ActionEvent event) 
    {
        User user = UserManager.getAuthenticatedUser(usernameField.getText(), passwordField.getText());
        if(user == null)
        {
            String[] errorArray = LocaleManager.getError();
            Dialog.displayConfirmDialog(errorArray[0], errorArray[1], errorArray[2]);
            return;
        }
        UserManager.setCurrentUser(user);
        SceneManager.mainMenu();
        AppointmentManager.appointmentNotification(15);
    }
    
}
