/**
 * 	10/20/2013 : Bryan Osterkamp - Initial Creation
 */
package org.oakwoodbaptist.mobile.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Data holder for Ray Tweet JSON item.
 * @author Bryan Osterkamp
 *
 */
public class RayTweetJsonItem {

	private String text;
	private String postDate;

	
	/**
	 * Constructs the RayTweetJsonItem on creation
	 */
	public RayTweetJsonItem(String text, String postDate)
	{
		this.text = text;
		this.postDate = postDate;
	}
	
	
	/**
	 * Format of the SermonRssItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
		
		//java.util.Calendar tweetDay = java.util.Calendar.getInstance
				//(TimeZone.getTimeZone("America/Chicago"), Locale.getDefault());
		//tweetDay.setTime(java.util.Date.valueOf(postDate));
		
		/*
		String testDate = "29-Apr-2010,13:00:14 PM";
		DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
		Date date = formatter.parse(testDate);
		
		//
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	String dateInString = "07/06/2013";
 
	try {
 
		Date date = formatter.parse(dateInString);
		
		*/
		
		String result = "";
		
		try {
		SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		
		Date parsedDate = formatter.parse(this.getPostDate());
		
		SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd, yyyy");  
		
		

			result = getText() + "\n" 
			//+ "@PastorRayStill" + "\n" 
			//+ tweetDay
			//+ getPostDate()
				//+ "   ( " + sdf.format(this.getPostDate()) + " )" + " \n " 
				//+ sdf.parse(this.getPostDate());
				//+ formatter.parse(this.getPostDate());
				+ sdf.format(parsedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return result;  
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * @return the postDate
	 */
	public String getPostDate() {
		return postDate;
	}


	/**
	 * @param postDate the postDate to set
	 */
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	
}
