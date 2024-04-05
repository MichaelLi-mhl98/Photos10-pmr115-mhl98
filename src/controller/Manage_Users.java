package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Controller;
import util.StageManager;
import util.User;

/**
 * Controls the "Manage_Users" stage
 *
 * @author xxxx
 * @author yyyy
 */
public class Manage_Users extends Controller {

    @FXML ListView<User> usersListView;
    @FXML Button addButton;
    @FXML Button deleteButton;
    @FXML Button logOutButton;
    @FXML Button quitButton;

    /**
     * Initializes controller's private fields and sets up controller for stage
     *
     * @param primaryStage is the Stage that this controller controls
     */
    public void start(Stage primaryStage) {
        displayUsers();
        this.mainStage = primaryStage;
    }

    /**
     * Opens up the "AddUser" stage
     *
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void addUser() throws IOException {
        StageManager stageManager = new StageManager();
        stageManager.getStage("AddUser").showAndWait();

        displayUsers();
    }

    /**
     * Deletes the selectedUser
     *
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void deleteUser() throws IOException {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("nothing was selected.");
            return;
        }

        StageManager stageManager = new StageManager();

        if (stageManager.getConfirmation()) {
            try {
                FileInputStream fileIn = new FileInputStream("data/Data.dat");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                ArrayList<User> storedUsers = (ArrayList<User>) in.readObject();
                in.close();
                fileIn.close();

                for (User user : storedUsers) {
                    if (user.getUserName().equals(selectedUser.getUserName())) {
                        storedUsers.remove(user);
                        break;
                    }
                }

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
        }

        displayUsers();
    }

    /**
     * Returns the User to the "Login" stage
     *
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void logOut() throws IOException {
        StageManager stageManager = new StageManager();
        stageManager.loadScene(mainStage, "Login");
    }

    /**
     * Helper method that populates the ListView with all available Users
     */
    private void displayUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            FileInputStream fileIn = new FileInputStream("data/Data.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<User> storedUsers = (ArrayList<User>) in.readObject();
            for (User user : storedUsers) {
                if (user.getAccountType().equals("user")) {
                    users.add(user);
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        } catch (IOException ex) {
            System.out.println("Error reading file.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }

        usersListView.setItems(users);
        usersListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> p) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean bln) {
                        super.updateItem(user, bln);
                        if (user != null) {
                            setText(user.getUserName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }

        });
    }
}
