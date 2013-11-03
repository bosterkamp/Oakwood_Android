package org.oakwoodbaptist.mobile.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * MediaPlayer does not yet support streaming from external URLs so this class provides a pseudo-streaming function
 * by downloading the content incrementally & playing as soon as we get enough audio in our temporary storage.
 */
public class StreamingMediaPlayer {

    private static final int INTIAL_KB_BUFFER =  96*20/8;//assume 96kbps*20secs/8bits per byte

	private TextView textStreamed;
	private TextView currentDuration;
	
	//Added for keeping track of duration in minutes and seconds
	private int minutes;
	private int seconds;
	private int total_seconds;
	
	private ImageButton playButton;
	
	private ProgressBar	progressBar;
	
	//  Track for display by progressBar
	private long mediaLengthInKb, mediaLengthInSeconds;
	private int totalKbRead = 0;
	
	// Create Handler to call View updates on the main UI thread.
	private final Handler handler = new Handler();

	private MediaPlayer 	mediaPlayer;
	
	private File downloadingMediaFile; 
	
	private boolean isInterrupted;
	
	private Context context;
	
	private int counter = 0;
	
 	public StreamingMediaPlayer(Context  context,TextView textStreamed, ImageButton	playButton, 
 			Button	streamButton,ProgressBar progressBar, TextView currentDuration) 
 	{
 		this.context = context;
		this.textStreamed = textStreamed;
		this.playButton = playButton;
		this.progressBar = progressBar;
		this.currentDuration = currentDuration;
	}
	
    /**  
     * Progressivly download the media to a temporary location and update the MediaPlayer as new content becomes available.
     */  
    public void startStreaming(final String mediaUrl, long	mediaLengthInKb, long	mediaLengthInSeconds) throws IOException {
    	
    	this.mediaLengthInKb = mediaLengthInKb;
    	this.mediaLengthInSeconds = mediaLengthInSeconds;
    	
		Runnable r = new Runnable() {   
	        public void run() {   
	            try {   
	        		downloadAudioIncrement(mediaUrl);
//	        		Log.v("startStreaming", "...");
	            } catch (IOException e) {
	            	Log.e(getClass().getName(), "Unable to initialize the MediaPlayer for fileUrl=" + mediaUrl, e);
	            	return;
	            }   
	        }   
	    };   
	    new Thread(r).start();
    }
    
    /**
     * Fast Forward
     */
    public void fastFoward()
    {
    	Runnable updater = new Runnable() {   
	        public void run() {   
	            try {   
	        		//Seek to?
					int currentPos = mediaPlayer.getCurrentPosition();
					Log.v("run", "current pos " + currentPos);
					
					//Need to figure out why this is always true, even after pushing play/pause
					while (!isInterrupted)
					{
						currentPos=currentPos+1;
						mediaPlayer.seekTo(currentPos);
						
						//Need to update current duration text and figure out why 
						//thread is hanging.
						Log.v("run", "interrupted " + isInterrupted);
						Log.v("run", "fwding " + mediaPlayer.getCurrentPosition());
					}
//	        		Log.v("startStreaming", "...");
	            } catch (Exception e) {
	            	Log.e(getClass().getName(), "Unable to fast foward", e);
	            	return;
	            }   
	        }   
	    };   
	    handler.post(updater);
    }
    
    /**  
     * Download the url stream to a temporary location and then call the setDataSource  
     * for that local file
     */  
    public void downloadAudioIncrement(String mediaUrl) throws IOException {
    	
    	Log.v("downloadAudioIncrement", "before openConnection call");
    	
    	//Looks like it is failing here...
//    	URLConnection cn = new URL(mediaUrl).openConnection();
    	
    	URL url = new URL(mediaUrl);   
    	HttpURLConnection cn = (HttpURLConnection) url.openConnection();   
    	
    	
//    	HttpURLConnection cn = new (mediaUrl).openConnection();
        cn.connect();   
        
        InputStream stream = cn.getInputStream();
        
        if (stream == null) {
        	Log.e(getClass().getName(), "Unable to create InputStream for mediaUrl:" + mediaUrl);
        }

        downloadingMediaFile = new File(context.getCacheDir(),"downloadingMedia_" + (counter++) + ".dat");
        FileOutputStream out = new FileOutputStream(downloadingMediaFile);   
        
        //Showing this is a file...debugging
        Log.v("FOS is this a file?", "" + downloadingMediaFile.isFile());

        
        byte buf[] = new byte[16384];
        int totalBytesRead = 0, incrementalBytesRead = 0;
        do {
        	int numread = stream.read(buf);   
            if (numread <= 0)   
                break;   
            out.write(buf, 0, numread);
            totalBytesRead += numread;
            incrementalBytesRead += numread;
            totalKbRead = totalBytesRead/1000;
            
            testMediaBuffer();
           	fireDataLoadUpdate();
        } while (validateNotInterrupted());   

       	stream.close();
        if (validateNotInterrupted()) {
	       	fireDataFullyLoaded();
        }

        //Showing this is a file...debugging
        Log.v("after stream... is this a file?", "" + downloadingMediaFile.isFile());

    
    }  

    private boolean validateNotInterrupted() {
		if (isInterrupted) {
			if (mediaPlayer != null) 
			{

				
				//uncommented to try and release properly
				mediaPlayer.release();
				
				//delete the files after we release the media player, so we free up the memory
				Log.v("VNI", "about to delete saved files");
				
				//(context.getCacheDir().toString());
				
				Log.v("Count of files: ", "" + counter);
				
				//add for loop
				for (int i = 1; i <= counter; i++) 
				{
					String fileLocPlaying = context.getCacheDir() + "/playingMedia" + (i) + ".dat";
//					String fileLocDownloading = context.getCacheDir() + "/downloadingMedia_" + (i) + ".dat";
					
					File filePlaying = new File(context.getCacheDir(),"playingMedia" + (i) + ".dat");
//					File fileDownloading = new File(context.getCacheDir(),"downloadingMedia_" + (i) + ".dat");
					
					Log.v("is this a file (playing)?", "" + filePlaying.isFile());
//					Log.v("is this a file (downloading)?", "" + fileDownloading.isFile());
					
					boolean deletedFilePlaying = filePlaying.delete();
//					boolean deletedFileDownloading = fileDownloading.delete();
					
					Log.v("File deleted (playing) " + fileLocPlaying + "?", "" + deletedFilePlaying);
//					Log.v("File deleted (downloading)" + fileLocDownloading + "?", "" + deletedFileDownloading);
				}
				
				
			}
			Log.v("validateNotInterrupted", "before false");
			return false;
		} else {
//			Log.v("validateNotInterrupted", "before true");
			return true;
		}
    }

    
    /**
     * Test whether we need to transfer buffered data to the MediaPlayer.
     * Interacting with MediaPlayer on non-main UI thread can causes crashes to so perform this using a Handler.
     */  
    private void  testMediaBuffer() {
	    Runnable updater = new Runnable() {
	        public void run() {
	            if (mediaPlayer == null) {
	            	//  Only create the MediaPlayer once we have the minimum buffered data
	            	if ( totalKbRead >= INTIAL_KB_BUFFER) {
	            		try {
		            		startMediaPlayer();
	            		} catch (Exception e) {
	            			Log.e(getClass().getName(), "Error copying buffered conent.", e);    			
	            		}
	            	}
	            } else if (!isInterrupted && mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000 ){ 
	            	//  NOTE:  The media player has stopped at the end so transfer any existing buffered data
	            	//  We test for < 1second of data because the media player can stop when there is still
	            	//  a few milliseconds of data left to play
	            	transferBufferToMediaPlayer();
	            }
	        }
	    };
//	    Log.v("testMediaBuffer", "...");
	    handler.post(updater);
    }
    
    private void startMediaPlayer() {
        try {   
        	File bufferedFile = new File(context.getCacheDir(),"playingMedia" + (counter++) + ".dat");
        	moveFile(downloadingMediaFile,bufferedFile);
    		
        	Log.v("Player", "Length: " + bufferedFile.length()+"");
        	Log.v("Player", "Path: " + bufferedFile.getAbsolutePath());
        	
        	//Using "File Descriptor"
        	FileInputStream fis = new FileInputStream(bufferedFile.getAbsolutePath()); 
        	
    		mediaPlayer = new MediaPlayer();
        	mediaPlayer.setDataSource(fis.getFD());
//    		mediaPlayer.setDataSource(bufferedFile.getAbsolutePath());
        	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        	mediaPlayer.prepare();
    		Log.v("Player", "After Prepare.");
    		fireDataPreloadComplete();
        	
        } catch (IOException e) {
        	Log.e(getClass().getName(), "Error initializing the MediaPlaer.", e);
        	return;
        }   
    }
    
    /**
     * Transfer buffered data to the MediaPlayer.
     * Interacting with MediaPlayer on non-main UI thread can causes crashes to so perform this using a Handler.
     */  
    private void transferBufferToMediaPlayer() {
	    
    	Log.v("transferBufferToMediaPlayer", "entering method...");
    	
    	try {
	    	// First determine if we need to restart the player after transferring data...e.g. perhaps the user pressed pause
	    	boolean wasPlaying = mediaPlayer.isPlaying();
	    	int curPosition = mediaPlayer.getCurrentPosition();
	    	
	    	Log.v("transferBufferToMediaPlayer", "current position: " + curPosition);
	    	
	    	mediaPlayer.pause();

        	File bufferedFile = new File(context.getCacheDir(),"playingMedia" + (counter++) + ".dat");
	    	//FileUtils.copyFile(downloadingMediaFile,bufferedFile);
        	
        	
        	moveFile(downloadingMediaFile,bufferedFile);
        	
        	Log.v("transferBufferToMediaPlayer", "File: " + bufferedFile);
        	
        	//Using "File Descriptor"
        	FileInputStream fis = new FileInputStream(bufferedFile.getAbsolutePath()); 

        	
			mediaPlayer = new MediaPlayer();
			
			Log.v("transferBufferToMediaPlayer", "before setDataSource");
			
        	mediaPlayer.setDataSource(fis.getFD());
//    		mediaPlayer.setDataSource(bufferedFile.getAbsolutePath());

        	Log.v("transferBufferToMediaPlayer", "before prepare");
        	
			//mediaPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
    		mediaPlayer.prepare();
    		
    		Log.v("transferBufferToMediaPlayer", "before seekTo");
    		mediaPlayer.seekTo(curPosition);
    		
    		//  Restart if at end of prior buffered content or mediaPlayer was previously playing.  
    		//	NOTE:  We test for < 1second of data because the media player can stop when there is still
        	//  a few milliseconds of data left to play
    		Log.v("getDuration", "checking end of file");
    		boolean atEndOfFile = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000;
        	if (wasPlaying || atEndOfFile){
        		mediaPlayer.start();
        	}
		}catch (Exception e) {
	    	Log.e(getClass().getName(), "Error updating to newly loaded content.", e);            		
		}
    }
    
    private void fireDataLoadUpdate() {
    	
		Runnable updater = new Runnable() {
	        public void run() {
	        	
	        	//Commented out so this isn't seen on the screen
	        	//textStreamed.setText((CharSequence) (totalKbRead + " Kb read"));
	        	
	        	float loadProgress = ((float)totalKbRead/(float)mediaLengthInKb);
	    		progressBar.setSecondaryProgress((int)(loadProgress*100));
	        }
	    };
//	    Log.v("fireDateLoadUpdate", "...");
	    handler.post(updater);
    }
    
    /**
     * We have preloaded enough content and started the MediaPlayer so update the buttons & progress meters.
     */
    private void fireDataPreloadComplete() {
    	Runnable updater = new Runnable() {
	        public void run() {
	    		mediaPlayer.start();
	    		textStreamed.setText("");
	    		startPlayProgressUpdater();
	        	playButton.setEnabled(true);
	        }
	    };
//	    Log.v("fireDataPreloadComplete", "...");
	    handler.post(updater);
    }

    private void fireDataFullyLoaded() {
		Runnable updater = new Runnable() { 
			public void run() {
   	        	transferBufferToMediaPlayer();
	        	textStreamed.setText((CharSequence) ("Audio fully loaded: " + totalKbRead + " Kb read"));
	        }
	    };
//	    Log.v("fireDataFullyDataLoaded", "...");
	    handler.post(updater);
    }
    
    public MediaPlayer getMediaPlayer() {
    	return mediaPlayer;
	}
	
    public void startPlayProgressUpdater() {
    	
    	//added for interrupt...
    	
    	if (!isInterrupted)
    	{
	    	float progress = (((float)mediaPlayer.getCurrentPosition()/1000)/(float)mediaLengthInSeconds);
	    	progressBar.setProgress((int)(progress*100));
	    	
	    	total_seconds = mediaPlayer.getCurrentPosition()/1000;

	    	//Formatting to minutes and seconds
    		minutes = total_seconds / 60;
    		seconds = total_seconds - (60 * minutes);
	    	
    		String prepend_zero = "";
    		if (seconds < 10)
    		{
    			prepend_zero = "0";
    		}
    		
	    	currentDuration.setText(minutes + ":" + prepend_zero + seconds);  //mediaPlayer.getCurrentPosition());
			if (mediaPlayer.isPlaying()) {
				Runnable notification = new Runnable() {
			        public void run() {
			        	startPlayProgressUpdater();
					}
			    };
//			    Log.v("startPlayProgressUpdater", "...");
			    handler.postDelayed(notification,1000);
	    	}
    	}
    }    
    
    public void interrupt() {
    	playButton.setEnabled(false);
    	isInterrupted = true;
    	validateNotInterrupted();
    }
    
	public void moveFile(File	oldLocation, File	newLocation)
	throws IOException {

		if ( oldLocation.exists( )) {
			BufferedInputStream  reader = new BufferedInputStream( new FileInputStream(oldLocation) );
			BufferedOutputStream  writer = new BufferedOutputStream( new FileOutputStream(newLocation, false));
            try {
		        byte[]  buff = new byte[8192];
		        int numChars;
		        while ( (numChars = reader.read(  buff, 0, buff.length ) ) != -1) {
		        	writer.write( buff, 0, numChars );
      		    }
            } catch( IOException ex ) {
				throw new IOException("IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
            } finally {
                try {
                    if ( reader != null ){
                    	writer.close();
                        reader.close();
                    }
                } catch( IOException ex ){
				    Log.e(getClass().getName(),"Error closing files when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() ); 
				}
            }
        } else {
			throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() );
        }
	}
	
	//Create a method that stops and releases the media player and posts to the task.
	//Can we kill the task too?
	public void finishMediaPlayer()
	{
		
		Runnable updater = new Runnable() 
		{ 
			public void run() 
			{
//				mediaPlayer.stop();
				Log.v("finishMediaPlayer", "about to interrupt");
				interrupt();
				Log.v("finishMediaPlayer", "about to release");
				mediaPlayer.release();
				Log.v("finishMediaPlayer", "released");
	        }
	    };

	    handler.post(updater);

	}
	
}
