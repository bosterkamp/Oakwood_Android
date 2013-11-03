/**
 * 	05/15/2011 : Bryan Osterkamp - Creation
 */
package org.oakwoodbaptist.mobile;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.oakwoodbaptist.mobile.custom.MyArrayAdapter;
import org.oakwoodbaptist.mobile.data.CalendarRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Represents the activity of showing and selecting the sermon to be consumed.
 * @author Bryan Osterkamp
 *
 */
public class CalendarActivity extends Activity implements Runnable{

	//Need to figure out how to get ical to work.
	private static final String CALENDAR_URL = "http://oakwoodnb.com/calendars/all/feed/ical/";
	private ProgressDialog pd;
	private boolean dialogComplete = false;

	Spinner spinner;
	ArrayList<String> categoriesStrings = new ArrayList<String>();
	TextView tv1;
	ListView lv1;
	
	//RSS data
	ArrayList<CalendarRssItem> rssItems = new ArrayList<CalendarRssItem>();
	ArrayList<CalendarRssItem> allRssItems = new ArrayList<CalendarRssItem>(); 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("CalendarActivity", "entry");
        
        setContentView(R.layout.calendar);
        
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        
        
        Thread thread = new Thread(this);
        thread.start();

        
        MyArrayAdapter<CalendarRssItem> aa = null; 
        tv1 = (TextView) this.findViewById(R.id.calendar_text);
        lv1 = (ListView) this.findViewById(R.id.rssListView);
//        spinner = (Spinner) this.findViewById(R.id.spin_categories);

    }

	/**
	 * 
	 */
	private void display() {
		MyArrayAdapter<CalendarRssItem> aa;
		
		
		//Try to sort the dates
		Collections.sort(rssItems, new Comparator<CalendarRssItem>() {
			public int compare(CalendarRssItem c1, CalendarRssItem c2) {
//				Log.v("CalendarActivity", c1.getEventDate() + " " + c1.getTitle());
				return c1.getEventDate().compareTo(c2.getEventDate());
			}
		});
		
		
		//End
		
		//Use adapter to pull in RSS feed data.  
		aa = new MyArrayAdapter<CalendarRssItem>(this, R.layout.rss_items, rssItems);  
		if (aa.isEmpty())
		{
			Log.v("aa ", "is empty");
			TextView errors = (TextView) this.findViewById(R.id.errors);
			errors.setText("Unable to retrieve calendar data.  Please make sure you " +
					"have an active network or cell connection and try again later.");
			
		}
		else
		{
			Log.v("aa count ", String.valueOf(aa.getCount()));
		}

		//Bind array adapter to the list  
		lv1.setAdapter(aa); 
		//Setup listener
		lv1.setOnItemClickListener(new OnItemClickListener() {  

		@Override 
		public void onItemClick(AdapterView<?> av, View view, int index,  
		  long arg3) {
			
			Log.v("Calendar ", "activity clicked");
		  }  
		});
	}
    
    public void filterByCategory(String category)
    {
    	//Filter out by category...
    	Log.v("filterByCategory", category);

    	rssItems.clear();
    	
    	for (int i = 0; i < allRssItems.size(); i++) 
    	{
			if (allRssItems.get(i).getCategory().equals(category))
			{
				rssItems.add(allRssItems.get(i));
			}
		}
    	
    }

    public void run() {
          
        try
        {
        	Log.v("CalendarActivity", "before RssReader call");
        	allRssItems = RssReader.processCalendarRssResponse(CALENDAR_URL);
        	Log.v("CalendarActivity", "after RssReader call");
        	
        	if (allRssItems != null)
        	{
        		//Log.v("CalendarActivity", "allRssItems size: " + allRssItems.size());
        	}
        	else
        	{
        		//Log.v("CalendarActivity", "allRssItems is null.");
        	}
        	
        	
        	rssItems.addAll(allRssItems);
        	
        }
        catch(Exception e)
        {
        	Log.v("failure", "e=" + e.getMessage());
        }

    	
          handler.sendEmptyMessage(0);
    }
    
    public class MyOnItemSelectedListener implements OnItemSelectedListener 
    {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id) 
    	{      
    		//Clear filters
    		if ((pos) == 0)
    		{
				rssItems.clear();
				rssItems.addAll(allRssItems);
    		}
    		
    		else
    		{
        		filterByCategory(parent.getItemAtPosition(pos).toString());
    		}
    		display();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) 
    	{      
    		rssItems.clear();
			rssItems.addAll(allRssItems);
			display();    
    	}
    	}
    
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        
        	final Set<String> cat = new HashSet<String>();
        	
        	//Pull out unique calendar items
        	for (int i = 0; i < allRssItems.size(); i++) 
        	{
				cat.add(allRssItems.get(i).getCategory());
			}
        	
 	
        	Iterator i = cat.iterator();
        	
        	categoriesStrings.add("Show All");
        	
        	while (i.hasNext())
        	{
        		String category = i.next().toString();
                categoriesStrings.add(category);
        	}
        	
//        	ArrayAdapter<String> spinnerArrayAdapter = 
//        		new ArrayAdapter<String>(CalendarActivity.this, R.layout.custom_spinner, 
//        						categoriesStrings); 
//        	
//        	//Setup listener
//    		spinner.setOnItemSelectedListener (new MyOnItemSelectedListener());
//        	
//        	spinner.setAdapter(spinnerArrayAdapter);
        	
        	//Need to sort based on current and future dates

            display();  

        //swallow the exception just in case something odd happens here.
            try 
            {
            	if (!dialogComplete)
            	{
            		pd.dismiss();
            		dialogComplete = true;
            	}
            	
            }
            catch (Exception e)
            {
            	Log.v("Calendar Activity ", e.getMessage());
            }
        
        }
    };

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
        try 
        {
        	if (!dialogComplete)
        	{
        		pd.dismiss();
        		dialogComplete = true;
        	}
        	
        }
        catch (Exception e)
        {
        	Log.v("Calendar Activity onPause", e.getMessage());
        }

		super.onPause();
	}

}
