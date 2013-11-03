/**
 * 	02/17/2011 : Bryan Osterkamp - Initial Creation
 *  01/13/2012 : Bryan Osterkamp - Added totalDuration.
 */
package org.oakwoodbaptist.mobile.data;

import java.util.Date;

/**
 * Data holder for RSS data.  Follows RSS spec.
 * @author Bryan Osterkamp
 *
 */
public class SermonRssItem {

	private String title;
	private String description;
	private Date publishDate;
	private String link;
	private String enclosureUrl;
	private String keywords;
	private String totalDuration;
	
	/**
	 * Constructs the SermonRssItem on creation
	 */
	public SermonRssItem(String title, String description, 
			Date publishDate, String link, String enclosureUrl, String keywords, String totalDuration)
	{
		this.title = title;
		this.description = description;
		this.publishDate = publishDate;
		this.link = link;
		this.enclosureUrl = enclosureUrl;
		this.keywords = keywords;
		this.totalDuration = totalDuration;
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
	 * @return the publishDate
	 */
	public Date getPublishDate() {
		return publishDate;
	}
	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
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
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}


	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	} 

	
	/**
	 * Format of the SermonRssItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");  
		String result = getTitle() 
//		+ " \n "
//			+ getKeywords()
//			+ "   ( " + sdf.format(this.getPublishDate()) + " )" + " \n " 
//			+ getDescription() 
			;  
		return result;  
	}


	/**
	 * @return the totalDuration
	 */
	public String getTotalDuration() {
		return totalDuration;
	}


	/**
	 * @param totalDuration the totalDuration to set
	 */
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}
	
}
