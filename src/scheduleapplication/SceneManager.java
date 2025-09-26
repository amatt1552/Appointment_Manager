package scheduleapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Enum for customer menu.
 */
enum MenuEnum
{
    Add, Update
}

/**
 * Manages changing the scenes.
 * @author Austin Matthews
 */
public class SceneManager 
{
   
    //Used so when I close this window i can still close all the appointment stages.
    private static final List<Stage> appointmentStages = new ArrayList<>();
    
    private static Stage mainStage;
    private static Object mainController;
    private static String currentScene;
    
    /**
     * Initializes the main stage.
     * @param stage 
     */
    protected static void setMainStage(Stage stage)
    {
        mainStage = stage;
        //sets event for x
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event) 
            {
                if(mainStopCheck())
                {
                    DatabaseManager.closeConnection();
                    closeAppointments();
                    mainStage.close();
                }
                event.consume();
            }
        });
        
        //sets event for restore down
        //https://stackoverflow.com/questions/15659817/listener-for-a-stage-minimizing-maximizing-etc
        mainStage.maximizedProperty().addListener(new ChangeListener<Boolean>() 
        {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) 
            {
                if(!mainStage.isMaximized())
                {
                    restoreStage();
                }
            }
        });
        //set the scene
        loginMenu();
    }
    
    /**
     * Sets scene to LoginMenu.
     */
    protected static void loginMenu()
    {
        setScene("LoginMenu", LocaleManager.getLoginTitle());
    }
    
    /**
     * Sets scene to MainMenu.
     */
    protected static void mainMenu()
    {
        if(isUpdateAppointmentOpen())
        {
            if(Dialog.displayConfirmDialogYN("Return to Menu", "Are you sure?", "You still haven't saved the appointment you're working on.") == 1)
            {
                closeAppointments();
            }
        }
        
        setScene("MainMenu", "Main Menu");
    }
    
    /**
     * Sets scene to UpdateCustomerMenu and changes UpdateCustomerMenu's state.
     */
    protected static void addCustomerMenu()
    {
        //ScheduleApplicationMain.setScene("UpdateCustomerMenu", CustomerMenuEnum.Add);
        
        setScene("UpdateCustomerMenu", "Add Customer Menu");

        UpdateCustomerMenuController controller = (UpdateCustomerMenuController)mainController;
        controller.updateCustomer(null, MenuEnum.Add);
        
    }
    
    /**
     * Sets scene to UpdateCustomerMenu and changes UpdateCustomerMenu's state.
     * @param customer
     */
    protected static void updateCustomerMenu(Customer customer)
    {
        //ScheduleApplicationMain.setScene("UpdateCustomerMenu");
        setScene("UpdateCustomerMenu", "Update Customer Menu");

        UpdateCustomerMenuController controller = (UpdateCustomerMenuController) mainController;
        controller.updateCustomer(customer, MenuEnum.Update);
        
    }
    
    /**
     * Adds appointment for a customer.
     */
    protected static void addAppointment()
    {
        //used to get the customer and user ids
        if(currentScene.equals("UpdateCustomerMenu"))
        {
            UpdateCustomerMenuController customerController = (UpdateCustomerMenuController) mainController;
            Appointment appointment = new Appointment();
            if(!customerController.iDField.getText().equals(""))
            {
                appointment.setCustomer(customerController.iDField.getText(), false);
            }
            appointment.setUser(UserManager.getCurrentUserID());

            //creates the appointment stage
            addUpdateAppointmentMenu(appointment, null, "Add Appointment Menu", MenuEnum.Add);
        }
        else
        {
            //creates the appointment stage with no values
            addUpdateAppointmentMenu(null, null, "Add Appointment Menu", MenuEnum.Add);
        }
        
    }
    
    /**
     * Adds selected appointment to the add/UpdateAppointmentMenu.
     * @param appointment 
     */
    protected static void updateAppointment(Appointment appointment, ObservableList<Appointment> tempList)
    {
        addUpdateAppointmentMenu(appointment, tempList, "Update Appointment Menu", MenuEnum.Update);
    }
    
    /**
     * Opens an appointment menu
     * @param title
     */
    private static void addUpdateAppointmentMenu(Appointment appointment, ObservableList<Appointment> tempList, String title, MenuEnum menuEnum)
    {
        Stage stage = new Stage();
        FXMLLoader loader;
        Scene scene;
        try 
        {
            loader = new FXMLLoader(ScheduleApplicationMain.class.getResource("UpdateAppointmentMenu.fxml"));
            scene = new Scene(loader.load());
        } 
        catch (IOException e) 
        {
            ExceptionManager.displayError(e);
            return;
        }
        
        
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        appointmentStages.add(stage);
        
        //get appointment menu controller
        UpdateAppointmentMenuController controller = (UpdateAppointmentMenuController) loader.getController();
        
        //set starting values
        switch(menuEnum)
        {
            case Add:
                controller.addUpdateButton.setText("Add");
                controller.appointmentLabel.setText("Adding Appointment:");
                break;
            case Update:
                controller.addUpdateButton.setText("Update");
                controller.appointmentLabel.setText("Updating Appointment:");
                break;
        }
        if(appointment != null)
        {
            controller.updateFields(appointment, tempList);
        }
        
        //setting event for cancel
        controller.cancelButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(stopCheck())
                {
                    stage.close();
                    appointmentStages.remove(stage);
                    mainStage.getScene().getRoot().setDisable(false);
                   
                }
            }
        });
        
        //setting event for addUpdate
        controller.addUpdateButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) 
            {
                //Get values 
                Appointment appointment = controller.fieldsToAppointmentCustomer();
                if(appointment == null)
                {
                    return;
                }
                if(currentScene.equals("UpdateCustomerMenu"))
                {
                    //Set them to the customer Menu
                    UpdateCustomerMenuController customerController = (UpdateCustomerMenuController) mainController;
                    customerController.updateTable(appointment, menuEnum);
                    stage.close();
                    appointmentStages.remove(stage);
                    mainStage.getScene().getRoot().setDisable(false);
                }
                else
                {
                    //Set them to the appointment Menu
                    AppointmentMenuController appointmentController = (AppointmentMenuController) mainController;
                    appointmentController.updateTable(appointment, menuEnum);
                    stage.close();
                    appointmentStages.remove(stage);
                    mainStage.getScene().getRoot().setDisable(false);
                }
            }
        });
                
        //setting event for x 
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event) 
            {
                if(stopCheck())
                {
                    stage.close();
                    appointmentStages.remove(stage);
                    mainStage.getScene().getRoot().setDisable(false);
                }
                event.consume();
            }
        });
        
        mainStage.getScene().getRoot().setDisable(true);
    }
    
    /**
     * checks if appointments are being updated or added
     * @return 
     */
    private static boolean isUpdateAppointmentOpen()
    {
        return !appointmentStages.isEmpty();
    }
    
    /**
     * closes all the add appointment windows
     */
    private static void closeAppointments()
    {
        appointmentStages.forEach(stage -> stage.close());
        appointmentStages.clear();
    }
    
    /**
     * Sets scene to Customer Menu.
     */
    protected static void customerMenu()
    {
        setScene("CustomerMenu", "Customer Menu");
        closeAppointments();
    }
    
    /**
     * Sets scene to Appointment Menu.
     */
    protected static void appointmentMenu()
    {
        setScene("AppointmentMenu", "Appointments Menu");
        closeAppointments();
    }
    
    /**
     * Sets scene to Reports Menu.
     */
    protected static void reportsMenu()
    {
        setScene("ReportsMenu", "Reports Menu");
    }
    
    private static boolean stopCheck()
    {
        return Dialog.cancel();
    }
    
    /**
     * Tries to close the application.
     */
    protected static void closeStage()
    {
        if(mainStopCheck())
        {
            DatabaseManager.closeConnection();
            appointmentStages.forEach(app -> app.close());
            mainStage.close();
        }
    }
    
    /**
     * Tries to close the application without a prompt.
     */
    protected static void closeStageNoPrompt()
    {
        
        DatabaseManager.closeConnection();
        appointmentStages.forEach(app -> app.close());
        mainStage.close();
    }
    
    /**
     * Restores the stage to preferred size.
     */
    private static void restoreStage()
    {
        if(mainStage != null) mainStage.sizeToScene();
    }
    
    protected static String getCurrentSceneName()
    {
        return currentScene;
    }
    
    private static Object getMainController()
    {
        return mainController;
    }
    
    //Warns about closing the application.
    private static boolean mainStopCheck()
    {
        return Dialog.displayConfirmDialogYN("Closing Window", "Are you sure?", "This will close the entire Application.") == 1;
        
    }
    
    //----------------------------------scene creation!----------------------------------------
    
    /**
     * Sets the current scene. 
     * file name does not need .fxml at the end.
     * @param fxmlFileName file name for the fxml file
     * @param title title for scene. Set to Inventory System by default
     * @return returns the controller
     */
    private static Object setScene(String fxmlFileName, String title)
    {
        if(mainStage == null)
        {
            System.out.println("No stage set!");
            return null;
        }
        FXMLLoader loader;
        Scene scene;
        Parent parent;
        try
        {
            loader = new FXMLLoader(ScheduleApplicationMain.class.getResource(fxmlFileName.concat(".fxml")));
            parent = loader.load();
            // Load the FXML file.
            
        }
        catch(IOException ioE)
        {
            ExceptionManager.displayError(ioE);
            return null;
        }
        
        // Display our window.
        if(mainStage.getScene() == null)
        {
            scene = new Scene(parent);
            mainStage.setScene(scene);
            
        }
        else
        {
            mainStage.getScene().setRoot(parent);
        }
        if(!mainStage.isMaximized())
        {
            mainStage.sizeToScene();
        }
        mainStage.centerOnScreen();
        mainStage.show();
        mainStage.setTitle(title);

        //set current scene
        currentScene = fxmlFileName;
        mainController = loader.getController();
        return mainController;
    }
    /**
     * Sets the current scene with title Inventory System. 
     * file name does not need .fxml at the end
     * @param fxmlFileName file name for the fxml file
     * @return returns the controller
     */
    private static Object setScene(String fxmlFileName)
    {
        if(mainStage == null)
        {
            System.out.println("No stage set!");
            return null;
        }
        
        FXMLLoader loader;
        Scene scene;
        Parent parent;
        try
        {
            loader = new FXMLLoader(ScheduleApplicationMain.class.getResource(fxmlFileName.concat(".fxml")));
            parent = loader.load();
            
        }
        catch(IOException ioE)
        {
            ExceptionManager.displayError(ioE);
            return null;
        }
        // Display our window.
        if(mainStage.getScene() == null)
        {
            scene = new Scene(parent);
            mainStage.setScene(scene);
            
        }
        else
        {
            mainStage.getScene().setRoot(parent);
        }
        if(!mainStage.isMaximized())
        {
            mainStage.sizeToScene();
        }
        mainStage.centerOnScreen();
        mainStage.show();
        mainStage.setTitle("Schedule Application");

        //used to see what scene i am on currently 
        currentScene = fxmlFileName;
        //fire event
        //loadComplete.loaded();
        mainController = loader.getController();
        return mainController;
    }
    
}
