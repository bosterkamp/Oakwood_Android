package org.oakwoodbaptist.mobile.map.route;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.oakwoodbaptist.mobile.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapRouteActivity extends MapActivity {      
	LinearLayout linearLayout;  
	MapView mapView;  
	private Road mRoad; 
	double coords[];
	
	@Override  
	public void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.route);   
		mapView = (MapView) findViewById(R.id.mapview);   
		mapView.setBuiltInZoomControls(true);       
		
		LocationManager mLocationManager;
		
        mLocationManager = (LocationManager) 
    	getSystemService(Context.LOCATION_SERVICE);
		
		//Get current location
		coords = LocationUtility.getLastKnownLocation(mLocationManager);
		
		new Thread() {    
			
			@Override    
			public void run() {   

				//Initialize to Oakwood's address in case GPS isn't turned on.
				//We shouldn't hit this since we have a check up front,
				//but just in case.
//				double fromLat = 29.731738, fromLon = -98.132973;
				
//				if (coords != null)
//				{
//					fromLat = coords[0];
//					fromLon = coords[1];
//				}
				
				//Public Library
				double fromLat = coords[0], fromLon = coords[1];
				double toLat = 29.731738, toLon = -98.132973;
				String url = RoadProvider       
				.getUrl(fromLat, fromLon, toLat, toLon);     
				InputStream is = getConnection(url);     
				mRoad = RoadProvider.getRoute(is);     
				mHandler.sendEmptyMessage(0);    
				}   
			}.start();  
			}

	Handler mHandler = new Handler() {   
		public void handleMessage(android.os.Message msg) {    
			TextView textView = (TextView) findViewById(R.id.description);    
			textView.setText(mRoad.mName + " " + mRoad.mDescription);    
			MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);    
			List<Overlay> listOfOverlays = mapView.getOverlays();    
			listOfOverlays.clear();    
			listOfOverlays.add(mapOverlay);    
			mapView.invalidate();   };  };   
			
			private InputStream getConnection(String url) {   
				InputStream is = null;   
				try {    
					URLConnection conn = new URL(url).openConnection();    
					is = conn.getInputStream();   
					} 
				catch (MalformedURLException e) {    
					e.printStackTrace();   
					} 
				catch (IOException e) {    
					e.printStackTrace();   
					}   return is;  }      
			
			@Override  
			protected boolean isRouteDisplayed() {   
				return false;  
				} 
			} 

//

	class MapOverlay extends com.google.android.maps.Overlay {  
		Road mRoad;  
		ArrayList<GeoPoint> mPoints;    
		public MapOverlay(Road road, MapView mv) {  
			mRoad = road;  
			if (road.mRoute.length > 0) {  
				mPoints = new ArrayList<GeoPoint>();  
				for (int i = 0; i < road.mRoute.length; i++) {  
					mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),  
							(int) (road.mRoute[i][0] * 1000000)));  
					}  
				int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(  
						mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)  
						.getLatitudeE6()) / 2);  
				
				int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(  
						mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)  
						.getLongitudeE6()) / 2);  
				
				GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);    
				MapController mapController = mv.getController();  
				mapController.animateTo(moveTo);  
				mapController.setZoom(10);  
				}  
			}    
		
		@Override  
		public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {  
			super.draw(canvas, mv, shadow);  
			drawPath(mv, canvas);  
			return true;  
			}    
		
		public void drawPath(MapView mv, Canvas canvas) {  
			int x1 = -1, y1 = -1, x2 = -1, y2 = -1;  
			Paint paint = new Paint();  
			paint.setColor(Color.GREEN);  
			paint.setStyle(Paint.Style.STROKE);  
			paint.setStrokeWidth(3);  
			
			for (int i = 0; i < mPoints.size(); i++) {  
				Point point = new Point();  
				mv.getProjection().toPixels(mPoints.get(i), point);  
				x2 = point.x;  
				y2 = point.y;  
				
				if (i > 0) {  
					canvas.drawLine(x1, y1, x2, y2, paint);  
					}  
				
				x1 = x2;  
				y1 = y2;  
				}  
			}  
		}  