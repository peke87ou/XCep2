package com.irina.xcep.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.irina.xcep.R;

public class Utils {

	public static final String urlGooglePlay = "https://play.google.com";
	public static final String urlAppXecp = "https://play.google.com/store/apps/details?id=com.bandainamcogames.dbzdokkanww";

	public static boolean isNotEmpty(EditText view, String text) {
		
		EditText et = (EditText) view;
		et.setError(TextUtils.isEmpty(text)? "� necesario encher o campo seleccionado" : null);			
		
		return !TextUtils.isEmpty(text);
	}
	
	public static ProgressDialog crearDialogoEspera(Context context, String mensaje){
		
		ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(mensaje);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        return mProgressDialog;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	
	public static void shareFacebook(String subject){
		
		
		
		
	}
}
