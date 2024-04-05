package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.Album;
import util.Controller;
import util.Photo;
import util.User;

/** 
 * Controls the "Move_Photo" stage
 * This controller facilitates the moving of a photo from one album to another.
 * It displays a list of albums belonging to the user and allows selecting one to move the photo.
 * Once a destination album is selected, the photo is moved from its current album to the chosen one.
 * @author xxxx
 * @author yyyy
 */
public class Move_Photo extends Controller{
    private User currUser;
    private Album album;
    private Photo selectedPhoto;

    @FXML ListView<Album> albumsListView;
    
    public static final Comparator<Album> AlbumComparator = new Comparator<Album>() {
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
     * @param primaryStage the Stage that this controller controls
     * @param currUser the current User that's accessing this stage
     * @param album the album that the photo is being moved from
     * @param selectedPhoto the photo that will be moved
     */
    public void start (Stage primaryStage, User currUser, Album album, Photo selectedPhoto) {
        this.mainStage = primaryStage;
        this.currUser = currUser;
        this.album = album;
        this.selectedPhoto = selectedPhoto;
        
        displayAlbums();
    }
    
    /**
     * Moves the selected photo to the user's selected album.
     * It deletes the photo from its current album and adds it to the chosen one.
     */
    public void movePhoto () {
        Album selectedAlbum = albumsListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum == null) {
            return;
        }
        
        album.deletePhoto(selectedPhoto);
        selectedAlbum.addPhoto(selectedPhoto);
        
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
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        }
        catch (IOException ex) {
            System.out.println("Error reading file.");
        }

        closeCurrentWindow();
    }
    
    /**
     * Helper method that populates the ListView with the user's albums.
     * Excludes the current album from the list.
     */
    private void displayAlbums () {
        ObservableList<Album> obsList = FXCollections.observableArrayList();
        for (Album album : currUser.getAlbums()) {
            if (album != this.album) {
                obsList.add(album);
            }
        }
        
        // Sort obsList
        FXCollections.sort(obsList, AlbumComparator);
        
        albumsListView.setItems(obsList);
        albumsListView.setCellFactory(param -> new ListCell<Album>() {
            @Override
            public void updateItem (Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty) {
                    setText (null);
                }
                else {
                    setText (album.getAlbumName());
                }
            }
        });     
    }
}

