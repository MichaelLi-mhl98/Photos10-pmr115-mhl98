package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user in the photo application.
 * Each user has a username, an account type, and a list of albums.
 * Implements Serializable to allow for object serialization.
 * @author xxxx
 * @author yyyy
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String userName;
    private final String accountType;
    public final ArrayList<Album> albums;

    /**
     * Constructs a User object with the given username and account type.
     * Initializes the list of albums.
     *
     * @param userName    The username of the user.
     * @param accountType The type of the user's account.
     */
    public User(String userName, String accountType) {
        this.userName = userName;
        this.accountType = accountType;
        this.albums = new ArrayList<>();
    }

    /**
     * Adds an album to the user's list of albums.
     *
     * @param newAlbum The album to be added.
     */
    public void addAlbums(Album newAlbum) {
        System.out.println("adding: " + newAlbum.getAlbumName() + " to: " + albums);
        this.albums.add(newAlbum);
    }
    
    /**
     * Retrieves the list of albums associated with the user.
     *
     * @return The list of albums associated with the user.
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }
    
    /**
     * Retrieves the account type of the user.
     *
     * @return The account type of the user.
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Deletes an album from the user's list of albums.
     *
     * @param albumToDelete The album to be deleted.
     */
    public void deleteAlbum(Album albumToDelete) {
        this.albums.remove(albumToDelete);
    }

    /**
     * Checks if this User object is equal to another User object.
     * Two User objects are considered equal if they have the same username.
     *
     * @param otherUser The User object to compare.
     * @return True if the User objects are equal, false otherwise.
     */
    public boolean equals(User otherUser) {
        if (otherUser == null) return false;
        return this.userName.equals(otherUser.getUserName());
    }

    /**
     * Saves the user's data by serializing the list of users and updating the stored data.
     * The serialized data is stored in the file "data/Data.dat".
     */
    public void saveUser() {
        try {
            // Deserialize storedUsers data
            FileInputStream fileIn = new FileInputStream("data/Data.dat");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);
            ArrayList<User> storedUsers = (ArrayList<User>) inStream.readObject();
            inStream.close();
            fileIn.close();

            // Traverse storedUsers and save this User
            for (User usr : storedUsers) {
                if (this.equals(usr)) {
                    storedUsers.set(storedUsers.indexOf(usr), this);
                }
            }

            // Serialize updated storedUsers
            FileOutputStream fileOut = new FileOutputStream("data/Data.dat");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(storedUsers);
            outStream.close();
            fileOut.close();
        } catch (ClassNotFoundException exception) {
            System.out.println("Class not found.");
        } catch (IOException exception) {
            System.out.println("Error reading file.");
        }
    }
}


