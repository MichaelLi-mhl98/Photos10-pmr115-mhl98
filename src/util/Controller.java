package util;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Class Representation of a Controller object
 * This class serves as the base controller for the JavaFX application.
 * It provides methods for initializing stages, displaying error dialogs, and closing windows.
 * Additionally, it holds references to the primary stage, current user, and an ArrayList of photos.
 * 
 * @author xxxx
 * @author yyyy
 *
 */
public class Controller {
    protected Stage mainStage;
    /**
     * Initializes controller's private fields and sets up controller for stage.
     * 
     * @param mainStage the Stage that this controller controls
     */
    public void start(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Initializes controller's private fields and sets up controller for stage.
     * Additionally, it accepts an ArrayList of photos that will be used to populate an added Album.
     * 
     * @param mainStage the Stage that this controller controls
     * @param user the current User that's accessing this stage
     * @param photoList the ArrayList of Photos passed through this controller
     *                  that will be used to populate the added Album
     */
    public void start(Stage mainStage, User user, ArrayList<Photo> photoList) {
        this.mainStage = mainStage;
    }
       
    /**
     * Initializes controller's private fields and sets up controller for stage.
     * 
     * @param mainStage the Stage that this controller controls
     * @param user the current User that's accessing this stage
     */
    public void start(Stage mainStage, User user) {
        this.mainStage = mainStage;
    }
    
    /**
     * Closes the current window.
     */
    public void closeCurrentWindow() {
        mainStage.close();
    }

    /**
     * Displays an error dialog with the given error message.
     * 
     * @param errorMessage the error message to be displayed
     */
    public void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(this.mainStage);
        alert.setTitle("ALERT ERROR");
        alert.setHeaderText("ERROR");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }    
}

