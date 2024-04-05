package util;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.Add_Photo;
import controller.Add_Tag;
import controller.Copy_Photo;
import controller.Edit_Caption;
import controller.Move_Photo;
import controller.PhotosController;

/**
 * Manages the creation of JavaFX stages dynamically.
 * This class assists in creating stages for various scenes in the application.
 * 
 * @author xxxx
 * @author yyyy
 */
public class StageManager {
	
	private boolean answer = false;
	
	/**
	 * Creates stage based off of input String sceneName for specific User user
	 * @param sceneName name of fxml and respective controller
	 * @return new Stage
	 * @throws IOException
	 */
	public Stage getStage (String sceneName) throws IOException {
		// Set up controller
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/" + sceneName +".fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
				
		Stage secondaryStage = new Stage();
		controller.start(secondaryStage);
								
		// Set up secondaryStage
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false);
				
		return secondaryStage;
	}
	
	/**
	 * Creates a stage based on the specified scene name for the given user.
	 * 
	 * @param sceneName The name of the FXML file and its respective controller.
	 * @param user      The current user signed in.
	 * @return A new Stage.
	 * @throws IOException If an I/O error occurs.
	 */
	public Stage getStage(String sceneName, User user) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/" + sceneName + ".fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		
		Stage secondaryStage = new Stage();
		controller.start(secondaryStage, user);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false);
		
		return secondaryStage;
	}
	
	/**
	 * Creates a stage based on the specified scene name for the given user and list of photos.
	 * 
	 * @param sceneName The name of the FXML file and its respective controller.
	 * @param user      The current user signed in.
	 * @param result    ArrayList of photos to be passed to the controller.
	 * @return A new Stage.
	 * @throws IOException If an I/O error occurs.
	 */
	public Stage getStage(String sceneName, User user, ArrayList<Photo> result) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/" + sceneName + ".fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		
		Stage secondaryStage = new Stage();
		controller.start(secondaryStage, user, result);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false);
		
		return secondaryStage;
	}		
	
	/**
	 * Creates stage based off of input String sceneName for specific User user when Adding a photo to album
	 * @param sceneName name of fxml and respective controller
	 * @param album Album being passed to Add_PhotoController
	 * @return new AddPhotoStage
	 * @throws IOException
	 */
	public Stage getAddPhotoStage (User currUser, Album album) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddPhoto.fxml"));
		Parent root = loader.load();
		Add_Photo addPhotoController = loader.getController();
		
		Stage secondaryStage = new Stage();
		addPhotoController.start(secondaryStage, currUser, album);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		
		return secondaryStage;
	}

	/**
	 * Loads a scene onto a stage with the specified scene name.
	 * 
	 * @param primaryStage The primary stage to load the scene onto.
	 * @param sceneName    The name of the FXML file and its respective controller.
	 * @throws IOException If an I/O error occurs.
	 */
	public void loadScene(Stage primaryStage, String sceneName) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/" + sceneName + ".fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		controller.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo App");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
		
	/**
	 * Loads a scene onto a stage with the specified scene name and user.
	 * 
	 * @param primaryStage The primary stage to load the scene onto.
	 * @param sceneName    The name of the FXML file and its respective controller.
	 * @param user         The current user signed in.
	 * @throws IOException If an I/O error occurs.
	 */
	public void loadScene(Stage primaryStage, String sceneName, User user) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/" + sceneName + ".fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		controller.start(primaryStage, user);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo App");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
		
	/**
	 * Displays a confirmation dialog box and returns the user's choice.
	 * 
	 * @return True if the user confirms, false otherwise.
	 * @throws IOException If an I/O error occurs.
	 */
	public boolean getConfirmation() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Delete.fxml"));
        Parent root = loader.load();
        Stage deleteWindow = new Stage();
        
        Scene scene = new Scene(root);
        deleteWindow.setScene(scene);
        deleteWindow.setResizable(false);
        
        Button yesButton = (Button) scene.lookup("#yesButton");
        Button noButton = (Button) scene.lookup("#noButton");
        
        yesButton.setOnAction(e -> {
            answer = true;
            deleteWindow.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            deleteWindow.close();
        });
        deleteWindow.showAndWait();
        return answer;
	}

	/**
	 * Creates a stage for editing the caption of a selected photo.
	 * 
	 * @param currUser      The current user signed in.
	 * @param selectedPhoto The photo whose caption will be edited.
	 * @return A new Stage.
	 * @throws IOException If an I/O error occurs.
	 */
	public Stage getEditCaptionStage(User currUser, Photo selectedPhoto) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/EditCaption.fxml"));
		Parent root = loader.load();
		Edit_Caption editCaptionController = loader.getController();
		
		Stage secondaryStage = new Stage();
		editCaptionController.start(secondaryStage, currUser, selectedPhoto);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		
		return secondaryStage;
	}
	
	/**
	 * Creates stage based off of input String sceneName for specific User user when copying photo
	 * @param sceneName name of fxml and respective controller
	 * @param album original photo is located
	 * @param selectedPhoto Photo to be moved
	 * @return new MovePhotoStage
	 * @throws IOException
	 */
	public Stage getMovePhotoStage(User currUser, Album album, Photo selectedPhoto) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/MovePhoto.fxml"));
		Parent root = loader.load();
		Move_Photo movePhotoController = loader.getController();
		
		Stage secondaryStage = new Stage();
		movePhotoController.start(secondaryStage, currUser, album, selectedPhoto);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		
		return secondaryStage;
	}

	/**
	 * Creates a stage for adding a tag to a selected photo.
	 * 
	 * @param currUser      The current user signed in.
	 * @param selectedPhoto The photo to which the tag will be added.
	 * @return A new Stage.
	 * @throws IOException If an I/O error occurs.
	 */
	public Stage getAddTagStage(User currUser, Photo selectedPhoto) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddTag.fxml"));
		Parent root = loader.load();
		Add_Tag addTagController = loader.getController();
		
		Stage secondaryStage = new Stage();
		addTagController.start(secondaryStage, currUser, selectedPhoto);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		
		return secondaryStage;
	}

	/**
	 * Creates stage based off of input String sceneName for specific User user when copying photo
	 * @param sceneName name of fxml and respective controller
	 * @param album original photo is located
	 * @param selectedPhoto Photo to be copied
	 * @return new CopyPhotoStage
	 * @throws IOException
	 */
	public Stage getCopyPhotoStage(User currUser, Album album, Photo selectedPhoto) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/CopyPhoto.fxml"));
		Parent root = loader.load();
		Copy_Photo copyPhotoController = loader.getController();
		
		Stage secondaryStage = new Stage();
		copyPhotoController.start(secondaryStage, currUser, album, selectedPhoto);
		
		Scene scene = new Scene(root);
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Photo App");
		secondaryStage.setResizable(false); 
		
		return secondaryStage;
	}

	/**
	 * Loads the photos scene onto a stage for the specified user and selected album.
	 * 
	 * @param primaryStage   The primary stage to load the scene onto.
	 * @param currUser       The current user signed in.
	 * @param selectedAlbum  The album selected by the user.
	 * @throws IOException  If an I/O error occurs.
	 */
	public void loadPhotosScene(Stage primaryStage, User currUser, Album selectedAlbum) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Photos.fxml"));
		Parent root = loader.load();
		PhotosController photosController = loader.getController();
		photosController.start(primaryStage, currUser, selectedAlbum);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo App");
		primaryStage.setResizable(false);
		primaryStage.show();
	}		
}
