/**
 * 	03/06/2011 : Bryan Osterkamp - Creation
 */
package org.oakwoodbaptist.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class SermonService extends Service 
{
	
	private static final String TAG = "SermonService";	
	MediaPlayer player;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override	
	public void onCreate() {		
//		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();		
		Log.d(TAG, "onCreate");				
//		player = 
//			MediaPlayer.create(this, R.raw.);		
//		player.setLooping(false); // Set looping	
		}	
	
	@Override	
	public void onDestroy() 
	{		
//		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();		
		Log.d(TAG, "onDestroy");		
//		player.stop();	
	}		
	
	@Override	
	public void onStart(Intent intent, int startid) 
	{		
//		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();		
		Log.d(TAG, "onStart");		
//		player.start();	
	}

}
