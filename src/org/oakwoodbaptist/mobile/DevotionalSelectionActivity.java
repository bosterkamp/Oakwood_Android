package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.custom.MyArrayAdapter;
import org.oakwoodbaptist.mobile.data.DevotionalRssItem;
import org.oakwoodbaptist.mobile.data.SermonRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Represents the activity of showing the devotionals.
 * @author Bryan Osterkamp
 *
 */
public class DevotionalSelectionActivity extends Activity implements Runnable {
	
	private static final String DEVOTIONAL_URL = "http://vimeo.com/channels/299087/videos/rss";
	private static final String TAG = "DevotionalSelectionActivity";
	
	TextView tv1;
	ListView lv1;
	ArrayList<DevotionalRssItem> rss = new ArrayList<DevotionalRssItem>();
	
	private ProgressDialog pd;
	private boolean dialogComplete;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sermon_selection);
        
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
	    pd.setIndeterminate(true);
	    pd.setCancelable(false);
	    pd.show();
      
	    
      
      Thread thread = new Thread(this);
      thread.start();
        
    }
    
    public void run() 
    {
        try
        {
        	rss = RssReader.processDevotionalsRssResponse(DEVOTIONAL_URL);
        	handler.sendEmptyMessage(0);
        }
        catch(Exception e)
        {
//        	e.printStackTrace();
        	Log.v("SermonSelectionActivity", " failure");
        	handler.sendEmptyMessage(0);
        }

    }
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        
        MyArrayAdapter<DevotionalRssItem> aa = null; 
        lv1 = (ListView) DevotionalSelectionActivity.this.findViewById(R.id.rssListView);
        TextView errors = (TextView) DevotionalSelectionActivity.this.findViewById(R.id.errors);
        
        try
        {
            //Use adapter to pull in RSS feed data.  
        	aa = new MyArrayAdapter<DevotionalRssItem>(DevotionalSelectionActivity.this, R.layout.rss_items, rss);  
        	if (aa.isEmpty())
        	{
        		Log.v("aa ", "is empty");
        		
        		errors.setText("Unable to retrieve devotions. Please make sure you " +
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

            	String url = rss.get(index).getLink();
            	String sermon_title = rss.get(index).getTitle();
            	Log.v("URL: ", url);
            	
            	
            	Intent launchDevotional = new Intent(DevotionalSelectionActivity.this, VideoDevotionalActivity.class);
            	startActivity(launchDevotional);

            	
            	//new
            	//startActivity(new Intent(Intent.ACTION_VIEW, 
            	//	    Uri.parse("http://vimeo.com/channels/299087/")));
            	//new end
            	
            	//String totalDuration = rss.get(index).getTotalDuration();
            	
            	//Launch a new activity sending the url of the media file.
//            	Intent launchSermon = new Intent(SermonSelectionActivity.this, SermonActivity_buffer.class);
            	//Use the new video player controller
            	//Intent launchDevotional = new Intent(DevotionalSelectionActivity.this, VideoPlayerController.class);
            	
            	//Create a "bundle" to pass data to new activity
            	//Bundle bundle = new Bundle();
            	//bundle.putString("mediaUrl", url);
            	//bundle.putString("sermontitle", sermon_title);
            	//bundle.putString("totalDuration", totalDuration);
            	//launchDevotional.putExtras(bundle);
            	//startActivity(launchDevotional);
            	
            	
              }  
            });  

        	
        	
        }
        catch(Exception e)
        {
        	Log.v("failure", "e=" + e.getMessage());
    		errors.setText("Unable to retrieve devotional. Please make sure you " +
    		"have an active network or cell connection and try again later.");
        	
        }
        
        
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
        	Log.v("DevotionalSelection Activity ", e.getMessage());
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
	    	Log.v("DevotionalSelection Activity onPause", e.getMessage());
	    }
	
		super.onPause();
	}

}
