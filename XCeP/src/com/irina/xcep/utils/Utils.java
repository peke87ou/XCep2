package com.irina.xcep.utils;

import android.text.TextUtils;
import android.widget.EditText;

public class Utils {

	public static boolean isNotEmpty(EditText view, String text) {
		
		EditText et = (EditText) view;
		et.setError(TextUtils.isEmpty(text)? "É necesario encher o campo seleccionado" : null);			
		
		return !TextUtils.isEmpty(text);
	}
}
