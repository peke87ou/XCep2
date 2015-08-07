package com.irina.xcep;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Produto;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class DetailProduct extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String barcode;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_product);

		
		barcode=getIntent().getExtras().getString("NOMEPRODUCTO");
		((TextView) findViewById(R.id.detail_product_name)).setText( barcode);
		
		barcode=getIntent().getExtras().getString("DESCRIPCIONPRODUCTO");
		((TextView) findViewById(R.id.detail_product_description)).setText( barcode);
		 
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
				
				detailProducto();
				
				
			}
		});
	}
	
	public void detailProducto(){
		
		
		String nameProductTxt ="";
		Produto addProduct = new Produto();
		
		
		
	
		//Barcode
//		addProduct.setIdentificadorScan(barcode);
		
		//Nome
		 
//		 TextView nameProduto = (TextView) findViewById(R.id.detail_product_name);
//		 Log.i("BARCODE", barcode.getClass().getName());
//		 barcode.getClass().getName();
//		 addProduct.setNome(nome)
		 //nameProductTxt = nameProduto.getText().toString();
		 
//		 addProduct.getNome();
//					
//		//Marca
//		 EditText markProduto = (EditText) findViewById(R.id.text_mark_product);
//		 String markProductTxt = markProduto.getText().toString();
//		 addProduct.setMarca(markProductTxt);
//		 
//		//Descrición
//		 EditText descriptionProduto = (EditText) findViewById(R.id.text_description_product);
//		 String descriptionProdutoTxt = descriptionProduto.getText().toString();
//		 addProduct.setDescripcion(descriptionProdutoTxt);
//		 
//		//foto
//		 ImageView fotoProducto = (ImageView) findViewById(R.id.image_view_product);
////		 String fotoProductoTxt = fotoProducto.getText().toString();
////		 addProduct.setUrlImaxe(fotoProductoTxt);
//		 
//		//Prezo
//		 EditText priceProduto = (EditText) findViewById(R.id.text_name_product);
////		 String priceProdutoTxt = priceProduto.getText().toString();
////		 addProduct.setPrezoPorSupermercado(priceProdutoTxt);
////		 
//		 addProduct.saveInBackground(new SaveCallback() {
//				@Override
//				public void done(ParseException arg0) {
//					if (arg0 == null){
//						Toast.makeText(AddProductActivity.this, "Engadimos O producto a BD ", Toast.LENGTH_SHORT).show();
//						Log.i("Producto", "Engadimos O producto a BD ");
//						finish();
//						
//					}else{
//						Toast.makeText(AddProductActivity.this, R.string.error_add_list+" " + arg0.getMessage(), Toast.LENGTH_SHORT).show();
//						Log.e("Producto", "ERROR O ENGADIR NA BD ");
//					}
//				}
//			});
		
	}
}