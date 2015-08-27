package com.irina.xcep;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.adapters.AdapterMarketPrice;
import com.squareup.picasso.Picasso;

public class DetailProduct extends Activity{
	
	public static final int requestCode = 1;
	public static final int resultCodeAdd = 1;
	public static final int resultCodeNoAdd = 0;
	
	ButtonRectangle btncancel, btnacept;
	private String dato;
	private String superIdLista;
	ListView listViewMarketPrice;
	AdapterMarketPrice adapter;
	
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
		
		//PRECIO
		ArrayList<String> arrayPrice;
		arrayPrice = getIntent().getStringArrayListExtra("PREZOPRODUCTO");
		
		ArrayList<String> listaUrlsSupermercados;
		listaUrlsSupermercados = getIntent().getStringArrayListExtra("URLSUPERMERCADO");
		
		ArrayList<String> listaIdSupermercados;
		listaIdSupermercados = getIntent().getStringArrayListExtra("IDSUPERMERCADO");
		
		superIdLista=getIntent().getExtras().getString("SUPERID");

		listViewMarketPrice = (ListView) findViewById(R.id.listMarketPrice);
		adapter = new AdapterMarketPrice(this, listaUrlsSupermercados, arrayPrice, superIdLista, listaIdSupermercados);
		listViewMarketPrice.setAdapter(adapter);
		
		
		btncancel = (ButtonRectangle) findViewById(R.id.share_product);
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setResult(resultCodeNoAdd);
				finish();
			}
		});
		
		btnacept = (ButtonRectangle) findViewById(R.id.add_product_list);
		btnacept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setResult(resultCodeAdd);
				finish();
			}
		});
	}
	
	
}