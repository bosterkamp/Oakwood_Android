/**
 * 	06/21/2011 : Bryan Osterkamp - Initial Creation
 */
package org.oakwoodbaptist.mobile.data;

import java.util.Date;

/**
 * Data holder for Bible Verse RSS data.  Follows RSS spec.
 * @author Bryan Osterkamp
 *
 */
public class BibleVerseRssItem {

	private String title;
	private String description;
	private String link;
	private String enclosureUrl;
	
	/**
	 * Constructs the SermonRssItem on creation
	 */
	public BibleVerseRssItem(String title, String description, 
			String link, String enclosureUrl)
	{
		this.title = title;
		this.description = description;
		this.link = link;
		this.enclosureUrl = enclosureUrl;
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
	 * @return the enclosureUrl
	 */
	public String getEnclosureUrl() {
		return enclosureUrl;
	}


	/**
	 * @param enclosureUrl the enclosureUrl to set
	 */
	public void setEnclosureUrl(String enclosureUrl) {
		this.enclosureUrl = enclosureUrl;
	}

	
	/**
	 * Format of the BibleVerseRssItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");  
		String result = getDescription() 
//		+ " \n "
//			+ "   ( " + sdf.format(this.getPublishDate()) + " )" + " \n " 
//			+ getDescription() 
			;  
		return result;  
	}
	
}
