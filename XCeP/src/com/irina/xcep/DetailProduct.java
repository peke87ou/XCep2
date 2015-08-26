package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Tag;

public class DetailProduct extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String dato;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);

		
		dato=getIntent().getExtras().getString("NOMEPRODUCTO");
		((TextView) findViewById(R.id.detail_product_name)).setText( dato);
		
		
		//dato = getIntent().getParcelableArrayListExtra("CATEGORIAPRODUCTO")
		ArrayList<String> arrayTag;
		arrayTag = getIntent().getStringArrayListExtra("CATEGORIAPRODUCTO");
		
		//for(S)
		
		((TextView) findViewById(R.id.detail_product_category)).setText( "Categoría:" + arrayTag );
		
		//IMAGEN PRODUCTO
		
		dato=getIntent().getExtras().getString("DESCRIPCIONPRODUCTO");
		((TextView) findViewById(R.id.detail_product_description)).setText( dato);
		
		dato=getIntent().getExtras().getString("MARCAPRODUCTO");
		((TextView) findViewById(R.id.detail_product_mark)).setText( dato);
		
		//SUPERMERCADO 
		
		//PRECIO
//		dato=getIntent().getExtras().getString("PREZOPRODUCTO");
//		((TextView) findViewById(R.id.priceProduct)).setText( "Prezo: " + dato + " €");
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