package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import util.User;

/** 
 * Controls the "Edit_Album" stage
 * This class handles editing an existing album's name.
 * It allows the user to input a new name for the album and saves the changes.
 * If the user tries to rename the album to an existing name, it displays an error message.
 * It also updates the stored user data file with the changes made to the album.
 * 
 * @author xxxx
 * @author yyyy
 */
public class Edit_Album extends Controller{
    private User currUser;
    private Album selectedAlbum;
    
    @FXML TextField albumNameTextField;
    
    /**
     * Initializes controller's private fields and sets up controller for stage
     * 
     * @param primaryStage The Stage that this controller controls
     * @param currUser The current User accessing this stage
     * @param selectedAlbum The album that will be edited
     */
    public void start (Stage primaryStage, User currUser, Album selectedAlbum) {
        this.mainStage = primaryStage;
        this.currUser = currUser;
        this.selectedAlbum = selectedAlbum;
    }
    
    /**
     * Edits the album's name based on the new input provided by the user.
     * If the album name is already taken, it displays an error message.
     * Updates the stored user data file with the changes made to the album.
     */
    public void editAlbum () {
        String albumName = albumNameTextField.getText();
        selectedAlbum.setAlbumName(albumName);
        for (Album album : currUser.getAlbums()) {
            if (album.getAlbumName().equals(albumName)) {
                showErrorDialog("Cannot have two albums with the same name.");
            }
        }

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
        catch (ClassNotFoundException | FileNotFoundException ex) {
            System.out.println(ex);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }

        closeCurrentWindow();
    }
}

