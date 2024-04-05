package controller;

import java.io.*;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import util.Controller;
import util.StageManager;
import util.User;

/** 
 * Controls the "Login" stage
 * This controller manages the login functionality. It checks the user credentials
 * and directs the user to the appropriate stage based on their account type.
 * If the credentials are invalid, it displays an error message.
 * @author xxxx
 * @author yyyy
 */
public class LoginController extends Controller{
    @FXML Button quitButton;
    @FXML TextField userNameTextField;
    

    /**
     * Initializes controller's private fields and sets up controller
     * for stage
     * @param primaryStage is the Stage that this controller controls
     */
    public void start(Stage primaryStage) {
        // This is so LoginConroller knows what the primaryStage is
        // Not too sure if this is 'hacky' :p
        this.mainStage = primaryStage;
    }
    
    /**
     * Checks to see if the User inputted valid credentials and loads the appropriate stage
     * @param e the ActionEvent that prompted the button 
     */
    public void checkSignIn ()  {
        // Get username combo from TextFields
        String userName = userNameTextField.getText();
        
        // Parse through user data in "accounts.txt" and see if there are any matches
        try {
            FileInputStream fileIn = new FileInputStream("data/Data.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<User> users = (ArrayList<User>) in.readObject();
            boolean validUser = false;
            for (User user : users) {
                if (user.getUserName().equals(userName) ) {
                    if (user.getAccountType().equals("user")) {
                        StageManager stageManager = new StageManager();
                        stageManager.loadScene(mainStage, "Albums", user);
                        validUser = true;
                    }
                    else if (user.getAccountType().equals("admin")) {
                        StageManager stageManager = new StageManager();
                        stageManager.loadScene(mainStage, "ManageUsers");
                        validUser = true;
                        break;
                    }
                }
            }
            if (!validUser) {
                showErrorDialog("Invalid username");
            }
            in.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

