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
 * Controls the "Copy_Photo" stage
 * This class handles copying a photo from one album to another.
 * It allows the user to select an album to copy the photo into and performs the copy operation.
 * The class also updates the stored user data file with the changes made to the albums.
 * 
 * @author xxxx
 * @author yyyy
 */
public class Copy_Photo extends Controller{
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
     * 
     * @param primaryStage The Stage that this controller controls
     * @param currUser The current User accessing this stage
     * @param album The album that the photo is being copied from
     * @param selectedPhoto The photo that will be copied
     */
    public void start (Stage primaryStage, User currUser, Album album, Photo selectedPhoto) {
        this.mainStage = primaryStage;
        this.currUser = currUser;
        this.album = album;
        this.selectedPhoto = selectedPhoto;
        
        displayAlbums();
    }
    
    /**
     * Copies the selected photo over to the user's selected album.
     * Updates the stored user data file with the changes made to the albums.
     */
    public void copyPhoto () {
        Album selectedAlbum = albumsListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum == null) {
            showErrorDialog("No selected album.");
            return;
        }
        
        selectedAlbum.addPhoto(selectedPhoto);
        
        try {
            FileInputStream fileIn = new FileInputStream("data/Data.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<User> storedUsers = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
                    
            for (User u : storedUsers) {
                if (currUser.equals(u)) {
                    storedUsers.set(storedUsers.indexOf(u), currUser);
                }
            }
                    
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
     * Helper method that populates the ListView with the user's albums
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

