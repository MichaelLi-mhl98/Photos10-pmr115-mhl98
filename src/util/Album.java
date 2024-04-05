package util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class representation of an Album Object
 * Represents a collection of photos with associated metadata.
 * Each album has a name, list of photos, and date range.
 * An Album object is serializable.
 * 
 * @author xxxx
 * @author yyyy
 *
 */
public class Album implements Serializable{
	private static final long serialVersionUID = 1L;
	private String albumName;
	private Date oldestPhotoDate = null;
	private Date newestPhotoDate = null;
	private final ArrayList<Photo> albumPhotos;
	
	/**
	 * Constructs an Album object with the given name.
	 * Initializes the list of photos.
	 * 
	 * @param name The name of the album
	 */
	public Album(String name) {
		this.albumName = name;
		albumPhotos = new ArrayList<>();
	}
	
	/**
	 * Constructs an Album object with the given name and list of photos.
	 * 
	 * @param name The name of the album
	 * @param photos The list of photos to be included in the album
	 */
	public Album(String name, ArrayList<Photo> photos) {
		this.albumName = name;
		albumPhotos = photos;
	}
	
	/**
	 * Removes a photo from the album and updates the date range.
	 * 
	 * @param photo The photo to be removed
	 */
	public void deletePhoto(Photo photo) {
		if(albumPhotos.contains(photo)) {
			albumPhotos.remove(photo);
			updatePhotoDates();
		}
	}
	
	/**
	 * Adds a photo to the album and updates the date range.
	 * 
	 * @param photo The photo to be added
	 */
	public void addPhoto(Photo photo) {
		albumPhotos.add(photo);
		if (oldestPhotoDate == null || photo.getPhotoDate().compareTo(oldestPhotoDate) < 0)
			oldestPhotoDate = photo.getPhotoDate();
	
		if (newestPhotoDate == null || photo.getPhotoDate().compareTo(newestPhotoDate) > 0)
			newestPhotoDate = photo.getPhotoDate();
	}
	
	/**
	 * Updates the date range of the album.
	 * 
	 * Iterates over all photos in the album to find the oldest and newest dates.
	 */
	public void updatePhotoDates() {
		if(albumPhotos.isEmpty()) {
			this.newestPhotoDate = null;
			this.oldestPhotoDate = null;
		} else {
			for(Photo photo : albumPhotos) {
				if (oldestPhotoDate == null || photo.getPhotoDate().compareTo(oldestPhotoDate) < 0)
					oldestPhotoDate = photo.getPhotoDate();
			
				if (newestPhotoDate == null || photo.getPhotoDate().compareTo(newestPhotoDate) > 0)
					newestPhotoDate = photo.getPhotoDate();
			}
		}
	}
	
	/**
	 * Sets the name of the album.
	 * 
	 * @param name The new name of the album
	 */
	public void setAlbumName(String name) {
		this.albumName = name;
	}
	
	/**
	 * Returns the list of photos in the album.
	 * 
	 * @return The list of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return this.albumPhotos;
	}
	
	/**
	 * Returns the number of photos in the album.
	 * 
	 * @return The number of photos
	 */
	public int getNumberOfPhotos() {
		return albumPhotos.size();
	}
		
	/**
	 * Returns the string representation of the album (its name).
	 * 
	 * @return The name of the album
	 */
	public String toString() {
		return this.albumName;
	}
	
	/**
	 * Returns the name of the album.
	 * 
	 * @return The name of the album
	 */
	public String getAlbumName() {
		return this.albumName;
	}

	/**
	 * Returns the string representation of the newest date in the album.
	 * 
	 * @return The newest date in "MM/dd/yy" format, or an empty string if there are no photos
	 */
	public String getNewestPhotoDateString(){
		if (this.newestPhotoDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yy").format(this.newestPhotoDate);
	}
		
	/**
	 * Returns the string representation of the oldest date in the album.
	 * 
	 * @return The oldest date in "MM/dd/yy" format, or an empty string if there are no photos
	 */
	public String getOldestPhotoDateString(){
		if (this.oldestPhotoDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yy").format(this.oldestPhotoDate);
	}	
}

