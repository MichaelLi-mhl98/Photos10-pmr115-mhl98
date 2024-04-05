package app;

import javafx.application.Application;
import javafx.stage.Stage;

import util.StageManager;

/**
 * Main class for the Photo Management Application.
 * Initializes the JavaFX application and starts the login scene.
 * This class extends Application, which is the entry point for JavaFX applications.
 * 
 * @author xxxx
 * @author yyyy
 */
public class Photos extends Application{
	
	/**
	 * Entry point for the JavaFX application.
	 * Starts the application by loading the login scene.
	 * 
	 * @param primaryStage The primary stage for the JavaFX application
	 * @throws Exception If an error occurs during application startup
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		StageManager stageManager = new StageManager();
		stageManager.loadScene(primaryStage, "Login");
	}
	
	/**
	 * Main method to launch the application.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}

