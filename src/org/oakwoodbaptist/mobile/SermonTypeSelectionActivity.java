/**
 * 	04/12/2015 : Bryan Osterkamp - Created activity to hold sermon type selection (audio or video)
 */

//Run emulator 10c
package org.oakwoodbaptist.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Represents the sermon type selection (audio or video) activity.
 * @author Bryan Osterkamp
 *
 */
public class SermonTypeSelectionActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.sermon_types);
        
        Log.v("SermonTypeSelectionActivity", "starting SermonTypeSelectionActivity");
        
        //Audio Button - waiting for image
        Button versesButton = (Button) findViewById(R.id.bible_verse_button);
        versesButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchVideoSermonsActivity();				
			}
		});
        
        //Video Button - waiting for image
        Button sermonButton = (Button) findViewById(R.id.sermon_button);
        sermonButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchSermonSelectionActivity();				
			}
		});
        
        
    }
    
    //Launch the Sermon activity
    private void launchSermonSelectionActivity()
    {
    	//modified for updated sermon
    	Intent launchSermon = new Intent(SermonTypeSelectionActivity.this, SermonSelectionActivity.class);
    	startActivity(launchSermon);
    }


    //Launch the video sermons
    private void launchVideoSermonsActivity()
    {
    	//Launch a new activity
    	startActivity(new Intent(Intent.ACTION_VIEW, 
    	Uri.parse("http://vimeo.com/channels/577936/")));		
    }
       
}