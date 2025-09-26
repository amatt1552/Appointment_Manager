package scheduleapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class for main menu.
 *
 * @author Austin Matthews
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void customerPressed(ActionEvent event) 
    {
        SceneManager.customerMenu();
    }

    @FXML
    private void appointmentPressed(ActionEvent event) 
    {
        SceneManager.appointmentMenu();
    }
    
    @FXML
    private void logoutPressed(ActionEvent event) 
    {
        SceneManager.loginMenu();
    }

    @FXML
    private void reportsPressed(ActionEvent event) 
    {
        SceneManager.reportsMenu();
    }
    
}
