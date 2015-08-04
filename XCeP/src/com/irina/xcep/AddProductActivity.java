package com.irina.xcep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Produto;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddProductActivity extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String barcode;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_product);
		
		btncancel = (ButtonRectangle) findViewById(R.id.cancel_new_product);
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		btnacept = (ButtonRectangle) findViewById(R.id.add_new_product);
		btnacept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Engadimos a nova lista a BD
				
				engadirProducto();
				
				
			}
		});
	}
	

	
	//Métodos empregados nesta clase
			/**
			 * Engade un producto a BD
			 * @param producto: Producto que se engade a BD
			 */
			public void engadirProducto(){
				
				
				String nameProductTxt ="";
				Produto addProduct = new Produto();
				
				
				
				barcode=getIntent().getExtras().getString("MESSAGE");
				//Barcode
				addProduct.setIdentificadorScan(barcode);
				
				//Nome
				 EditText nameProduto = (EditText) findViewById(R.id.text_name_product);
				 nameProductTxt = nameProduto.getText().toString();
				 addProduct.setNome(nameProductTxt);
							
				//Marca
				 EditText markProduto = (EditText) findViewById(R.id.text_mark_product);
				 String markProductTxt = markProduto.getText().toString();
				 addProduct.setMarca(markProductTxt);
				 
				//Descrición
				 EditText descriptionProduto = (EditText) findViewById(R.id.text_description_product);
				 String descriptionProdutoTxt = descriptionProduto.getText().toString();
				 addProduct.setDescripcion(descriptionProdutoTxt);
				 
				//foto
				 ImageView fotoProducto = (ImageView) findViewById(R.id.image_view_product);
//				 String fotoProductoTxt = fotoProducto.getText().toString();
//				 addProduct.setUrlImaxe(fotoProductoTxt);
				 
				//Prezo
				 EditText priceProduto = (EditText) findViewById(R.id.text_name_product);
//				 String priceProdutoTxt = priceProduto.getText().toString();
//				 addProduct.setPrezoPorSupermercado(priceProdutoTxt);
//				 
				 addProduct.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							if (arg0 == null){
								Toast.makeText(AddProductActivity.this, "Engadimos O producto a BD ", Toast.LENGTH_SHORT).show();
								Log.i("Producto", "Engadimos O producto a BD ");
								finish();
								
							}else{
								Toast.makeText(AddProductActivity.this, R.string.error_add_list+" " + arg0.getMessage(), Toast.LENGTH_SHORT).show();
								Log.e("Producto", "ERROR O ENGADIR NA BD ");
							}
						}
					});
				
			}

}
