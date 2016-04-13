/**
 * 	06/21/2011 : Bryan Osterkamp - Initial Creation
 */
package org.oakwoodbaptist.mobile.data;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.text.Html;
import android.util.Log;

/**
 * Data holder for Calendar RSS data.  Follows RSS spec.
 * @author Bryan Osterkamp
 *
 */
public class CalendarRssItem {

	private String title;
	private String description;
	private net.fortuna.ical4j.model.Date eventDate;
	private String category;
	private String recurrence;
	
	/**
	 * Constructs the CalendarRssItem on creation
	 */
	public CalendarRssItem(String title, String description, 
			net.fortuna.ical4j.model.Date eventDate, String category)
	{
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.category = category;
		this.recurrence = recurrence;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Format of the CalendarRssItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
		
		String formatString = "";

		if (0 == getEventDate().getHours() && 0 == getEventDate().getMinutes())
		{
			formatString = "MMMM d, yyyy";
		}
		else
		{
			formatString = "MMMM d, yyyy h:mm aa";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.US);

		String details = new String();
		
		details = " " + "Day " + getEventDate().getDay() + " H/M " + getEventDate().getHours() 
			+ getEventDate().getMinutes();
		
		//Log.v("CalendarRssItem", "Summary: " + getTitle() + " Details: " + details);
		
		TimeZone tz = TimeZone.getTimeZone("America/Chicago");
		formatter.setTimeZone(tz);
//		Log.v("Reader Title", getTitle());
//		Log.v("Item Dates: ", "_startDate " + getEventDate().getDate() + 
//				getEventDate().getHours() + " " + getEventDate().getMinutes());
//		
//		Log.v("Formatted Date", formatter.format(getEventDate()));
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");  
		String result = 
//			"Details " + details + " " +
//			"Event Date: " 
//		+ 
		formatter.format(getEventDate().getTime())  //+ " " + getRecurrence()
		+ " \n "  
		+	Html.fromHtml(getTitle()) 
		+ " \n "
		//Removed until I can get a category from the RSS feed.
		//		+ "Group: " + getCategory()
//		+ " \n"
		+ Html.fromHtml(getDescription()) 
		;  
		return result;  
	}


	/**
	 * @return the eventDate
	 */
	public net.fortuna.ical4j.model.Date getEventDate() {
		return eventDate;
	}


	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(net.fortuna.ical4j.model.Date eventDate) {
		this.eventDate = eventDate;
	}


	/**
	 * @return the recurrence
	 */
	public String getRecurrence() {
		return recurrence;
	}


	/**
	 * @param recurrence the recurrence to set
	 */
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	
}
