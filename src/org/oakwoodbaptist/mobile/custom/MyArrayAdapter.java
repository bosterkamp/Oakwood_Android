/**
 * 
 */
package org.oakwoodbaptist.mobile.custom;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Bryan Osterkamp
 * @param <T>
 *
 */
public class MyArrayAdapter<T> extends ArrayAdapter<T> {

	public MyArrayAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
	View view = super.getView(position, convertView, parent);  
	if (position % 2 == 1) {
	    view.setBackgroundColor(Color.GRAY);  
	} else {
	    view.setBackgroundColor(Color.DKGRAY);  
	}

	return view;  
	}

}
