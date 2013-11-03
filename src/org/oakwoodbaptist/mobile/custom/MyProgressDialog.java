/**
 * 02/18/2012 : Bryan Osterkamp - Creation
 */
package org.oakwoodbaptist.mobile.custom;

import org.oakwoodbaptist.mobile.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

/**
 * @author Bryan Osterkamp
 * Custom Progress Dialog
 *
 */
public class MyProgressDialog extends Dialog 
{  
	public MyProgressDialog show(Context context, CharSequence title,         
			CharSequence message) 
			{     
				return show(context, title, message, false); 
			}  
	
	public MyProgressDialog show(Context context, CharSequence title,         
			CharSequence message, boolean indeterminate) 
			{     
				return show(context, title, message, indeterminate, false, null); 
			}  
	
	public MyProgressDialog show(Context context, CharSequence title,         
			CharSequence message, boolean indeterminate, boolean cancelable) 
			{     
				return show(context, title, message, indeterminate, cancelable, null); 
			}  
	
	public MyProgressDialog show(Context context, CharSequence title,         
			CharSequence message, boolean indeterminate,         
			boolean cancelable, OnCancelListener cancelListener) 
			{     
				MyProgressDialog dialog = new MyProgressDialog(context);     
				dialog.setTitle(title);     
				dialog.setCancelable(cancelable);     
				dialog.setOnCancelListener(cancelListener);     
				
				/* The next line will add the ProgressBar to the dialog. */     
				dialog.addContentView(new ProgressBar(context), 
						new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));     
				dialog.show();      
				return dialog; 
			}  
	
	public MyProgressDialog(Context context) 
	{     
		super(context, R.style.NewDialog); 
	} 
}
