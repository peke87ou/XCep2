package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Tag;
import com.squareup.picasso.Picasso;

public class DetailProduct extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String dato;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);
		
		dato=getIntent().getExtras().getString("NOMEPRODUCTO");
		((TextView) findViewById(R.id.detail_product_name)).setText( dato.toUpperCase());
		
		//Categoria
		ArrayList<String> arrayTag;
		arrayTag = getIntent().getStringArrayListExtra("CATEGORIAPRODUCTO");
		dato = "";
		for(String cadena: arrayTag ){
			dato = dato + cadena+ "  ";
		}
		((TextView) findViewById(R.id.detail_product_category)).setText(  dato );
		
		//IMAGEN PRODUCTO
		ImageView imageProduct =  (ImageView) findViewById(R.id.image_view_product);
		dato=getIntent().getExtras().getString("IMAGEPRODUCTO");
		Picasso.with(this).load(dato).into(imageProduct);
		
		//DESCRICION
		dato=getIntent().getExtras().getString("DESCRIPCIONPRODUCTO");
		((TextView) findViewById(R.id.detail_product_description)).setText( dato);
		
		dato=getIntent().getExtras().getString("MARCAPRODUCTO");
		((TextView) findViewById(R.id.detail_product_mark)).setText( dato);
		
		//SUPERMERCADO 
		ImageView imageMarket =  (ImageView) findViewById(R.id.ImageMarket);
		dato=getIntent().getExtras().getString("SUPERIMAGE");
		Picasso.with(this).load(dato).into(imageMarket);
		
		//PRECIO
		ArrayList<String> arrayPrice;
		arrayPrice = getIntent().getStringArrayListExtra("PREZOPRODUCTO");
		dato = "";
		for(String cadena: arrayPrice ){
			dato = dato + cadena+ "  ";
		}		
		((TextView) findViewById(R.id.priceProduct)).setText( dato + " �");

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