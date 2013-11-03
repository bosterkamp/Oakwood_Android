package org.oakwoodbaptist.mobile.map.route;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtility {

	public static double[] getLastKnownLocation(LocationManager mLocationManager)
	{
		
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	    //Change to NETWORK_PROVIDER TO READ FOR WIRELESS NETWORKS...
	    //Still not working...
	    String locationProvider = LocationManager.NETWORK_PROVIDER;
//	    	mLocationManager.getBestProvider(criteria, true);
	    Location mLocation =
	    	mLocationManager.getLastKnownLocation(locationProvider);
	    
//	    double[] coords = null;
	    double[] coords = new double[2];
	    
	    //Check to make sure we have a known location, otherwise we return null
//	    if (mLocation != null)
//	    {
//	    	coords = new double[2];
	       	coords[0] = mLocation.getLatitude();
	    	coords[1] = mLocation.getLongitude();
//	    }
	    return coords;
    
	}
	
}
