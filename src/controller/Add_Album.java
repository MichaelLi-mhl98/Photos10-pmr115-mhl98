package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Album;
import util.Controller;
import util.Photo;
import util.User;

/** 
 * Controls the "Add_Album" stage
 * This class manages the addition of an album to the user's collection.
 * It allows the user to input the album name and adds it to the user's albums.
 * If a list of photos is provided, it populates the album with those photos.
 * Ensures album names are unique.
 * @author xxxx
 * @author yyyy
 */
public class Add_Album extends Controller{
	private User currUser;
	private ArrayList<Photo> result;
	@FXML TextField albumNameTextField;

	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 * @param user is the current User that's accessing this stage
	 */
	public void start(Stage primaryStage,User user) {
		this.currUser = user;
		this.mainStage = primaryStage;
	}
	
	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 * @param user is the current User that's accessing this stage
	 * @param result is the ArrayList of Photos that are passed through this controller
	 * that will be used to populate the added Album
	 */	
	public void start(Stage primaryStage,User user, ArrayList<Photo> result) {
		this.currUser = user;
		this.mainStage = primaryStage;
		this.result = result;
		
	}

	/**
	 * Adds an album to the User if the User inputs a valid album name
	 */
	public void addAlbum() {
		String albumName = albumNameTextField.getText();
		if (!albumName.isEmpty()) {
			for (Album album: currUser.getAlbums()) {
				if (album.getAlbumName().equals(albumName)) {
					showErrorDialog("Albums cannot have the same name.");
					return;
				}
			}

			Album album;
			if (result == null) {
				album = new Album(albumName);
			} else {
				album = new Album(albumName, result);
			}
			currUser.addAlbums(album);

			try {
				// Deserialize storedUsers data
				FileInputStream fileIn = new FileInputStream("data/Data.dat");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ArrayList < User > storedUsers = (ArrayList < User > ) in.readObject();
				in.close();
				fileIn.close();

				// Find currUser in storedUsers and add album to the user
				for (User user: storedUsers) {
					if (user.equals(currUser)) {
						storedUsers.set(storedUsers.indexOf(user), currUser);
					}
				}

				// Serialize updated storedUsers
				FileOutputStream fileOut = new FileOutputStream("data/Data.dat");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(storedUsers);
				out.close();
				fileOut.close();
			} catch (ClassNotFoundException ex) {
				System.out.println("Class not found.");
			} catch (IOException ex) {
				System.out.println("Error reading file.");
			}
		} else {
			return;
		}
		closeCurrentWindow();
	}
}

