package org.oakwoodbaptist.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerController extends Activity {
	
	String url;
	String title_string;
	TextView title;
	VideoView videoView;
	int previous_duration = 0;
	private boolean isAudioLoaded = false;
	private static final String PREFS_NAME = "MyPrefsFile";
	private static final String PREFS_TITLE = "PREFS_TITLE";
	private static final String PREFS_TITLE_DEF = "PREFS_TITLE_DEF";
	private static final String PREFS_DURATION = "PREFS_DURATION";
	
	@Override     
	public void onCreate(Bundle savedInstanceState) {         
		super.onCreate(savedInstanceState);          
		setContentView(R.layout.video);          
		videoView = (VideoView) findViewById(R.id.VideoView);
		final TextView control = (TextView) findViewById(R.id.control_note);
		
        Intent i = getIntent();
        if (i != null)
        {
			//Get from intent
	    	Bundle bundle = i.getExtras();
	    	url = bundle.getString("mediaUrl");
	    	title_string = bundle.getString("sermontitle");
	    	title = (TextView) this.findViewById(R.id.sermon_title);
	    	title.setText(title_string);		
			//End
	    	
        }
		
        control.setText("Buffering Audio...");
        
		final MediaController mediaController = new MediaController(this);         
		mediaController.setAnchorView(videoView); 
		
		// Set video link (mp4 format )         
		Uri video = Uri.parse(url);
		videoView.setMediaController(mediaController);
		videoView.setVideoURI(video);     
		
		
//		videoView.setOnPreparedListener(l)
		
		videoView.setOnPreparedListener(new OnPreparedListener()          
		{                        
			public void onPrepared(MediaPlayer mp)              
			{                 

		        // Restore preferences
		        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		        String saved_title = settings.getString(PREFS_TITLE, PREFS_TITLE_DEF);
		        Log.v("VPC", "onPrepared Previous Title: " + saved_title);
		        Log.v("VPC", "onPrepared Previous Duration: " + previous_duration);
		        
		        
		        if (saved_title.equalsIgnoreCase(title_string))
		        {
		        	previous_duration = settings.getInt(PREFS_DURATION, 0);
		        	Log.v("VPC", "onPrepared Previous Duration: " + previous_duration);
		        }
				
				
				if (previous_duration != 0)
				{
					videoView.seekTo(previous_duration);
				}
				mediaController.show(10000);
				control.setText("");
				isAudioLoaded = true;
			}                    
		});
		
//			}
//		}
		
		videoView.start();
		
		}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Log.v("VPC", "onPause isAudioLoaded: " + isAudioLoaded);
		
		if (isAudioLoaded)
		{
			
		      // We need an Editor object to make preference changes.
		      // All objects are from android.context.Context
		      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		      SharedPreferences.Editor editor = settings.edit();
		      editor.putString(PREFS_TITLE, title_string);
		      editor.putInt(PREFS_DURATION, videoView.getCurrentPosition());

		      Log.v("VPC", "onPause duration: " + previous_duration);
		      
		      // Commit the edits!
		      editor.commit();

		}
	} 
} 
