/**
 * 	06/20/2011 : Bryan Osterkamp - Added Map, Home Group, and Contact Us Activities
 */

//Run emulator 10c
package org.oakwoodbaptist.mobile;

import org.oakwoodbaptist.mobile.map.route.MapRouteActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Represents the main activity.
 * @author Bryan Osterkamp
 *
 */
public class OakwoodMainActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        Log.v("OakwoodMainActivity", "starting app");
        
        //Bible Verses Button
        Button versesButton = (Button) findViewById(R.id.bible_verse_button);
        versesButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchBibleVerseActivity();				
			}
		});
        
        //Sermon Button
        Button sermonButton = (Button) findViewById(R.id.sermon_button);
        sermonButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchSermonSelectionActivity();				
			}
		});
        
        //Calendar Button  
        Button calendarButton = (Button) findViewById(R.id.calendar_button);
        calendarButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchCalendarActivity();				
			}
		});
		        
        //Ray Tweets Button
        Button rayTweetsButton = (Button) findViewById(R.id.eray_button);
        rayTweetsButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchRayTweetsActivity();				
			}
		});
        
        //Address Button
        Button addressButton = (Button) findViewById(R.id.address_button);
        addressButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchMapActivity();				
			}
		});
        
        /*
        //eRay Button
        Button eRayButton = (Button) findViewById(R.id.eray_button);
        eRayButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				launchERayActivity();				
			}
		});
		*/
    
        //Devotionals Button
//        Button devotionalsButton = (Button) findViewById(R.id.devotionals_button);
//        devotionalsButton.setOnClickListener(new View.OnClickListener() 
//        {
//			
//			@Override
//			public void onClick(View v) 
//			{
//				launchDevotionalSelectionActivity();				
//			}
//		});
        
    }
    
    
    //Launch the calendar activity
    private void launchCalendarActivity()
    {
    	//modified for updated sermon
    	Intent launchCalendar = new Intent(OakwoodMainActivity.this, CalendarActivity.class);
    	startActivity(launchCalendar);
    }
    
    //Launch the Ray Tweets activity
    private void launchRayTweetsActivity()
    {
    	//Launch a new activity
    	Intent launchRayTweets = new Intent(OakwoodMainActivity.this, RayTweetsActivity.class);
    	startActivity(launchRayTweets);
    }
    
    //Launch the eray activity
    private void launchERayActivity()
    {
    	//Launch a new activity
    	Intent launchERay = new Intent(OakwoodMainActivity.this, ERayActivity.class);
    	startActivity(launchERay);
    }
    
    //Launch the devotional selection activity
    private void launchDevotionalSelectionActivity()
    {
    	//Launch a new activity
    	//commented out to try the mediacontroller
    	Intent launchDevotionalSelection = new Intent(OakwoodMainActivity.this, DevotionalSelectionActivity.class);
    	startActivity(launchDevotionalSelection);
    }
    
    //Launch the Sermon activity
    private void launchSermonSelectionActivity()
    {
    	//modified for updated sermon
    	Intent launchSermon = new Intent(OakwoodMainActivity.this, SermonSelectionActivity.class);
    	startActivity(launchSermon);
    }

    //Launch the bible verse activity
    private void launchBibleVerseActivity()
    {
    	//Launch a new activity
    	Intent launchBibleVerse = new Intent(OakwoodMainActivity.this, BibleVerseActivity.class);
    	startActivity(launchBibleVerse);
    }
    
    //Launch the map activity
    private void launchMapActivity()
    {
    	//Check to see if GPS is enabled, if it is, go to the route activity
    	//if it is not, just show the location.
//    	Criteria criteria = new Criteria();
//	    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//	    criteria.setPowerRequirement(Criteria.POWER_LOW);
//	    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    	
	    //Change to NETWORK_PROVIDER TO READ FOR WIRELESS NETWORKS...
	    //Still not working...
//	    String locationProvider = LocationManager.NETWORK_PROVIDER;
//	    	manager.getBestProvider(criteria, true);
    	  
//    	 Location mLocation = manager.getLastKnownLocation(locationProvider);
//    	 if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) || mLocation == null) {         
//    		 buildAlertMessageNoGps();     
//    		 }
    	 
    	 Intent launchMap = null;
    	 
    	 //Commenting out until I can work on this...
//    	 if (mLocation == null)
//    	 {
    		 launchMap = new Intent(OakwoodMainActivity.this, HelloMapViewActivity.class);
//    	 }
    	  //Show route
//    	 else
//    	 {
    	    	//Launch a new activity
//    	     launchMap = new Intent(OakwoodMainActivity.this, MapRouteActivity.class);
//    	    	Intent launchMap = new Intent(OakwoodMainActivity.this, HelloMapViewActivity.class);
//    	    	Intent launchMap = new Intent(OakwoodMainActivity.this, OakwoodAddressActivity.class);
    		 
//    	 }
    	  
    	 startActivity(launchMap);
    }
    
    //Commenting out, since we don't get location right now.
    //This is used when GPS is disabled.
//    private void buildAlertMessageNoGps() {     
//		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);     
//		 builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")            
//		 .setCancelable(false)            
//		 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                
//			 public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, 
//					 @SuppressWarnings("unused") final int id) {                    
//				 startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));                 
//				 }            
//			 })            
//			 .setNegativeButton("No", new DialogInterface.OnClickListener() {                
//				 public void onClick(final DialogInterface dialog, 
//						 @SuppressWarnings("unused") final int id) {                     
//					 dialog.cancel();                
//					 }            
//				 });     
//		 final AlertDialog alert = builder.create();     
//		 alert.show(); 
//		 }
    
}