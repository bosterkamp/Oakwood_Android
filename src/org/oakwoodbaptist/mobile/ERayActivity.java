package org.oakwoodbaptist.mobile;

import java.util.ArrayList;

import org.oakwoodbaptist.mobile.data.ERayRssItem;
import org.oakwoodbaptist.mobile.reader.RssReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ERayActivity extends Activity implements Runnable {

//	WebView mWebView;

	private ProgressDialog pd;
	private boolean dialogComplete;
	
	TextView tv1;
	TextView tv_title;
//	ListView lv1;
	ArrayList<ERayRssItem> rss = new ArrayList();
//	ArrayAdapter<String> aa = null; /
	
//	private static final String ERAY_URL = "http://www.oakwoodbaptist.org/tags/eRay/feed/rss";
	private static final String ERAY_URL = "http://oakwoodnb.com/eray/feed";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("ERayActivity", "before content view setup.");
        
        setContentView(R.layout.eray);
        tv_title = (TextView) this.findViewById(R.id.eray_title_text);
        tv_title.setTypeface(null, Typeface.BOLD);
        tv1 = (TextView) this.findViewById(R.id.eray_text);
//        lv1 = (ListView) this.findViewById(R.id.rssListView);
        
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
	    pd.setIndeterminate(true);
	    pd.setCancelable(false);
	    pd.show();
      
      
      Thread thread = new Thread(this);
      thread.start();
        
        
    }
    
    
    public void run() {
        
        try
        {
            Log.v("ERayActivity", "about to get intent");
            
            //View Another ERay Button
            Button eRayButton = (Button) findViewById(R.id.view_another_eray);
            eRayButton.setOnClickListener(new View.OnClickListener() 
            {
    			
    			@Override
    			public void onClick(View v) 
    			{
    		    	Intent launchERaySelection = new Intent(ERayActivity.this, ERaySelectionActivity.class);
    		    	startActivity(launchERaySelection);				
    			}
    		});
            
            

            handler.sendEmptyMessage(0);
        	
        }
        catch(Exception e)
        {
        	Log.v("failure", "e=" + e.getMessage());
        }
        }
    
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
        	
            Intent i = getIntent();
            try {
				if (i != null)
				{
					Log.v("ERayActivity", "intent is not null");
					
					Bundle bundle = i.getExtras();
					
					
					
					//Initialize url.
//            	String url = null;
					String title = null;
					if (bundle != null)
					{
						//let's try to use the title, not the url to match against
//                	url = bundle.getString("url");
						title = bundle.getString("title");
				    	Log.v("ERayActivity", "bundle not equal null");
				    	
				    	Log.v("ERayActivity", "title: " + title);
				    	
				    	
				    	try
				    	{
				    		rss = RssReader.processERayRssResponse(ERAY_URL);	
				    	}
						catch (Exception e) 
						{
							Log.v("ERayActivity", "processRSS  for bundle not null failed.");
							tv1.setText("Unable to retrieve eRay. Please make sure you " +
							"have an active network or cell connection and try again later.");
						} 
				    	
				    	
				    	for (int j = 0; j < rss.size(); j++) {
							if (title.equals(rss.get(j).getTitle()))
							{
								Log.v("ERayActivity", "right title: " + title);
								tv_title.setText(Html.fromHtml(rss.get(j).getTitle()));
								tv1.setText(Html.fromHtml(rss.get(j).getDescription()));
							}
							else
							{
								Log.v("ERayActivity", "wrong title: " + rss.get(j).getTitle());
							}
						}
				    	
//                	tv1.setText(Html.fromHtml(rss.get(0).getDescription()));
					}
					else
					{
						//If there are no extras, show the latest eRay
						try 
						{
							rss = RssReader.processERayRssResponse(ERAY_URL);
							Log.v("ERayActivity", "after process");
							tv_title.setText(Html.fromHtml(rss.get(0).getTitle()));
							
							tv1.setText(Html.fromHtml(rss.get(0).getDescription()));
						} 
						catch (Exception e) {
							Log.v("ERayActivity", "processRSS Failed.");
							tv1.setText("Unable to retrieve eRay. Please make sure you " +
							"have an active network or cell connection and try again later.");
						} 
						
						
					}
					
					
				}
				else
				{
					Log.v("ERayActivity", "null intent");
					tv1.setText("Unable to retrieve eRay. Please make sure you " +
					"have an active network or cell connection and try again later.");
				}
			} catch (Exception e) {
				tv1.setText("Unable to retrieve eRay. Please make sure you " +
						"have an active network or cell connection and try again later.");
				e.printStackTrace();
			}
			tv1.setMovementMethod(new ScrollingMovementMethod());

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
            	Log.v("Eray Activity ", e.getMessage());
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
        	Log.v("Eray Activity onPause", e.getMessage());
        }
		super.onPause();
	}
        	
}
