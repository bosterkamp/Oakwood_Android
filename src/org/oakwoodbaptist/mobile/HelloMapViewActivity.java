package org.oakwoodbaptist.mobile;

import java.util.List;

import org.oakwoodbaptist.mobile.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class HelloMapViewActivity extends MapActivity {
	
	
	LinearLayout linearLayout;
	MapView mapView;
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	HelloItemizedOverlay itemizedOverlay;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.church_map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        //added to bring down to street level
//        mapView.setStreetView(true);
        mapView.setSatellite(true);
//        mapView.
        
        mapOverlays = mapView.getOverlays();
        
        //changed to obc leaf
//        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        drawable = this.getResources().getDrawable(R.drawable.oakwoodapp);
        itemizedOverlay = new HelloItemizedOverlay(drawable);
        
        MapController mc = mapView.getController();
        
//        GeoPoint point = new GeoPoint((int)(29.729949 * 1E6),(int)(-98.141727 * 1E6));
        GeoPoint point = new GeoPoint((int)(29.727649 * 1E6),(int)(-98.141727 * 1E6));
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        
        
        //Added to try to zoom
        mc.animateTo(point);
        mc.setZoom(17);
        
        
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
    }
    
    @Override
    protected boolean isRouteDisplayed() 
    {    
    	return false;
    }
}