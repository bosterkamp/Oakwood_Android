package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.data.ERayRssItem;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Represents the activity of showing the daily bible verse.
 * @author Bryan Osterkamp
 *
 */
public class ERaySelectionActivity extends Activity  implements Runnable {

	//Same URL as the Sermon, because the Bible Verses are shoved in the Sermon feed...
	private static final String ERAY_URL = "http://www.oakwoodbaptist.org/tags/eRay/feed/rss";
	private static final String TAG = "ERaySelectionActivity";
	
	ListView lv1;
	TextView tv1;
	TextView eray_view;
	ArrayList<ERayRssItem> rss = new ArrayList();
	ArrayAdapter<ERayRssItem> aa = null;
	TextView errors;
	private ProgressDialog pd;
	private boolean dialogComplete;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eray_selection);
        
        
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
	    pd.setIndeterminate(true);
	    pd.setCancelable(false);
	    pd.show();
      
      
      Thread thread = new Thread(this);
      thread.start();
      
       
      lv1 = (ListView) this.findViewById(R.id.rssListView);

      
    }
        
        
    
    //
    public void run() 
    {
        try
        {
        	Log.v(TAG, "About to call process eray rss");
        	rss = RssReader.processERayRssResponse(ERAY_URL);
        	handler.sendEmptyMessage(0);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	Log.v("BibleVerseActivity", " failure");
        	handler.sendEmptyMessage(0);
        }

    }
    //

    
    
    //
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

    try
    {
        TextView errors = (TextView) findViewById(R.id.errors);
        
        //Use adapter to pull in RSS feed data.  
    	aa = new ArrayAdapter<ERayRssItem>(ERaySelectionActivity.this, R.layout.rss_items, rss);  
    	if (aa.isEmpty())
    	{
    		Log.v("aa ", "is empty");
    		
    		errors.setText("Unable to retrieve eRays. Please make sure you " +
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
        	String title = rss.get(index).getTitle();
        	
        	//Launch a new activity sending the url of the media file.
        	Intent launchERay = new Intent(ERaySelectionActivity.this, ERayActivity.class);
        	
        	//Create a "bundle" to pass data to new activity
        	Bundle bundle = new Bundle();
        	bundle.putString("url", url);
        	bundle.putString("title", title);
        	launchERay.putExtras(bundle);
        	startActivity(launchERay);
        	
          }  
        });  
	
    }
    catch(Exception e)
    {
		errors.setText("Unable to retrieve eRays. Please make sure you " +
		"have an active network or cell connection and try again later.");
    	e.printStackTrace();
    	Log.v("ErayActivity", " failure");
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
    	Log.v("Eray Selection Activity ", e.getMessage());
    }

        
        }
    };    

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
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
        	Log.v("Eray Selectoin Activity onPause", e.getMessage());
        }

		super.onPause();
	}	

    
    
    
}
