/**
 * 	02/17/2011 : Bryan Osterkamp - Creation
 */
package org.oakwoodbaptist.mobile.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
//import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oakwoodbaptist.mobile.data.JsonItem;
import org.oakwoodbaptist.mobile.data.RayTweetJsonItem;

import android.util.Base64;
import android.util.Log;

/**
 * This class performs json reader functionality.
 * @author Bryan Osterkamp
 *
 */
public class JsonReader {




	//Start Refactor
    /**
     * Return the JSON response based on the url passed in.
     */
	public static ArrayList<JsonItem> processJsonResponse(String feedUrl)
		throws MalformedURLException, IOException, IllegalStateException,
		JSONException, NoSuchAlgorithmException
	{
		
    	ArrayList<JsonItem> jsonItems = new ArrayList<JsonItem>();
		
	
	    StringBuilder responseBuilder = new StringBuilder();
	    Log.v("Search Request", feedUrl);
	    URL url = new URL(feedUrl);
	    
	    HttpURLConnection httpconn = 
	    		(HttpURLConnection) url.openConnection();
	    
	    //Check to make sure we got a connection.  Otherwise, we need to handle no connection.
	    if(httpconn.getResponseCode()==HttpURLConnection.HTTP_OK)
	    {
	    	BufferedReader input = new BufferedReader
	    		(new InputStreamReader(httpconn.getInputStream()), 8192);
	    	
	    	String strLine = null;
	    	while ((strLine = input.readLine()) != null)
	    	{
	    		responseBuilder.append(strLine);
	    	}
	    	input.close();
	    	Log.v("Response: ", responseBuilder.toString());
	    }
	    
	    String resp = responseBuilder.toString();

	    //Now process the response
//		StringBuilder sb = new StringBuilder();
//		Log.v("JSON Search", resp);
		JSONObject mResponseObject = new JSONObject(resp);
		JSONArray jsonArray = mResponseObject.getJSONArray("posts");
//		Log.v("JSON lines", "Length" + jsonArray.length());
		
		for (int i=0; i<jsonArray.length(); i++)
		{
			Log.v("result", i+" "+ jsonArray.get(i).toString());
			String title = jsonArray.getJSONObject(i).getString("title");
			String content = jsonArray.getJSONObject(i).getString("content");
//			String sanitizedContent = StringEscapeUtils.unescapeHtml(content);
//			sb.append(title);
//			sb.append("\n");
//			sb.append(content);
//			sb.append("\n");
			
			JsonItem jsonItem = new JsonItem(title, content);
            jsonItems.add(jsonItem);
		}

//		Log.v("StringBuilder", sb.toString());
	    
	    
		
	  return jsonItems;
	}
	
	/////
	
	/**
	 * Return the JSON response based on the url passed in.
	 * @param feedUrl
	 * @return
	 */
    public static ArrayList<RayTweetJsonItem> processTwitterResponse(String feedUrl) 
	{
    	ArrayList<RayTweetJsonItem> json = new ArrayList<RayTweetJsonItem>();
    	
    	try
    	{
    		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
    		HttpPost httppost = new HttpPost("https://api.twitter.com/oauth2/token");

    		final String APIKEY = "C9RBCzlygAApikTsS1u6aw";
    		final String APISECRET = "cVdwOx5rIxANARRL5vXSR02u6zWoeDcQzLUyXvmOGY";

    		String apiString = APIKEY + ":" + APISECRET;
    		String authorization = "Basic " + Base64.encodeToString(apiString.getBytes(), Base64.NO_WRAP);

    		httppost.setHeader("Authorization", authorization);
    		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    		httppost.setEntity(new StringEntity("grant_type=client_credentials"));

    		InputStream inputStream = null;
    		HttpResponse response = httpclient.execute(httppost);
    		HttpEntity entity = response.getEntity();

    		inputStream = entity.getContent();
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
    		StringBuilder sb = new StringBuilder();

    		String line = null;
    		while ((line = reader.readLine()) != null)
    		{
    		    sb.append(line + "\n");
    		    //Uncomment this to see the json string coming back
    		    //Log.v("Tweets", "token: " + sb.toString());
    		}
    		
    	json = generateTimeline(sb, feedUrl);
    	}
    	catch (Exception e)
    	{
    		Log.v("Tweets", "exception thrown: " + e.getMessage());
    	}
    	
		return json;
	}
    
	/**
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws UnsupportedEncodingException
	 */
	private static ArrayList<RayTweetJsonItem> generateTimeline(StringBuilder bearerToken, String url) throws IOException,
			ClientProtocolException, IllegalStateException,
			UnsupportedEncodingException {
		ArrayList<RayTweetJsonItem> tweets = new ArrayList<RayTweetJsonItem>();

		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpget = new HttpGet(url);
		StringBuilder bf = new StringBuilder("Bearer ");

		String bearer_token = ""; 
		JSONObject root;
		try {
			root = new JSONObject(bearerToken.toString());
			bearer_token = root.getString("access_token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v("Tweets", "exception thrown: " + e.getMessage());
		}
		
		bf.append(bearer_token);

		httpget.setHeader("Authorization", bf.toString());
		//Log.v("Tweets", "Authorization: " + bf.toString());
		httpget.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		inputStream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null)
		{
		 sb.append(line + "\n");
		 //Log.v("Tweets", sb.toString());
		}
		
		JSONArray root_response;
		
		// loop array
		try 
		{
			root_response = new JSONArray(sb.toString());
			//root_response.get(0);
			int tweetCount = root_response.length();
			int count = 0;
			
			//Log.v("Tweet count", ":" + tweetCount);
			
			
			//Iterate over all the tweets...
			while (count < tweetCount)
			{
				JSONObject jObj = (JSONObject) root_response.get(count);
				//Log.v("Tweet Text" + " #" + count, jObj.getString("text"));
				RayTweetJsonItem jsonItem = new RayTweetJsonItem(jObj.getString("text"), jObj.getString("created_at"));
				tweets.add(jsonItem);
				count++;
			}
			
			//TODO Need to figure out how to pull user and screen name inside entities
			//Log.v("First tweet user", jObj.getString("user"));	
			//JSONObject jUser = (JSONObject) root_response.get(0).getString("user");
		} 
		catch (JSONException e) 
		{
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return tweets;
	}
	
}
