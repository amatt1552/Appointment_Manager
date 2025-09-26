package scheduleapplication;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Helps display dialog messages.
 * @author Austin Matthews
 */
public class Dialog 
{
    /**
     * Display an error in a label.
     * @param label label used to display error
     * @param message message displayed
     */
    protected static void displayMessageConsole(Label label, String message)
    {
        label.setText(message);
    }
    
    /**
     * Creates a confirmation dialog box with a message. 
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
     * @param title string passed to title
     * @param header string passed header text
     * @param message string passed to content text
     * @return result (0 = cancel, 1 = ok)
     */
    protected static int displayConfirmDialog(String title, String header, String message)
    {
        //setting alert
        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION);
        alertDialog.setTitle(title);
        alertDialog.getDialogPane().setHeaderText(header);
        alertDialog.getDialogPane().setContentText(message);
        
        //check result
        //optional seems to be good for things where null checks wouldn't work.
        Optional<ButtonType> result = alertDialog.showAndWait();
        if (result.isPresent())
        {
            if(result.get() == ButtonType.CANCEL)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        return 0;
        //int input = JOptionPane.showConfirmDialog(null, message, "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        //JOptionPane.showConfirmDialog(null, message); .getDialogPane().lookupButton(ButtonType.OK)).setText("Not OK Anymore");
    }
    
    /**
     * Creates a confirmation dialog box with a message as yes or no. 
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
     * @param title string passed to title
     * @param header string passed header text
     * @param message string passed to content text
     * @return result (0 = cancel, 1 = ok)
     */
    protected static int displayConfirmDialogYN(String title, String header, String message)
    {
        //setting alert
        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION);
        alertDialog.setTitle(title);
        alertDialog.getDialogPane().setHeaderText(header);
        alertDialog.getDialogPane().setContentText(message);
        //sets button's text
        Button button = (Button) alertDialog.getDialogPane().lookupButton(ButtonType.OK);
        button.setText("Yes");
        button = (Button) alertDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        button.setText("No");
        //check result
        //optional seems to be good for things where null checks wouldn't work.
        Optional<ButtonType> result = alertDialog.showAndWait();
        if (result.isPresent())
        {
            if(result.get() == ButtonType.CANCEL)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        return 0;
    }
    
    /**
     * Creates a confirmation dialog box with a message. 
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
     * @param title string passed to title
     * @param header string passed header text
     * @param message string passed to content text
     */
    protected static void displayDialog(String title, String header, String message)
    {
        //setting alert
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle(title);
        alertDialog.getDialogPane().setHeaderText(header);
        alertDialog.getDialogPane().setContentText(message);
        alertDialog.show();
        
    }
    
    /**
     * Predefined cancel message.
     * @return 
     */
    protected static boolean cancel()
    {
        return Dialog.displayConfirmDialogYN("Canceling", "Are you sure?", "Values wont be saved if you cancel.") == 1;        
    }
    
    /**
     * Predefined clear message.
     * @return 
     */
    protected static boolean clear()
    {
        return Dialog.displayConfirmDialog("Clearing", "Are you sure?", "Clearing fields cannot be undone.") == 1;
        
    }
}
