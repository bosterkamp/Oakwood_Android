/**
 * 	02/17/2011 : Bryan Osterkamp - Creation
 */
package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.custom.MyArrayAdapter;
import org.oakwoodbaptist.mobile.data.SermonRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Represents the activity of showing and selecting the sermon to be consumed.
 * @author Bryan Osterkamp
 *
 */
public class SermonSelectionActivity extends Activity implements Runnable {

	
	//private static final String SERMONS_URL = "http://www.oakwoodbaptist.org/category/sermons/feed/podcast";
	private static final String SERMONS_URL = "http://oakwoodnb.com/sermons/feed";
	
	TextView tv1;
	ListView lv1;
	ArrayList<SermonRssItem> rss = new ArrayList<SermonRssItem>();
	
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
        	rss = RssReader.processSermonRssResponse(SERMONS_URL);
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

        
        MyArrayAdapter<SermonRssItem> aa = null; 
        lv1 = (ListView) SermonSelectionActivity.this.findViewById(R.id.rssListView);
        TextView errors = (TextView) SermonSelectionActivity.this.findViewById(R.id.errors);
        
        try
        {
            //Use adapter to pull in RSS feed data.  
        	aa = new MyArrayAdapter<SermonRssItem>(SermonSelectionActivity.this, R.layout.rss_items, rss);  
        	if (aa.isEmpty())
        	{
        		Log.v("aa ", "is empty");
        		
        		errors.setText("Unable to retrieve sermons. Please make sure you " +
        		"have an active network or cell connection and try again later.");
        		
        	}
        	else
        	{
//        		errors.setText("Unable to retrieve sermons. Please make sure you " +
//        		"have an active network or cell connection and try again later.");
        		Log.v("aa count ", String.valueOf(aa.getCount()));
        	}
        		
        	//Bind array adapter to the list  
        	lv1.setAdapter(aa); 

        	
            //Setup listener
            lv1.setOnItemClickListener(new OnItemClickListener() {  

            @Override 
            public void onItemClick(AdapterView<?> av, View view, int index,  
              long arg3) {

            	String url = rss.get(index).getEnclosureUrl();
            	String sermon_title = rss.get(index).getTitle();
            	String totalDuration = rss.get(index).getTotalDuration();
            	
            	//Launch a new activity sending the url of the media file.
//            	Intent launchSermon = new Intent(SermonSelectionActivity.this, SermonActivity_buffer.class);
            	//Use the new video player controller
            	Intent launchSermon = new Intent(SermonSelectionActivity.this, VideoPlayerController.class);
            	
            	//Create a "bundle" to pass data to new activity
            	Bundle bundle = new Bundle();
            	bundle.putString("mediaUrl", url);
            	bundle.putString("sermontitle", sermon_title);
            	bundle.putString("totalDuration", totalDuration);
            	launchSermon.putExtras(bundle);
            	startActivity(launchSermon);
            	
              }  
            });  

        	
        	
        }
        catch(Exception e)
        {
        	Log.v("failure", "e=" + e.getMessage());
    		errors.setText("Unable to retrieve sermons. Please make sure you " +
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
        	Log.v("SermonSelection Activity ", e.getMessage());
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
	    	Log.v("SermonSelection Activity onPause", e.getMessage());
	    }
	
		super.onPause();
	}

}