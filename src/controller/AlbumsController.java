package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.Album;
import util.Controller;
import util.StageManager;
import util.User;

/** 
 * Controls the "Albums" stage
 * @author xxxx
 * @author yyyy
 */
public class AlbumsController extends Controller implements Serializable{

	private static final long serialVersionUID = 1L;

	@FXML ListView<Album> albumsListView;
	@FXML Button add;
	@FXML Button delete;
	@FXML Button edit;
	@FXML Button search;
	@FXML Button logout;
	@FXML Button quit;
	@FXML TextField albumName;
	@FXML TextField numPhotos;
	@FXML TextField dateRange;
	
	private User currUser;

	public static final Comparator<Album> AlbumComparator = new Comparator<>() {
		public int compare(Album a1, Album a2) {
			if(a1.getAlbumName().compareToIgnoreCase(a2.getAlbumName())==0) {
				if(a1.getNewestPhotoDateString().compareToIgnoreCase(a2.getNewestPhotoDateString())==0){
					return a2.getOldestPhotoDateString().compareToIgnoreCase(a1.getOldestPhotoDateString());
				}else {	
					return a1.getNewestPhotoDateString().compareToIgnoreCase(a2.getNewestPhotoDateString());
				}
			}else {
				return a1.getAlbumName().compareToIgnoreCase(a2.getAlbumName());
			}
		}
	};
	
	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 * @param user is the current User that's accessing this stage
	 */
	public void start(Stage primaryStage,User user) {
		this.currUser = user;
		this.mainStage = primaryStage;
		displayAlbums();
	}
	
	/**
	 * Opens up the "Edit_Album" stage
	 * @param e the ActionEvent that prompted the button
	 * @throws IOException
	 */
	public void editAlbum() throws IOException{
		Album selectedAlbum = albumsListView.getSelectionModel().getSelectedItem();
		
		if (selectedAlbum == null) {
			showErrorDialog("No album selected.");
			return;
		}
		
		// Loads a scene onto a stage
		// Set up EditAlbumController
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/EditAlbum.fxml"));
		Parent root = loader.load();
		Edit_Album controller = loader.getController();
		
		Stage secondaryStage = new Stage();
		controller.start(secondaryStage, currUser, selectedAlbum);
		
		// Set up secondaryStage
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		secondaryStage.showAndWait();
		
		// After prompting user for edit, re-display updated albums
		displayAlbums();
	}
	
	/**
	 * Opens up the "Search_Photo" stage
	 * @param e the ActionEvent that prompted the button
	 * @throws IOException
	 */
	public void searchAlbum() throws IOException{
		StageManager stageManager = new StageManager();
		stageManager.loadScene(mainStage, "SearchPhoto", currUser);
	}	
	
	/**
	 * Returns User to the "Login" stage
	 * @param e the ActionEvent that prompted the button
	 * @throws IOException
	 */
	public void logOut () throws IOException {
		StageManager stageManager = new StageManager();
		stageManager.loadScene(mainStage, "Login");
	}
			
	/**
	 * Opens up the "AddAlbum" stage
	 * @param e the ActionEvent that prompted the button 
	 * @throws IOException
	 */
	public void addAlbum () throws IOException {
		// Open a second window that prompts the user for new album's credentials
		StageManager stageManager = new StageManager();
		stageManager.getStage("AddAlbum",currUser).showAndWait();
		
		displayAlbums();
	}
	
	/**
	 * Deletes the selected album from the current User
	 * @param e the ActionEvent that prompted the button
	 * @throws IOException
	 */
	public void deleteAlbum() throws IOException{
		// Get the selected album's name and check if anything was selected
		Album selectedAlbum = albumsListView.getSelectionModel().getSelectedItem();
		if (selectedAlbum == null) {
			showErrorDialog("No selected album.");
			return;
		}
				
		// Ask the user if they really want to delete the selected user
		StageManager stageManager = new StageManager();
				
		// If user confirms, copy the entire file into a temp file
		// but don't copy the line that matches the selectedUser.
		// This mimics "deleting" a user from the accounts.txt file.				
		if (stageManager.getConfirmation()) {
			try {
				// Deserialize storedUsers data
				FileInputStream fileIn = new FileInputStream("data/Data.dat");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ArrayList<User> storedUsers = (ArrayList<User>) in.readObject();
				in.close();
				fileIn.close();
						
				// Traverse storedUsers and remove selected album
				for (User u : storedUsers) {
					if (currUser.equals(u)) {
						currUser.deleteAlbum(selectedAlbum);
						storedUsers.set(storedUsers.indexOf(u), currUser);
					}
				}
						
				// Serialize updated storedUsers
				FileOutputStream fileOut = new FileOutputStream("data/Data.dat");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(storedUsers);
				out.close();
				fileOut.close();
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Class not found.");
			}
			catch (IOException ex) {
				System.out.println("Error reading file.");
			}
		}
		
		displayAlbums();

	}
		
	/**
	 * Helper method that populates the ListView with the User's albums
	 */
	private void displayAlbums () {
		ObservableList<Album> obsList = FXCollections.observableArrayList();
		
		
		// Add all of user's albums to obsList
		for (Album album : currUser.getAlbums()) {
			obsList.add(album);
		}
		
		// Sort obsList
		FXCollections.sort(obsList, AlbumComparator);
		
		// Display currUser's albums
		albumsListView.setItems(obsList);
      
		albumsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {

    	    @Override
    	    public void changed(ObservableValue<? extends Album> observable, Album oldValue, Album newValue) {
    	    	if(newValue!=null) {
    	    		albumName.setText("Album Name: \t"+"\""+newValue.getAlbumName()+"\"");
    	    		numPhotos.setText("Number of Photos: \t"+newValue.getNumberOfPhotos());
    	    		dateRange.setText("Date Range: \t"+newValue.getOldestPhotoDateString()+" - "+newValue.getNewestPhotoDateString());
    	    	}
    	    	else {
    	    		albumName.setText("");
    	    		numPhotos.setText("");
    	    		dateRange.setText("");
    	    	}
    	    }
    	});         		
		
		// Add a double-click handler so that when an album is double-clicked,
		// the Photos scene is loaded
		albumsListView.setOnMouseClicked (new EventHandler<>() {
			@Override
			public void handle (MouseEvent click) {
				Album selectedAlbum = albumsListView.getSelectionModel().getSelectedItem();
				if (click.getClickCount() == 2) {
					StageManager stageManager = new StageManager();
					try {
						stageManager.loadPhotosScene(mainStage, currUser, selectedAlbum);
					} 
					catch (IOException ex) {
						System.out.println(ex);
					}
				}
			}
		});
		albumsListView.getSelectionModel().select(0);
	}
	
}
