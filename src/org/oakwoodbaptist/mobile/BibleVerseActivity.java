package org.oakwoodbaptist.mobile;


import java.util.ArrayList;

import org.oakwoodbaptist.mobile.data.BibleVerseRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Represents the activity of showing the daily bible verse.
 * @author Bryan Osterkamp
 *
 */
public class BibleVerseActivity extends Activity implements Runnable  {

	//Same URL as the Sermon, because the Bible Verses are shoved in the Sermon feed...
	//private static final String BIBLE_VERSE_URL = "http://www.oakwoodbaptist.org/category/sermons/feed/podcast";
	private static final String BIBLE_VERSE_URL = "http://oakwoodnb.com/sermons/feed";
	private static final String TAG = "BibleVerseActivity";
	
	ArrayList<BibleVerseRssItem> rss = new ArrayList();
	
	private ProgressDialog pd;
	private boolean dialogComplete = true;
	
	ListView lv1;
	TextView tv1;
	TextView bible_verses_view;
	TextView disclaimer;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible_verse);
        
        
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
	    pd.setIndeterminate(true);
	    pd.setCancelable(false);
	    pd.show();
	    dialogComplete = false;
      
      
      Thread thread = new Thread(this);
      thread.start();

    }
    
    
    public void run() 
    {
        try
        {
        	rss = RssReader.processBibleVerseRssResponse(BIBLE_VERSE_URL);
        	handler.sendEmptyMessage(0);
        }
        catch(Exception e)
        {
//        	e.printStackTrace();
        	Log.v("BibleVerseActivity", " failure");
        	handler.sendEmptyMessage(0);
        }

    }
    private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//               	Log.v("BVR" + rss.size(), "size " + rss.size());
            	
            	bible_verses_view = (TextView) findViewById(R.id.bible_verses_text);
            	disclaimer = (TextView) findViewById(R.id.bible_verse_disclaimer);
            	
            	try {
					if (rss != null)
					{
						if (rss.get(0) != null)
						{
							//Set the text in HTML format        			
							bible_verses_view.setText(Html.fromHtml(rss.get(0).toString()));
							//Make the links clickable
							bible_verses_view.setMovementMethod(LinkMovementMethod.getInstance());
							disclaimer.setText("Note: Tapping on a verse will redirect you to mobile.biblegateway.com.");
						}
						else
						{
							Log.v(TAG, "rss.get(0) is null");
							bible_verses_view.setText("Unable to retrieve bible verses. Please make sure you " +
									"have an active network or cell connection and try again later.");
						}
					}
					else
					{
						Log.v(TAG, "rss is null");
						bible_verses_view.setText("Unable to retrieve bible verses. Please make sure you " +
								"have an active network or cell connection and try again later.");
					}
				} catch (Exception e) {
					bible_verses_view.setText("Unable to retrieve bible verses. Please make sure you " +
					"have an active network or cell connection and try again later.");
					e.printStackTrace();
				}
     
            	
		        //swallow the exception, but we shouldn't get one.
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
		protected void onPause() {
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
            	Log.v("Bible Verse Activity onDetachedFromWindow", e.getMessage());
            }
			super.onPause();
		}
        
    	
}
