package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import util.Controller;
import util.User;

/** 
 * Controls the "Add_User" stage
 * This class manages the addition of a new user.
 * It allows the user to input a username and adds the user to the system.
 * @author xxxx
 * @author yyyy
 */
public class Add_User extends Controller {
	@FXML Button addUserButton;
	@FXML Button cancelButton;
	@FXML TextField userNameTextField;
	
	/**
	 * Initializes controller's private fields and sets up controller
	 * for stage
	 * @param primaryStage is the Stage that this controller controls
	 */
	public void start (Stage primaryStage) {
		this.mainStage = primaryStage;
	}
	
	/**
	 * Adds a User based off of the User's inputed userName
	 * @throws IOException
	 */
	public void addUser() {
		String userName = userNameTextField.getText();
		
		// If the userName textfields are populated, write
		// the credentials into the accounts.txt file
		if (!userName.isEmpty()) {		
			try {
				// Deserialize storedUsers data and add new User
				FileInputStream fileIn = new FileInputStream("data/Data.dat");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ArrayList<User> storedUsers = (ArrayList<User>) in.readObject();
				in.close();
				fileIn.close();
				storedUsers.add(new User(userName, "user"));
				
				// Serialize updated storedUsers
				FileOutputStream fileOut = new FileOutputStream("data/Data.dat");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(storedUsers);
				out.close();
				fileOut.close();
				closeCurrentWindow();
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Class not found.");
			}
			catch (IOException ex) {
				System.out.println("Error reading file.");
			}
		}
		else {
			showErrorDialog("Please provide inputs for both fields");
		}
	}
	
}

