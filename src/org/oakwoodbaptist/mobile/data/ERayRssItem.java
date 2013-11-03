/**
 * 	01/9/2012 : Bryan Osterkamp - Initial Creation
 */
package org.oakwoodbaptist.mobile.data;

import java.util.Date;

/**
 * Data holder for ERay RSS data.  Follows RSS spec.
 * @author Bryan Osterkamp
 *
 */
public class ERayRssItem {

	private String title;
	private String description;
	private String link;
	
	/**
	 * Constructs the ERayItem on creation
	 */
	public ERayRssItem(String title, String description, 
			String link)
	{
		this.title = title;
		this.description = description;
		this.link = link;
	}
	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	
	/**
	 * Format of the BibleVerseRssItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
 
		String result = getTitle(); 
			;  
		return result;  
	}
	
}
