package org.oakwoodbaptist.mobile;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public HelloItemizedOverlay(Drawable arg0) {
//		super(arg0);
		super(boundCenter(arg0));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
//		return null;
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
//		return 0;
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {    
		mOverlays.add(overlay);    
		populate();
	}

}
