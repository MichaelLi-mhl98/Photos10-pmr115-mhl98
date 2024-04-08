package util;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class Representation of a Photo object
 * 
 * @author xxxx
 * @author yyyy
 */
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String photoCaption;
    private final File photoFile;
    private final Date photoDate;
    private final ArrayList<Tag> photoTags;
    
    /**
     * Constructor for a Photo Instance
     * 
     * @param file File object that contains the photo
     * @param cap  String caption
     */
    public Photo(File file, String cap) {
        this.photoCaption = cap;
        this.photoFile = file;
        this.photoDate = new Date(file.lastModified());
        this.photoTags = new ArrayList<Tag>();
    }
    
    /**
     * Constructor for a Photo Instance
     * 
     * @param file File object that contains the photo
     */
    public Photo(File file) {
        this.photoCaption = ("/data/Stock-photos/" + file.getName()).toString();
        this.photoFile = file;
        this.photoDate = new Date(file.lastModified());
        this.photoTags = new ArrayList<Tag>();
    }
    
    /**
     * Returns the caption of the photo
     * 
     * @return String caption of the photo
     */
    public String getPhotoCaption() {
        return this.photoCaption;
    }    

    /**
     * Returns the ArrayList containing the tags a photo has
     * 
     * @return ArrayList containing tags
     */
    public ArrayList<Tag> getPhotoTags() {
        return this.photoTags;
    }
    
    /**
     * Overwrites the toString method to print the photo's caption
     * 
     * @return String caption
     */
    public String toString() {
        return this.photoCaption;
    }

    /**
     * Returns the date of the photo
     * 
     * @return Data object containing the date of the photo
     */
    public Date getPhotoDate() {
        return this.photoDate;
    }
        
    /**
     * Checks to see if Tag t is contained in the photo's list of tags
     * 
     * @param t Tag to be checked
     * @return True if found, false if otherwise
     */
    public boolean doesContainTag(Tag t) {
        for (Tag tag : this.photoTags) {
            if (tag.equals(t)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes tag from ArrayList of Tags
     * 
     * @param t Tag to be removed
     */
    public void removePhotoTag(Tag t) {
        this.photoTags.remove(t);
    }

    /**
     * Adds tag to ArrayList of Tags
     * 
     * @param t Tag to be added
     */
    public void addPhotoTag(Tag t) {
        this.photoTags.add(t);
    }
    
    /**
     * Returns the path of the photo file
     * 
     * @return String path name
     */
    public String getPhotoPath() {
        return (System.getProperty("user.dir") + "/data/Stock-photos/" + this.photoFile.getName()).toString();
    }

    /**
     * Converts the Date object into a formatted string
     * 
     * @return String "MM/dd/yy"
     */
    public String getFormattedDateString() {
        return new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(this.getPhotoDate());
    }
    
    /**
     * Changes the caption of the photo to newCaption
     * 
     * @param newCaption String of the new caption
     */
    public void setPhotoCaption(String newCaption) {
        this.photoCaption = newCaption;
    }
}

