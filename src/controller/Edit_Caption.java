package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Controller;
import util.Photo;
import util.User;

/** 
 * Controls the "Edit_Caption" stage
 * This controller manages the functionality to edit the caption of a photo.
 * It receives the current user and the selected photo whose caption is being edited.
 * Upon editing, it updates the photo's caption and saves the changes.
 * @author xxxx
 * @author yyyy
 */
public class Edit_Caption extends Controller {
    private User currUser;
    private Photo selectedPhoto;    
    
    @FXML TextField captionTextField;
    
    /**
     * Initializes controller's private fields and sets up controller
     * for stage
     * @param primaryStage is the Stage that this controller controls
     * @param currUser is the current User that's accessing this stage
     * @param selectedPhoto is the photo that is getting its caption edited
     */
    public void start (Stage primaryStage, User currUser, Photo selectedPhoto) {
        this.mainStage = primaryStage;
        this.currUser = currUser;
        this.selectedPhoto = selectedPhoto;
    }
    
    /**
     * Edits the caption of the selectedPhoto based on the User's input
     */
    public void editCaption () {
        String caption = captionTextField.getText();
        selectedPhoto.setPhotoCaption(caption);
        currUser.saveUser();
        closeCurrentWindow();
    }
}

