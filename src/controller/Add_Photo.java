package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.Album;
import util.Controller;
import util.Photo;
import util.User;

/** 
 * Controls the "Add_Photo" stage
 * This class manages the addition of a photo to an album.
 * It allows the user to select a photo file, specify a caption, and add the photo to the album.
 * @author xxxx
 * @author yyyy
 */
public class Add_Photo extends Controller{
	private User currUser;
	private Album album;
	private File selectedFile;
	
	@FXML TextField photoPathTextField;
	@FXML TextField captionTextField;
	
	
	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 * @param currUser is the current User that's accessing this stage
	 * @param album is the album that a photo will be added to
	 */
	public void start (Stage primaryStage, User currUser, Album album) {
		this.mainStage = primaryStage;
		this.currUser = currUser;
		this.album = album;
	}
	
	/**
	 * Adds a photo the current User's album based off of the User's selectedFile
	 */
	public void addPhoto () {
		if (selectedFile == null) {
			return;
		}
		if (selectedFile.exists()) {
			String caption = captionTextField.getText();
			if (!caption.isEmpty()) {
				album.addPhoto(new Photo (selectedFile, caption));
			}
			else {
				album.addPhoto(new Photo (selectedFile));
			}
			currUser.saveUser();			
		}
	}
	
	/**
	 * Populates the selectedFile by prompting the user to choose a file
	 */
	public void getUserFile () {
		FileChooser fileChooser = new FileChooser();
		selectedFile = fileChooser.showOpenDialog(mainStage);
		
		if (selectedFile != null) {
			photoPathTextField.setText(("/data/Stock-photos/" + selectedFile.getName()).toString());
		}
	}
}

