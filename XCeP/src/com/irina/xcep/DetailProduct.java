package com.irina.xcep;

import android.app.Activity;
import android.os.Bundle;

import com.gc.materialdesign.views.ButtonRectangle;

public class DetailProduct extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);

	}
}