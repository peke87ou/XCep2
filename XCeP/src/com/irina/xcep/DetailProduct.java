package com.irina.xcep;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.adapters.AdapterMarketPrice;
import com.irina.xcep.utils.ShareSocialMediaActivity;
import com.squareup.picasso.Picasso;

@SuppressLint("DefaultLocale")
public class DetailProduct extends ShareSocialMediaActivity{
 
    
	public static final int requestCode = 1;
	public static final int resultCodeAdd = 1;
	public static final int resultCodeNoAdd = 0;
	
	ButtonRectangle btncancel, btnacept;
	ImageView botonShare;
	private String dato;
	private String superIdLista;
	ListView listViewMarketPrice;
	AdapterMarketPrice adapter;
	ImageView imageProduct;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);
		
		dato=getIntent().getExtras().getString("NOMEPRODUCTO");
		((TextView) findViewById(R.id.detail_product_name)).setText( dato.toUpperCase(Locale.getDefault()));
		
		//Categoria
		ArrayList<String> arrayTag;
		arrayTag = getIntent().getStringArrayListExtra("CATEGORIAPRODUCTO");
		dato = "";
		for(String cadena: arrayTag ){
			dato = dato + cadena+ "  ";
		}
		((TextView) findViewById(R.id.detail_product_category)).setText(  dato );
		
		//IMAGEN PRODUCTO
		imageProduct =  (ImageView) findViewById(R.id.image_view_product);
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
		
		
		btncancel = (ButtonRectangle) findViewById(R.id.close_dialog);
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
		
		
		botonShare = (ImageView) findViewById(R.id.imageButtonShare1);
		botonShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final String[] items = {"Twitter", "Facebook"};
				
				AlertDialog.Builder builder = new AlertDialog.Builder(DetailProduct.this);
		        builder.setTitle("Compartir "+getIntent().getExtras().getString("NOMEPRODUCTO"))
		           .setItems(items, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int item) {
		                    Log.i("Dialogos", "Opci�n elegida: " + items[item]);
		                    if (items[item].equalsIgnoreCase("Twitter")){
		                    	
		                    	shareTwitter();
		                    
		                    }else if(items[item].equalsIgnoreCase("Facebook")){
		                    	
		                    	shareFacebookPost();
		                    }
		                }
		            });
		 
		        builder.create().show();
			}
		});
	}
	
}