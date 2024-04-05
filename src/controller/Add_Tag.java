package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Controller;
import util.Photo;
import util.Tag;
import util.User;

/** 
 * Controls the "Add_Tag" stage
 * This class manages the addition of a tag to a photo.
 * It allows the user to input tag information and adds the tag to the selected photo.
 * @author xxxx
 * @author yyyy
 */
public class Add_Tag extends Controller {
	private User currUser;
	private Photo selectedPhoto;
	
	@FXML TextField tagTypeTextField;
	@FXML TextField tagValueTextField;
	
	
	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 * @param currUser is the current User that's accessing this stage
	 * @param selectedPhoto is the Photo that the tag will be added to
	 */
	public void start (Stage primaryStage, User currUser, Photo selectedPhoto) {
		this.mainStage = primaryStage;
		this.currUser = currUser;
		this.selectedPhoto = selectedPhoto;
	}
	
	/**
	 * Adds a tag a to the selectedPhoto
	 */
	public void addTag () {
		String tagType = tagTypeTextField.getText();
		String tagValue = tagValueTextField.getText();
		
		if (!tagType.isEmpty() && !tagValue.isEmpty()) {
			Tag tag = new Tag(tagType, tagValue);
			selectedPhoto.addPhotoTag(tag);
			currUser.saveUser();
			closeCurrentWindow();
		}
		else {
			showErrorDialog("Please provide input for both fields.");
		}
	}
	
}

