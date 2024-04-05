package util;

import java.io.Serializable;

/**
 * Represents a tag associated with a photo, consisting of a type and a value.
 * Instances of this class are Serializable.
 *
 * @author xxxx
 * @author yyyy
 */
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;
	private String tagType;
	private String tagValue;

	/**
	 * Constructs a Tag object with the given type and value.
	 *
	 * @param type  The type of the tag.
	 * @param value The value of the tag.
	 */
	public Tag(String type, String value) {
		this.tagType = type;
		this.tagValue = value;
	}

	/**
	 * Retrieves the type of the tag.
	 *
	 * @return The type of the tag.
	 */
	public String getType() {
		return this.tagType;
	}

	/**
	 * Retrieves the value of the tag.
	 *
	 * @return The value of the tag.
	 */
	public String getValue() {
		return this.tagValue;
	}

	/**
	 * Returns a string representation of the tag in the format "type : value".
	 *
	 * @return A string representation of the tag.
	 */
	public String toString() {
		return this.tagType + " : " + this.tagValue;
	}

	/**
	 * Compares this tag to another tag for equality.
	 * Two tags are considered equal if their types and values match.
	 *
	 * @param otherTag The tag to compare.
	 * @return True if the tags are equal, false otherwise.
	 */
	public boolean equals(Tag otherTag) {
		if (!(otherTag instanceof Tag)) {
			return false;
		}
		if (otherTag.getType().isEmpty()) {
			if (this.tagValue.toLowerCase().trim().equals(otherTag.getValue().toLowerCase().trim())) {
				return true;
			}
		}
		if (otherTag.getValue().isEmpty()) {
			if (this.tagType.toLowerCase().trim().equals(otherTag.getType().toLowerCase().trim())) {
				return true;
			}
		}
        return this.tagValue.toLowerCase().trim().equals(otherTag.getValue().toLowerCase().trim())
                && this.tagType.toLowerCase().trim().equals(otherTag.getType().toLowerCase().trim());
    }

}
