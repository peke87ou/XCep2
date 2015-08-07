package com.irina.xcep;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

public class DetailProduct extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String barcode;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);

		
		barcode=getIntent().getExtras().getString("NOMEPRODUCTO");
		((TextView) findViewById(R.id.detail_product_name)).setText( barcode);
		
		//barcode=getIntent().getExtras().getString("CATEGORIAPRODUCTO");
		//((TextView) findViewById(R.id.detail_product_category)).setText( barcode);
		
		//IMAGEN PRODUCTO
		
		barcode=getIntent().getExtras().getString("DESCRIPCIONPRODUCTO");
		((TextView) findViewById(R.id.detail_product_description)).setText( barcode);
		
		barcode=getIntent().getExtras().getString("MARCAPRODUCTO");
		((TextView) findViewById(R.id.detail_product_mark)).setText( barcode);
		
		//SUPERMERCADO Y PRECIO
		
		//RESTO SUPERMERCADOS Y PRECIOS
		
		 
		btncancel = (ButtonRectangle) findViewById(R.id.share_product);
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//finish();
				
			}
		});
		
		btnacept = (ButtonRectangle) findViewById(R.id.add_product_list);
		btnacept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Engadimos a nova lista a BD
				
				
			}
		});
	}
	
	
}