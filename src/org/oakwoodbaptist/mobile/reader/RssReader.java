/**
 * 	02/17/2011 : Bryan Osterkamp - Creation
 * 01/13/2012 : Bryan Osterkamp - Added duration mapping to sermon rss.
 */
package org.oakwoodbaptist.mobile.reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.filter.Rule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.RRule;

import org.oakwoodbaptist.mobile.data.BibleVerseRssItem;
import org.oakwoodbaptist.mobile.data.CalendarRssItem;
import org.oakwoodbaptist.mobile.data.DevotionalRssItem;
import org.oakwoodbaptist.mobile.data.ERayRssItem;
import org.oakwoodbaptist.mobile.data.SermonRssItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * This class performs rss reader functionality.
 * @author Bryan Osterkamp
 *
 */
public class RssReader {
	
	/**
	 * Return the RSS response based on the url passed in.
	 * @param feedUrl
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static ArrayList<SermonRssItem> processSermonRssResponse(String feedUrl) throws IllegalStateException,
	IOException, ParserConfigurationException, SAXException
	{

    	//TODO Add null checks and logging
    	ArrayList<SermonRssItem> rssFeeds = new ArrayList<SermonRssItem>();
    	
    	Element element = retrieveDocumentElement(feedUrl);
    	//Put RSS Nodes in Node List
			NodeList nodeList = element.getElementsByTagName("item");
			
			if (nodeList.getLength() > 0) 
			{
				Log.v("RssReader: ", "RSS Feed has " + nodeList.getLength() + "nodes");
				for (int i = 0; i < nodeList.getLength(); i++) 
				{
					try
					{
		                //take each entry (corresponds to <item></item> tags in  
		                //xml data  
		                Element entry = (Element) nodeList.item(i);  
		                Element _titleE = (Element) entry.getElementsByTagName("title").item(0);  
		                Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);  
		                Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
		                Element _enclosureE = (Element) entry.getElementsByTagName("enclosure").item(0);
		                
		                
		                //Added new keywords element
		                Element _keywordsE = (Element) entry.getElementsByTagName("itunes:keywords").item(0);
		                
		                //Added new duration element for total duration
		                Element _durationE = (Element) entry.getElementsByTagName("itunes:duration").item(0);
		                String _title = _titleE.getFirstChild().getNodeValue();
		                String _link = _linkE.getFirstChild().getNodeValue();  
	
		                //To get the url of the media file, you have to pull in the url attribute of the
		                //enclosure tag.
		                String _enclosureUrl = _enclosureE.getAttribute("url");
		                
		                String _keywords = _keywordsE.getFirstChild().getNodeValue();
		                String _duration = _durationE.getFirstChild().getNodeValue();
		                
		                SermonRssItem sermonRssItem = new SermonRssItem(_title, null, null, _link, _enclosureUrl, _keywords, _duration);
		                rssFeeds.add(sermonRssItem);
					}
					catch (Exception e)
					{
						//Swallow exception in case we have an invalid node...
						Log.v("RssReader", "invalid node " + e.getMessage());
					}
				}
			}			
			else
			{
				Log.v("RssReader: ", "RSS Feed is null.");
			}
		
		return rssFeeds;

	}

	/**
	 * Return the RSS response based on the url passed in.
	 * @param feedUrl
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static ArrayList<BibleVerseRssItem> processBibleVerseRssResponse(String feedUrl) throws IllegalStateException,
	IOException, ParserConfigurationException, SAXException
	{

    	//TODO Add null checks and logging
    	ArrayList<BibleVerseRssItem> rssFeeds = new ArrayList<BibleVerseRssItem>();
    	
    	Element element = retrieveDocumentElement(feedUrl);
			
			//Put RSS Nodes in Node List
			NodeList nodeList = element.getElementsByTagName("item");
			
			if (nodeList.getLength() > 0) 
			{
//				Log.v("RssReader: ", "RSS Feed has " + nodeList.getLength() + "nodes");
				
				//Added to get the most recent node that has bible verses...
				boolean nodeHasValidContent = false;
				
				//Added just in case we have no valid nodes.
				boolean nodesComplete = false;
				
				while(!nodeHasValidContent || !nodesComplete)
				{
				
					//For Bible Verses, we just need the current one.
//					int i = 0;
					for (int i = 0; i < nodeList.getLength(); i++) 
					{
						
						//wrap in try block to catch exceptions
						try
						{
			                //take each entry (corresponds to <item></item> tags in  
			                //xml data  
			                Element entry = (Element) nodeList.item(i); 
			                
			                //Changed from "description" to "content:encoded" with website upgrade.
			                Element _descriptionE = (Element) entry.getElementsByTagName("content:encoded").item(0);  
//			                Log.v("RssReader", "before desc");
			                String _description = _descriptionE.getFirstChild().getNodeValue();  
//			                Log.v("description" + i + " ", _description);
			                
			                //call private method to parse out only the bible verses...
			                String parsedDescription = RssReader.parseBibleVerses(_description); 
//			                Log.v("parsed description" + i + " ", parsedDescription);
			                
			                BibleVerseRssItem bibleVerseRssItem = new BibleVerseRssItem(null, parsedDescription, null, null);
			                rssFeeds.add(bibleVerseRssItem);
			                nodeHasValidContent = true;
						}
						catch (Exception e)
						{
							//Swallow exception in case we have an invalid node...
							Log.v("RssReader", "invalid node");
						}
	
					}
					
					nodesComplete = true;
				}			
			}
			else
			{
				Log.v("RssReader: ", "RSS Feed is null.");
			}

		return rssFeeds;

	}
    
    /**
     * Parse out the bible verse by "Monday" and <br />
     * @param fullString
     * @return
     */
    private static String parseBibleVerses(String fullString)
    {
    	String formattedString = new String();
    	
    	//Need to parse out by weekday, since the <p> is now used in
    	//between the days.
    	String[] strings = fullString.split("<p>");
    	
    	for (int i = 0; i < strings.length; i++) 
    	{
//    		Log.v("parseBibleVerses" + i, " " + strings[i]);
    		if (strings[i].contains("Monday"))
    		{
    			String parsedString = strings[i]; 

    			//change to the mobile site...
    			formattedString = parsedString.replaceAll("www", "mobile");
    			
//    			Log.v("parseBibleVerses", "found Monday!");
    			break;
    		}
		}
//    	Log.v("parseBibleVerses", "formattedString: " + formattedString);
    	return formattedString;
    }

	/**
	 * Return the RSS response based on the url passed in.
	 * @param feedUrl
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static ArrayList<CalendarRssItem> processCalendarRssResponse(String feedUrl) throws IllegalStateException,
	IOException, ParserConfigurationException, SAXException
	{
//    	Log.v("processCalendarRssResponse", "entrada!");
    	ArrayList<CalendarRssItem> rssFeeds = new ArrayList<CalendarRssItem>();
    	URL url = new URL(feedUrl);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
//        Log.v("processCalendarRssResponse", "about to check connection");
        
    	    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) 
    	    {  
    	        InputStream is = conn.getInputStream();  
    	    	CalendarBuilder builder = new CalendarBuilder();

    	    	try 
    	    	{
    	    		//Log.v("RssReader", "about to build");
	    			Calendar calendar = builder.build(is);
	    			//Log.v("RssReader", "just did build");
	    			
        			//setup variables
        			String _title = "";
        			String _description = "";
        			net.fortuna.ical4j.model.Date _startDate = new net.fortuna.ical4j.model.Date(); 
        			String _category = "";
        			
        			//setting up dates to filter
        			
        			//start with today for start date
        			java.util.Calendar today = java.util.Calendar.getInstance
        				(TimeZone.getTimeZone("America/Chicago"), Locale.getDefault());
        			today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        			today.clear(java.util.Calendar.MINUTE);
        			today.clear(java.util.Calendar.SECOND);
        			
        			Log.v("RssReader", "starting with today");
        			
        			//setup duration in weeks (12 weeks is 3 months)
        			Dur duration = new Dur(4);
        			
        			//Log.v("RssReader", "set duration");
        			
        			Period period = new Period(new DateTime(today.getTime()), duration);
        			
        			//Log.v("RssReader", "set period");
        			
        			Rule[] rules = new Rule[1];
        			rules[0] = new PeriodRule(period);
        			
        			//Trying recurrence - WIP (get all occurrences for the events 
        			//and set as normal) - so Activity doesn't have to understand
        			//recurrence but just dates of events.
        			net.fortuna.ical4j.model.Date fDate 
        				= new net.fortuna.ical4j.model.Date(today.getTime());
        			
 //       			Log.v("RssReader", "about to filter");
        			
        			Filter filter = new Filter(rules, Filter.MATCH_ALL);
	        			
        			//Log.v("RssReader", "just filtered");
        			
        			for(Object o: filter.filter(calendar.getComponents(Component.VEVENT)))
        			{
        				//Log.v("RssReader", "about to cast event");
        				VEvent event = (VEvent) o; 
        				//Log.v("RssReader", "cast event");
	        				
        				//Try to get all dates for the event
        				Property prop = event.getProperty(Property.RRULE);
	        				
        				if (prop != null)
        				{
        					RRule rrulep = (RRule) prop;
        					Recur rec =  rrulep.getRecur(); 
	        				
	        				if (rec != null)
	        				{
    	        				DateList occurrences = rec.getDates(fDate, period, Value.DATE);
	    	        				
    	        				if (occurrences != null && occurrences.size() > 0)
        	        			{

    	        					//Log.v("RssReader", "Occurences = " + occurrences.size());

    	        					for (int i = 0; i < occurrences.size(); i++) 
    	        					{
        	        						
    	        						//Remove the time from the date...
		        	        			//DtStart sanitizedStartDate = new DtStart();

    	        						//Sanitize the date to have no hours or minutes
    	        						long longSaniDate = event.getStartDate().getDate().getTime();
        	        					Date longNewSaniDate = new Date(longSaniDate);
        	        					longNewSaniDate.setHours(0);
        	        					longNewSaniDate.setMinutes(0);
        	        					//_startDate = longNewDate;
	        						
    	        						//Had to change to ! after to be able to get the first date.
    	        						if (!event.getStartDate().getDate()
    	        								.after((net.fortuna.ical4j.model.Date)occurrences.get(i))
    	        								||
    	        								longNewSaniDate
    	        								.equals((net.fortuna.ical4j.model.Date)occurrences.get(i)))
    	        						{
        	        							
		        	        				if (event.getSummary() != null)
		        	        				{
		        	        					_title = event.getSummary().getValue();
		        	        					//Log.v("cal occ summary", _title);
		        	        				}

		        	        				//need to pull in occurrence date
		        	        				if (/*event.getStartDate()*/ occurrences.get(i) != null)
		        	        				{
		        	        					_startDate = /*event.getStartDate()*/
		        	        						(net.fortuna.ical4j.model.Date)occurrences.get(i)/*.getDate()*/;
	//		        	        	        					_startDate = event.getStartDate().getValue();
		        	        				}
        	        	        				
		        	        				boolean duplicate = isDuplicate(rssFeeds, _title, _startDate);
    	        	        				
		        	        				//Don't add an item that already exists
		        	        				if (!duplicate)
		        	        				{
		        	        					//Get the time from the start date
			        	        				_startDate.setHours(event.getStartDate().getDate().getHours());
			        	        				_startDate.setMinutes(event.getStartDate().getDate().getMinutes());

			        	        				CalendarRssItem calendarRssItem = 
	    	        		                		new CalendarRssItem(_title, _description, _startDate, _category);
	    	        		                	rssFeeds.add(calendarRssItem);
		        	        				}
    	        						}
									}
    	        				}
    	        				else
    	        				{
    	        					//Log.v("RssReader", "occurences size" + occurrences.size());
    	        				}    	        					

	        				}
	        				else
	        				{
	        					Log.v("RssReader", "reccurrence is null");
	        				}
        				}
        				else
        				{
        					
        					//Log.v("RssReader", "in else");
        					//There are no RRULES setup for recurrence.
        					//Now we need to check for a different start and end date to
        					//check for other recurrence settings.
        						//TODO read in start and end dates...
    						//Only pull in dates after today.
        					net.fortuna.ical4j.model.Date fToday = new net.fortuna.ical4j.model.Date();
    						if (event.getStartDate() != null && event.getStartDate().getDate()
    								.after(fToday))
    						{
								//while /*dates left after start date*/
    							net.fortuna.ical4j.model.property.DtEnd dtEnd = 
    								(net.fortuna.ical4j.model.property.DtEnd)event.getEndDate();
    								
    								if (dtEnd != null)
    								{
    									net.fortuna.ical4j.model.Date endDate = dtEnd.getDate();	
	        							net.fortuna.ical4j.model.Date tempDate = event.getStartDate().getDate();
	        							
	        							//If end date has no time, then set it for one day earlier.
	        							if (0 == endDate.getHours() && 0 == endDate.getMinutes())
	        							{
		        	        				GregorianCalendar gcz = new GregorianCalendar();
		        	        				gcz.setTime(endDate);
		        	        				gcz.add(GregorianCalendar.DAY_OF_MONTH, -1);
		        	        				long longTempDate = gcz.getTime().getTime();
		        	        				endDate = new net.fortuna.ical4j.model.Date(longTempDate);
	        							}

	        							//just so it doesn't go into an infinite loop
	        							int i = 0;
	        							while (!tempDate.after(endDate) && i < 30)
	        							{
	        								//logic
	        								//increment
	        								i++;
		    	        					// For non-recurring
		        	        				if (event.getSummary() != null)
		        	        				{
		        	        					_title = event.getSummary().getValue();
		        	        				}
		        	        				
		        	        				//need to pull in occurrence date
		        	        				if (tempDate != null)
		        	        				{
		        	        					//In order to not keep overwriting the date
		        	        					//we have to get the time, convert it to long
		        	        					//and set the new date.
		        	        					long longDate = tempDate.getTime();
		        	        					net.fortuna.ical4j.model.Date longNewDate 
		        	        						= new net.fortuna.ical4j.model.Date(longDate);
		        	        					_startDate = longNewDate;
    		        	        				//	_startDate = event.getStartDate().getValue();
		        	        				}

   		        	        				boolean duplicate = isDuplicate(rssFeeds, _title, _startDate);

//    	        	        				net.fortuna.ical4j.model.Date eventTempDate = tempDate;
        	        	        				
		        	        				//Don't add an item that already exists
		        	        				if (!duplicate)
		        	        				{
			        		                	CalendarRssItem calendarRssItem = 
			        		                		new CalendarRssItem(_title, _description, tempDate, _category);
			        		                	rssFeeds.add(calendarRssItem);
		        	        				}
		        	        				
		        	        				//TODO convert to calendar to set time?
		        	        				GregorianCalendar gc = new GregorianCalendar();
		        	        				gc.setTime(tempDate);
		        	        				gc.add(GregorianCalendar.DAY_OF_MONTH, 1);
		        	        				long longTempDate = gc.getTime().getTime();
		        	        				tempDate = new net.fortuna.ical4j.model.Date(longTempDate);
//		        	        				eventTempDate.setTime(tempDate.getTime() + 1 * 24 * 60 * 60 * 1000);
//	        								tempDate = eventTempDate;
//	    	        								Log.v("RssReader", "tempDate after _startDate " + _startDate);
    	        							}
        								}
        						}    	        				
	        				}
	        			}
    	        			
	        	} 
    	    	catch (ParserException e) 
    	    	{
    	    		Log.v("RssReader", "Caught ParserException " + e.getMessage());
	        	}
    	        
    	    }
    	    //otherwise we didn't get a connection
    	    else
    	    {
    	    	Log.v("input stream", "Response Code: " + conn.getResponseMessage());
    	    }

    	    return rssFeeds;
    	
	}

    //Commented out to try to fix multiple dates issue.
//	/**
//	 * @param rssFeeds
//	 * @param _title
//	 * @param _startDate
//	 * @return boolean
//	 */
//	private static boolean isDuplicate(ArrayList<CalendarRssItem> rssFeeds,
//			String _title, Date _startDate) 
//	{
//		boolean duplicate = false;
//		
//		for (int index = 0; index < rssFeeds.size(); index++) 
//		{
//			CalendarRssItem checkCalItem = rssFeeds.get(index);
//			if (checkCalItem.getTitle().equals(_title) &&
//					checkCalItem.getEventDate().equals(_startDate))
//			{
////				Log.v("RSSReader duplicate check: ", checkCalItem.getTitle() + " " + _title);
////				Log.v("RSSReader duplicate check: ", checkCalItem.getEventDate() + " " + _startDate);
////				Log.v("RSSReader duplicate check: ", " Duplicate: " + duplicate);
//				//we have a duplicate
//				duplicate = true;
//			}
//			if (_title.equalsIgnoreCase("All Access Back to School Bash Wednesday, August 29"))
//			{
//				Log.v("RSSReader duplicate check: ", checkCalItem.getTitle() + " " + _title);
//				Log.v("RSSReader duplicate check: ", checkCalItem.getEventDate() + " " + _startDate);
//				Log.v("RSSReader duplicate check: ", " Duplicate: " + duplicate);
//			}
//			
//		}
//		return duplicate;
//	}
    
	/**
	 * @param rssFeeds
	 * @param _title
	 * @param _startDate
	 * @return boolean
	 */
	private static boolean isDuplicate(ArrayList<CalendarRssItem> rssFeeds,
			String _title, Date _startDate) 
	{
		boolean duplicate = false;
		
		for (int index = 0; index < rssFeeds.size(); index++) 
		{
			CalendarRssItem checkCalItem = rssFeeds.get(index);
			if (checkCalItem.getTitle().equals(_title) 
//					&&
//					checkCalItem.getEventDate().equals(_startDate)
					)
				//Make sure the date types match
				//Event Date needs to be converted to date
			{
				java.util.Date properDate = new java.util.Date();
				properDate.setTime(checkCalItem.getEventDate().getTime());
//				Log.v("RSSReader duplicate check: ", checkCalItem.getTitle() + " " + _title);
//				Log.v("RSSReader duplicate check: ", checkCalItem.getEventDate() + " " + _startDate);
//				Log.v("RSSReader duplicate check: ", "Proper Date" + properDate.toString());
//				Log.v("RSSReader duplicate check: ", " Duplicate: " + duplicate);
				//we have a duplicate
				duplicate = true;
			}
			/*
			if (_title.startsWith("Shine"))
			{
				Log.v("RSSReader duplicate check: ", checkCalItem.getTitle() + " " + _title);
				Log.v("RSSReader duplicate check: ", checkCalItem.getEventDate() + " " + _startDate);
				Log.v("RSSReader duplicate check: ", " Duplicate: " + duplicate);
			}
			*/
		}
		return duplicate;
	}
    
    //ERAY
	/**
	 * Return the RSS response based on the url passed in.
	 * @param feedUrl
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static ArrayList<ERayRssItem> processERayRssResponse(String feedUrl) throws IllegalStateException,
	IOException, ParserConfigurationException, SAXException
	{

    	//TODO Add null checks and logging
    	ArrayList<ERayRssItem> rssFeeds = new ArrayList<ERayRssItem>();
    	
        Element element = retrieveDocumentElement(feedUrl);	
			//Put RSS Nodes in Node List
			NodeList nodeList = element.getElementsByTagName("item");
			
			if (nodeList.getLength() > 0) 
			{
//				Log.v("RssReader: ", "RSS Feed has " + nodeList.getLength() + "nodes");
				
				//Added to get the most recent node that has bible verses...
				boolean nodeHasValidContent = false;
				
				//Added just in case we have no valid nodes.
				boolean nodesComplete = false;
				
			
					//For Bible Verses, we just need the current one.
//					int i = 0;
					for (int i = 0; i < nodeList.getLength(); i++) 
					{
						
						//wrap in try block to catch exceptions
						try
						{
			                //take each entry (corresponds to <item></item> tags in  
			                //xml data  
			                Element entry = (Element) nodeList.item(i);  
//			                Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);  
			                
			                //changing to encoded to get the full text
			                Element _descriptionE = (Element) entry.getElementsByTagName("content:encoded").item(0);
			                
//			                Log.v("RssReader", "before desc");
			                String _description = _descriptionE.getFirstChild().getNodeValue();  
//			                Log.v("description" + i + " ", _description);
			                
			                Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
			                String _title = _titleE.getFirstChild().getNodeValue();  
//			                Log.v("title" + i + " ", _title);
			                
			                Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
			                String _link = _linkE.getFirstChild().getNodeValue();  
//			                Log.v("link" + i + " ", _link);
			                
			                ERayRssItem ERayRssItem = new ERayRssItem(_title, _description, _link);
			                rssFeeds.add(ERayRssItem);
			                nodeHasValidContent = true;
						}
						catch (Exception e)
						{
							//Swallow exception in case we have an invalid node...
							Log.v("RssReader", "invalid node");
						}
	
					}			
			}
			else
			{
				Log.v("RssReader: ", "RSS Feed is null.");
			}

//        }


		
		return rssFeeds;

	}

    //Devotionals
    //ERAY
	/**
	 * Return the RSS response based on the url passed in.
	 * @param feedUrl
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static ArrayList<DevotionalRssItem> processDevotionalsRssResponse(String feedUrl) throws IllegalStateException,
	IOException, ParserConfigurationException, SAXException
	{

    	//TODO Add null checks and logging
    	ArrayList<DevotionalRssItem> rssFeeds = new ArrayList<DevotionalRssItem>();
    	
        Element element = retrieveDocumentElement(feedUrl);	
			//Put RSS Nodes in Node List
			NodeList nodeList = element.getElementsByTagName("item");
			
			if (nodeList.getLength() > 0) 
			{
				//Log.v("RssReader: ", "RSS Feed has " + nodeList.getLength() + "nodes");
				
				//Added to get the most recent node that has bible verses...
				boolean nodeHasValidContent = false;
				
				//Added just in case we have no valid nodes.
				boolean nodesComplete = false;
				
			
					for (int i = 0; i < nodeList.getLength(); i++) 
					{
						
						//wrap in try block to catch exceptions
						try
						{
			                //take each entry (corresponds to <item></item> tags in  
			                //xml data  
			                Element entry = (Element) nodeList.item(i);  
			                Element _descriptionE = (Element) entry.getElementsByTagName("content:encoded").item(0);  
//			                Log.v("RssReader", "before desc");
			                String _description = _descriptionE.getFirstChild().getNodeValue();  
//			                Log.v("description" + i + " ", _description);
			                
			                Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
			                String _title = _titleE.getFirstChild().getNodeValue();  
//			                Log.v("title" + i + " ", _title);
			                
			                Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
			                String _link = _linkE.getFirstChild().getNodeValue();  
//			                Log.v("link" + i + " ", _link);
			                
			                DevotionalRssItem devotionalRssItem = new DevotionalRssItem(_title, _description, _link);
			                rssFeeds.add(devotionalRssItem);
			                nodeHasValidContent = true;
						}
						catch (Exception e)
						{
							//Swallow exception in case we have an invalid node...
							Log.v("RssReader", "invalid node");
						}
	
					}			
			}
			else
			{
				Log.v("RssReader: ", "RSS Feed is null.");
			}

//        }


		
		return rssFeeds;

	}
    
    
    
/**
 * @param feedUrl
 * @throws MalformedURLException
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 */
private static Element retrieveDocumentElement(String feedUrl)
		throws MalformedURLException, IOException,
		ParserConfigurationException, SAXException {
	URL url = new URL(feedUrl);  
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
    //Initialize to null
    Element element = null;
    
	    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) 
	    {  
	        InputStream is = conn.getInputStream();  
		
			//Used for Parsing XML.
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document document = db.parse(is);
			element = document.getDocumentElement();
			
	    }
	    //otherwise we didn't get a connection
	    else
	    {
	    	Log.v("retrieveDocumentElement", "Response Code: " + conn.getResponseMessage());
	    }
	    
	    return element;
	}

private static Date parseDate(String description)
{
	String formattedString = null;
	
	String[] strings = description.split("<br />");
	
	//we just need the first string
	formattedString = strings[0]; 
	//Log.v("parseDate", "parsed" + formattedString);
	
	//Now let's divide the date into three parts
	String[] date_parts = description.split(" ");
	
	formattedString = "";
	
	for (int i = 0; i <= 2; i++) 
	{
		//Log.v("date_parts" + i, "" + date_parts[i]); 
		formattedString = formattedString + date_parts[i] + " ";
	}	
	
	DateFormat df = new SimpleDateFormat("MMMMMMMMMM dd, yyyy ");
	
    Date d1=null;         
    try 
    {             
    	SimpleDateFormat formatter = new SimpleDateFormat("MMMMMMMMMM dd, yyyy");

    	d1 = df.parse(formattedString);         
    	//Log.v("parseDate - Formatted", formatter.format(d1));
        //Log.v("parseDate", "month" + d1.getMonth());
        //Log.v("parseDate", "days" + d1.getDay());
        //Log.v("parseDate", "year" + d1.getYear());
    } 
    catch (ParseException e) 
    {                                         
    	Log.v("RssReader", "parse exception" + e.getMessage()); 
    } 
	
	return d1;
}
    
    
    
}
