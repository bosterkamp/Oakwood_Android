package org.oakwoodbaptist.mobile;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Represents the activity of showing the video devotional.
 * @author Bryan Osterkamp
 *
 */
public class VideoDevotionalActivity extends Activity {
	
    /**     
     * TODO: Set the path variable to a streaming video URL or a local media file path.     
     * */    
	
//	private String path = "http://player.vimeo.com/video/37749667";
//	private String path = "http://player.vimeo.com/video/37749667?player_id=player&title=0&byline=0&portrait=0&autoplay=1&api=1";
	private String path = "http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800";
	private WebView mWebView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_devotional_layout);        
        mWebView = (WebView) findViewById(R.id.devotional_web);        
        
        if (path == "") 
        {            
        	
        	// Tell the user to provide a media file URL/path.            
        	Toast.makeText(VideoDevotionalActivity.this, "Please edit VideoViewDemo Activity, and set path" 
        			+ " variable to your media file URL/path", Toast.LENGTH_LONG).show();        
     	} 
        else {            
    		/* Alternatively,for streaming media you can use 
    		 * mVideoView.setVideoURI(Uri.parse(URLstring));
    		 */            
//    		mVideoView.setVideoPath(path);            
//        	mVideoView.setVideoURI(Uri.parse(path));
//    		mVideoView.setMediaController(new MediaController(this));            
//    		mVideoView.requestFocus();        
            mWebView.getSettings().setJavaScriptEnabled(true);    
            
            
            mWebView.getSettings().setAppCacheEnabled(true); 
            mWebView.getSettings().setDomStorageEnabled(true);  
            
            // how plugin is enabled change in API 8 
            if (Build.VERSION.SDK_INT < 8) 
            {   
            	mWebView.getSettings().setPluginsEnabled(true); 
            } 
            else 
            {   
            	mWebView.getSettings().setPluginState(PluginState.ON); 
            } 
            
            
            //Need to figure out how to make this load the latest eRay.
            	mWebView.loadUrl(path);
    		}    
      }
    
}
