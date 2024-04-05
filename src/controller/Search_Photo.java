package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
 * Controls the "Search_Photo" stage
 * Handles user interactions and search functionalities
 * Displays search results based on user input parameters
 * Manages navigation between different stages
 * 
 * @author xxxx
 * @author yyyy
 */
public class Search_Photo extends Controller {
    private User currUser;

    private ObservableList < Photo > obsList;
    private ObservableList < Tag > tagsObsList;
    private final ObservableList < Tag > paraObsList = FXCollections.observableArrayList();
    StageManager stageManager = new StageManager();

    @FXML ListView < Photo > display;
    @FXML ListView < Tag > tagListView;
    @FXML ImageView image_view;
    @FXML TextField caption;
    @FXML TextField date;

    @FXML TextField start_date;
    @FXML TextField end_date;
    @FXML TextField tag_type;
    @FXML TextField tag_value;
    @FXML Button search_date;
    @FXML Button create_album;
    @FXML Button back;
    @FXML Button search_tag;
    @FXML Button remove;

    /**
     * Initializes controller's private fields and sets up controller
     * for stage
     * @param primaryStage is the Stage that this controller controls
     * @param currUser is the current User that's accessing this stage
     */
    public void start(Stage primaryStage, User user) {
        this.mainStage = primaryStage;
        this.currUser = user;
        DisplaySearchResults();
    }

    /**
     * Handles the action of pressing the back button
     * Takes the User back to the "Albums" stage
     * 
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void backButton() throws IOException {
        stageManager.loadScene(mainStage, "Albums", currUser);
    }

    /**
     * Handles the action of pressing the search date button
     * Populates the ListView based on the User's search parameters regarding date
     * 
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void searchDateButton() {
        obsList = FXCollections.observableArrayList();
        SimpleDateFormat f = new SimpleDateFormat("mm/dd/yy");
        Date startD = null;
        Date endD = null;

        if (start_date.getText().isEmpty() || end_date.getText().isEmpty()) {
            return;
        }
        try {
            startD = f.parse(start_date.getText());
            endD = f.parse(end_date.getText());
        } catch (ParseException e1) {
			showErrorDialog("Error: Wrong Format\nRequired: MM/DD/YY");
            return;
        }

        if (startD != null && endD != null) {
            if (startD.compareTo(endD) > 0 || startD.equals(endD)) {
				showErrorDialog("Error: Start Date must be Prior to End Date");
                return;
            }
            for (Album i: currUser.getAlbums()) {
                for (Photo p: i.getPhotos()) {
                    if (p.getPhotoDate().compareTo(startD) >= 0 && p.getPhotoDate().compareTo(endD) <= 0)
                        obsList.add(p);
                }
            }
            if (obsList.isEmpty()) {
				showErrorDialog("No Photos within specified date range");
            } else {
                DisplaySearchResults();
            }
        }
    }

	/**
	 * Handles the action of pressing the search tag button
	 * Populates the ListView based on the User's search parameters regarding tags
	 * 
	 * @param e the ActionEvent that prompted the button
	 * @throws IOException
	 */
	public void searchTagButton() {
		obsList = FXCollections.observableArrayList();
		String t = tag_type.getText();
		String v = tag_value.getText();
        clearSearch();
		if (t.isEmpty() && v.isEmpty()) {
		    return;
		}
		Tag tag = new Tag(t, v);
		if (!paraObsList.contains(tag)) {
		    paraObsList.add(tag);
		    System.out.println(paraObsList);
		}

		for (Album i: currUser.getAlbums()) {
		    for (Photo p: i.getPhotos()) {
		        if (p.doesContainTag(paraObsList.get(0))) {
		            obsList.add(p);
		        }
		    }
		}

		while (paraObsList.size() > 1) {
		    for (int i = 1; i < paraObsList.size(); i++) {
		        for (Photo p: obsList) {
		            if (!p.doesContainTag(paraObsList.get(i))) {
		                obsList.remove(p);
		            }
		        }
		    }
		}

		if (obsList.isEmpty()) {
			showErrorDialog("No Photos containing specified tag");
		} else {
		    DisplaySearchResults();
		}
	}


    /**
     * Handles the action of pressing the create album button
     * Creates an album based on the Photo search results
     * 
     * @param e the ActionEvent that prompted the button
     * @throws IOException
     */
    public void createAlbum() throws IOException {
        ArrayList < Photo > result = new ArrayList < Photo > ();
        if (obsList == null)
            return;
        for (Photo p: obsList) {
            result.add(p);
        }

        stageManager.getStage("AddAlbum", currUser, result).showAndWait();
    }

    /**
     * Updates the listview with the images that match the search parameters
     */
    private void DisplaySearchResults() {
        display.setItems(obsList);

        display.setCellFactory(param -> new ListCell < Photo > () {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String path = "file:///" + photo.getPhotoPath();
                    Image image = new Image(path, 50, 50, true, true);
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });
		
		display.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Photo> obsList, Photo oldPhoto, Photo newPhoto) {
    	    	if(newPhoto!=null) {
    	    		String path = "file:///" + newPhoto.getPhotoPath();
    	    		Image image = new Image(path, true);
    	    		image_view.setImage(image);
    	    		
    	    		caption.setText(newPhoto.getPhotoCaption());
    	    		date.setText(newPhoto.getFormattedDateString());
    	    		tagsObsList = FXCollections.observableArrayList();
    	    		for (Tag tag : newPhoto.getPhotoTags()) {
    	    			tagsObsList.add(tag);
    	    		}
    	    		tagListView.setCellFactory(param -> new ListCell<Tag>() {
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
	 * Clears all search parameters and resets search results.
	 */
	public void clearSearch() {
		start_date.clear();
		end_date.clear();
		tag_type.clear();
		tag_value.clear();
		obsList.clear();
		paraObsList.clear();
		DisplaySearchResults(); // Refresh display to show cleared results
		image_view.setImage(null); // Clear the image displayed in the ImageView
	}

	public void clearButtonAction() {
		clearSearch();
	}

}

