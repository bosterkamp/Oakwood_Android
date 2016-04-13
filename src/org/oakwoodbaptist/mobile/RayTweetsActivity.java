package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.custom.MyArrayAdapter;
import org.oakwoodbaptist.mobile.data.RayTweetJsonItem;
import org.oakwoodbaptist.mobile.reader.JsonReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Represents the activity of showing Pastor Ray's tweets.
 * @author Bryan Osterkamp
 *
 */
public class RayTweetsActivity extends Activity  implements Runnable {

	private ProgressDialog pd;
	private boolean dialogComplete = false;
	ArrayList<RayTweetJsonItem> tweets = new ArrayList<RayTweetJsonItem>();
	TextView tweetTextView;
	ListView lv1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);
	
	//Log.v("Inside Tweet View", "onCreate");
	
	setContentView(R.layout.ray_tweets);

    pd = new ProgressDialog(this);
    pd.setMessage("Loading");
    pd.setIndeterminate(true);
    pd.setCancelable(false);
    pd.show();
    
    
    Thread thread = new Thread(this);
    thread.start();

    lv1 = (ListView) this.findViewById(R.id.rssListView);


		
		//TextView tweetsText = (TextView) findViewById(R.id.tweetTextView);
		//tweetsText.setText("Ray Tweets!");
	}
	

	public void run() 
	{
		//Return the last 15 tweets
		String feedUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?count=10&screen_name=pastorraystill";
		tweets = JsonReader.processTwitterResponse(feedUrl);
        handler.sendEmptyMessage(0);
	}
	
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

    		//Use adapter to pull in JSON feed data.  
        	MyArrayAdapter<RayTweetJsonItem> aa; 

    		aa = new MyArrayAdapter<RayTweetJsonItem>(RayTweetsActivity.this, R.layout.rss_items, tweets);  
    		if (aa.isEmpty())
    		{
    			Log.v("aa ", "is empty");
    			TextView errors = (TextView) RayTweetsActivity.this.findViewById(R.id.errors);
    			errors.setText("Unable to retrieve Ray tweets.  Please make sure you " +
    					"have an active network or cell connection and try again later.");
    			
    		}
    		else
    		{
    			Log.v("aa count ", String.valueOf(aa.getCount()));
    		}

    		//Bind array adapter to the list  
    		lv1.setAdapter(aa); 	

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
            	Log.v("Ray Tweets Activity Exception", e.getMessage());
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
        	Log.v("Ray Tweets Activity onPause", e.getMessage());
        }

		super.onPause();
	}
	
 
}
