package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.data.DevotionalRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Represents the activity of showing the devotionals.
 * @author Bryan Osterkamp
 *
 */
public class DevotionalSelectionActivity extends Activity {
	
	private static final String DEVOTIONAL_URL = "http://www.oakwoodbaptist.org/category/vlog/feed/rss";
	private static final String TAG = "DevotionalSelectionActivity";
	
	ListView lv1;
	TextView devotional_view;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devotional_selection);
        
//        ArrayAdapter<DevotionalRssItem> aa = null; 
//        lv1 = (ListView) this.findViewById(R.id.rssListView);
        
        //TODO fix this once this is validated...
        devotional_view = (TextView) this.findViewById(R.id.devotional_text);
        
        try
        {
        	Log.v(TAG, "About to call process devotional rss");
        	
        	final ArrayList<DevotionalRssItem> rss = RssReader.processDevotionalsRssResponse(DEVOTIONAL_URL);

        	Log.v(TAG, "after process devotional rss");
        	
        	//Trying to get videos to work
        	if (rss != null)
        	{
        		if (rss.get(0) != null)
        		{
        			Log.v(TAG, "rss get 0 not null");
        			Log.v(TAG, "rss get 0 desc: " + rss.get(0).getDescription());
        			
        			//reformat to include escape characters...
        			
        			
        			//get description, parse out http://player.vimeo.com/video/37749667
        	    	String desc = rss.get(0).getDescription();
        	    	String title = rss.get(0).getTitle();
        	    	String link = rss.get(0).getLink();
        	    	
        			String formattedString = new String();
        			
        			formattedString = desc;
        			
        			String[] strings = desc.split("?");
        			
        			//we just need the first string
        			formattedString = strings[0];
        			
        			//should look like this.. <p><iframe src="http://player.vimeo.com/video/37749667
        			String[] linkString = formattedString.split("\"");
        			formattedString = linkString[1];
        			
        			//should
        			
        			
//        			formattedString = desc + "<a href=&quot;" + link +  "&quot;>" + title + "</a>";
//        			formattedString = desc + "<a href=\"" + link +  "\">" + title + "</a>";
        			
        			formattedString = "<a href=\"" + formattedString +  "\">" + title + "</a>";
        			
//        			formattedString = formattedString.replaceAll("</p>", "");
//        			formattedString = formattedString + "</p>";
//        			formattedString = formattedString.replaceAll("\"", "&quot;");
        			
        			//Do I need this?
//        			formattedString = formattedString.replaceAll("\"", "\"\"");
        			
        			Log.v(TAG, "formattedString: " + formattedString);
        			//end
        			//Set the text in HTML format;
        			//working
//        			formattedString="<a href=\"http://vimeo.com/37749667\">Video Devotion - Week of March 4, 2012</a>";
        			//I think this works too, which is what we need.
//        			formattedString="<a href=\"http://player.vimeo.com/video/37749667\">Video Devotion - Week of March 4, 2012</a>";
        			
        			devotional_view.setText(Html.fromHtml(formattedString));
//        			devotional_view.setText(Html.fromHtml(rss.get(0).getDescription()));
//        			bible_verses_view.setTextSize(50);
        			
        			//Make the links clickable
        			devotional_view.setMovementMethod(LinkMovementMethod.getInstance());
        		}
            	else
            	{
            		Log.v(TAG, "rss.get(0) is null");
            	}
        	}
        	else
        	{
        		Log.v(TAG, "rss is null");
        	}
        	//end
        	
            //Use adapter to pull in RSS feed data.  
//        	aa = new ArrayAdapter<DevotionalRssItem>(this, R.layout.rss_items, rss);  
//        	if (aa.isEmpty())
//        	{
//        		Log.v("aa ", "is empty");
//        		TextView errors = (TextView) this.findViewById(R.id.errors);
//        		errors.setText("Unable to retrieve devotionals.  Please make sure you have an" +
//        				"internet connection and/or try again later.");
//        		
//        	}
//        	else
//        	{
//        		Log.v("aa count ", String.valueOf(aa.getCount()));
//        	}
//        		
//        	//Bind array adapter to the list  
//        	lv1.setAdapter(aa); 
//
//        	
//            //Setup listener
//            lv1.setOnItemClickListener(new OnItemClickListener() {  
//
//            @Override 
//            public void onItemClick(AdapterView<?> av, View view, int index,  
//              long arg3) {
//
//            	String url = rss.get(index).getLink();
//            	
//            	//Launch a new activity sending the url of the media file.
//            	Intent launchDevotional = new Intent(DevotionalSelectionActivity.this, VideoDevotionalActivity.class);
//            	Toast.makeText(DevotionalSelectionActivity.this, "URL to be launched: " + url, Toast.LENGTH_LONG).show();
//            	//Create a "bundle" to pass data to new activity
//            	Bundle bundle = new Bundle();
//            	bundle.putString("url", url);
//            	launchDevotional.putExtras(bundle);
//            	startActivity(launchDevotional);
//            	
//              }  
//            });  
  	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	Log.v("DevotionalActivity", " failure");
        }
        
    }

}
