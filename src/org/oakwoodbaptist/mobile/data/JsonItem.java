/**
 * 	03/05/2011 : Bryan Osterkamp - Initial Creation
 */
package org.oakwoodbaptist.mobile.data;


/**
 * Data holder for Json data.
 * @author Bryan Osterkamp
 *
 */
public class JsonItem {

	private String title;
	private String content;

	
	/**
	 * Constructs the JsonItem on creation
	 */
	public JsonItem(String title, String content)
	{
		this.title = title;
		this.content = content;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}


	/**
	 * Format of the JsonItem when toString is called.
	 */
	@Override 
	public String toString() 
	{  
		String result = 
//			getTitle() + " \n " + 
			getContent() ;  
		return result;  
	} 

	
}
