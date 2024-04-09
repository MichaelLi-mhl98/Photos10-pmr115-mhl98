package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.Album;
import util.Controller;
import util.Photo;
import util.StageManager;
import util.Tag;
import util.User;

/** 
 * Controls the "Photos" stage
 * Manages photo-related actions such as adding, deleting, and editing photos
 * in the selected album.
 * @author xxxx
 * @author yyyy
 */
public class PhotosController extends Controller {
    private User currUser;
    private Album album;
    private ObservableList<Tag> tagsObsList;
    
    @FXML ListView<Photo> photosListView;
    @FXML TitledPane photosTitledPane;
    @FXML ImageView selectedImageView;
    @FXML TextField captionTextField;
    @FXML TextField dateTakenTextField;
    @FXML ListView<Tag> tagsListView;
    
    StageManager stageManager = new StageManager();
    
    /**
     * Initializes controller's private fields and sets up controller
     * for stage
     * @param primaryStage The Stage that this controller controls
     * @param currUser The current User accessing this stage
     * @param selectedAlbum The album that the User is viewing
     */
    public void start (Stage primaryStage, User currUser, Album selectedAlbum) {
        this.mainStage = primaryStage;
        this.currUser = currUser;
        this.album = selectedAlbum;
        
        photosTitledPane.setText("Photos in " + selectedAlbum.getAlbumName());
        displayPhotos();
    }
	
	/**
	 * Loads the "Add_Photo" stage so the User can add a photo
	 * @throws IOException if an I/O error occurs
	 */
	public void addPhoto () throws IOException {
		stageManager.getAddPhotoStage(currUser, album).showAndWait();
		displayPhotos();
	}
	
	/**
	 * Loads the "Copy_Photo" stage so the User can copy photos
	 * @throws IOException if an I/O error occurs
	 */
	public void copyPhoto () throws IOException {
		Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		stageManager.getCopyPhotoStage(currUser, album, selectedPhoto).showAndWait();
	}

	/**
	 * Loads the "Move_Photo" stage so the User can move photos
	 * @throws IOException if an I/O error occurs
	 */
	public void movePhoto () throws IOException {
		Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		stageManager.getMovePhotoStage(currUser, album, selectedPhoto).showAndWait();
		displayPhotos();
	}	

	/**
	 * Prompts the User if they want to delete the selected photo, and if the User accepts, delete the photo
	 * @throws IOException if an I/O error occurs
	 */
	public void deletePhoto () throws IOException {
		Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		if (selectedPhoto == null) {
		    return;
		}
		
		if (stageManager.getConfirmation()) {
		    album.deletePhoto(selectedPhoto);
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
		                storedUsers.set(storedUsers.indexOf(u), currUser);
		            }
		        }
		                
		        // Serialize updated storedUsers
		        FileOutputStream fileOut = new FileOutputStream("data/Data.dat");
		        ObjectOutputStream out = new ObjectOutputStream(fileOut);
		        out.writeObject(storedUsers);
		        out.close();
		        fileOut.close();
		        
		        displayPhotos();
		    }
		    catch (ClassNotFoundException ex) {
		        System.out.println("Class not found.");
		    }
		    catch (IOException ex) {
		        System.out.println("Error reading file.");
		    }           
		    
		}
		
	}

	/**
	 * Returns the User to the "Albums" stage
	 * @throws IOException if an I/O error occurs
	 */
	public void goBack () throws IOException {
		stageManager.loadScene(mainStage, "Albums", currUser);
	}

	/**
	 * Loads the "Edit_Caption" stage so the User can edit a photo's caption
	 * @throws IOException if an I/O error occurs
	 */
	public void editCaption () throws IOException {
		Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		stageManager.getEditCaptionStage(currUser, selectedPhoto).showAndWait();
		displayPhotos();
	}
	
	/**
	 * Prompts the User if they want to delete the selected Tag, and if the User accepts, delete the Tag
	 * @throws IOException if an I/O error occurs
	 */
	public void deleteTag() throws IOException {
		if (stageManager.getConfirmation()) {
		    Tag tag = tagsListView.getSelectionModel().getSelectedItem();
		    if (tag != null) {
		        Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		        selectedPhoto.removePhotoTag(tag);
		        currUser.saveUser();
		    }
		}
		displayPhotos();
	}	

	/**
	 * Loads the "Add_Tag" stage so the User can add a Tag to a photo's caption
	 * @throws IOException if an I/O error occurs
	 */
	public void addTag () throws IOException {
		Photo selectedPhoto = photosListView.getSelectionModel().getSelectedItem();
		stageManager.getAddTagStage(currUser, selectedPhoto).showAndWait();
		displayPhotos();
	}

	/**
	 * Helper method that populates the ListView with the current User's photos
	 */
	private void displayPhotos () {
		ArrayList<Photo> photos = album.getPhotos();
		ObservableList<Photo> obsList = FXCollections.observableArrayList();
		
		for (Photo photo : photos) {
		    obsList.add(photo);
		}
		
		photosListView.setItems(obsList);
		photosListView.setCellFactory(param -> new ListCell<Photo>() {
		    private final ImageView imageView = new ImageView();
		    @Override
		    public void updateItem (Photo photo, boolean empty) {
		        super.updateItem(photo, empty);
		        if (empty) {
		            setText (null);
		            setGraphic(null);
		        }
		        else {
		            String path = "file:///" + photo.getPhotoPath();
		            Image image = new Image(path, 50, 50, true, true);
		            imageView.setImage(image);
		            setGraphic(imageView);
		        }
		    }
		});
		photosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
		    @Override
		    public void changed(ObservableValue<? extends Photo> obsList, Photo oldPhoto, Photo newPhoto) {
		        if(newPhoto!=null) {
		            String path = "file:///" + newPhoto.getPhotoPath();
					System.out.println(path);
		            Image image = new Image(path, true);
		            selectedImageView.setImage(image);
		            
		            captionTextField.setText(newPhoto.getPhotoCaption());
		            dateTakenTextField.setText(newPhoto.getFormattedDateString());
		            tagsObsList = FXCollections.observableArrayList();
		            for (Tag tag : newPhoto.getPhotoTags()) {
		                tagsObsList.add(tag);
		            }
		            tagsListView.setItems(tagsObsList);
		            tagsListView.setCellFactory(param -> new ListCell<Tag>() {
		                @Override
		                public void updateItem (Tag tag, boolean empty) {
		                    super.updateItem(tag, empty);
		                    if (empty) {
		                        setText(null);
		                    }
		                    else {
		                        setText(tag.toString());
		                    }
		                }
		            });
		        }
		    }
		});     
	}
	
	/**
	 * Selects the next Photo in the ListView
	 * @throws IOException if an I/O error occurs
	 */
	public void nextPhoto() {
		int index = photosListView.getSelectionModel().getSelectedIndex();
		photosListView.getSelectionModel().select(index+1);
	}

	/**
	 * Selects the previous Photo in the ListView
	 * @throws IOException if an I/O error occurs
	 */
	public void prevPhoto() {
		int index = photosListView.getSelectionModel().getSelectedIndex();
		if (index-1 >=0) {
		    photosListView.getSelectionModel().select(index-1);
		}
	}	

}
