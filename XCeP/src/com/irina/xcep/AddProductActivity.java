package com.irina.xcep;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Tag;
import com.irina.xcep.utils.MultiSelectionSpinner;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	private String barcode;
	
	
	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	Bitmap bitmap;
	private Intent pictureActionIntent = null;
	LinearLayout clickfotoProducto;
	ImageView fotoProducto;
	TextView txtengadirImaxe;
	private MultiSelectionSpinner multiSelectionSpinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_product);
		
		TextView nameLista = (TextView) findViewById(R.id.idNameMarket);
		String snameLista= getIntent().getExtras().getString("SUPERNAME");
		nameLista.setText(snameLista);
		
		ImageView imageMarket = (ImageView) findViewById(R.id.imageMarket);
		String simageMarket= getIntent().getExtras().getString("SUPERIMAGE");
		Picasso.with(this).load(simageMarket).into(imageMarket);
		
		fotoProducto = (ImageView) findViewById(R.id.image_view_product);
		txtengadirImaxe = (TextView) findViewById(R.id.txt_engadir_imaxe);
		
		clickfotoProducto = (LinearLayout) findViewById(R.id.button_camera_product);
		clickfotoProducto.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	 startDialog();
	        }
	    });
		
		//Spinner multiple para tags
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.spinnerTags);

		ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
		query.findInBackground(new FindCallback<Tag>() {
				
			@Override
			public void done(List<Tag> objects, ParseException e) {
				ArrayList<String> listaTags = new ArrayList<String>();
				for(Tag tag:objects){
					listaTags.add(tag.getName());
				}
				multiSelectionSpinner.setItems(listaTags);
				multiSelectionSpinner.setSelection(new int[]{});
			}
		});
		
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
	

	
	//M�todos empregados nesta clase
			/**
			 * Engade un producto a BD
			 * @param producto: Producto que se engade a BD
			 */
			public void engadirProducto(){
				
				String nameProductTxt ="";
				Produto addProduct = new Produto();
				
				//Barcode
				barcode=getIntent().getExtras().getString("BARCODE");
				addProduct.setIdentificadorScan(barcode);
				
				//Nome
				EditText nameProduto = (EditText) findViewById(R.id.text_name_product);
				nameProductTxt = nameProduto.getText().toString();
				addProduct.setTitle(nameProductTxt);
							
				//Marca
				EditText markProduto = (EditText) findViewById(R.id.text_mark_product);
				String markProductTxt = markProduto.getText().toString();
				addProduct.setMarca(markProductTxt);
				 
				//Descrici�n
				EditText descriptionProduto = (EditText) findViewById(R.id.text_description_product);
				String descriptionProdutoTxt = descriptionProduto.getText().toString();
				addProduct.setDescripcion(descriptionProdutoTxt);
				 
				//foto
				 ByteArrayOutputStream stream = new ByteArrayOutputStream();
				 bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				 byte[] byteArray = stream.toByteArray();
				 if(byteArray == null){
					 Toast.makeText(this, "Error al adjuntar imagen", Toast.LENGTH_SHORT).show();
					 return;
				 }
				 //FIXME Error en imagenes al rotar
				 //http://www.chinabtp.com/how-to-save-rotated-photos-in-parse-android/
				 
				 ParseFile imagenProduct = new ParseFile(fotoProducto.toString() +".png", byteArray);
				 imagenProduct.saveInBackground();
				 
				 addProduct.setIcon(imagenProduct);
				
				 //FIXME falta guardar precio y arraytags
				//Prezo
				//EditText priceProduto = (EditText) findViewById(R.id.text_name_product);
				
				 
				//ArrayTags
				 
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
			
			
			private void startDialog() {
			    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
			    myAlertDialog.setTitle("Adxuntar fotograf�a");
			    myAlertDialog.setMessage("Seleccione donde buscar a fotograf�a");

			    myAlertDialog.setPositiveButton("Galer�a",
			            new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface arg0, int arg1) {
			                    pictureActionIntent = new Intent(
			                            Intent.ACTION_GET_CONTENT, null);
			                    pictureActionIntent.setType("image/*");
			                    pictureActionIntent.putExtra("return-data", true);
			                    startActivityForResult(pictureActionIntent,
			                            GALLERY_PICTURE);
			                }
			            });

			    myAlertDialog.setNegativeButton("Camara",
			            new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface arg0, int arg1) {
			                    pictureActionIntent = new Intent(
			                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			                    startActivityForResult(pictureActionIntent,
			                            CAMERA_REQUEST);

			                }
			            });
			    myAlertDialog.show();
			}

			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				//ImageView fotomarket = (ImageView) findViewById(R.id.image_view_market);

			    super.onActivityResult(requestCode, resultCode, data);
			    if (requestCode == GALLERY_PICTURE) {
			        if (resultCode == RESULT_OK) {
			            if (data != null) {
			            	fotoProducto.setImageURI(data.getData());
		                    bitmap =  ((BitmapDrawable)fotoProducto.getDrawable()).getBitmap();
		                    txtengadirImaxe.setVisibility(View.GONE);
			            } else {
			                Toast.makeText(getApplicationContext(), "Cancelled",
			                        Toast.LENGTH_SHORT).show();
			            }
			        } else if (resultCode == RESULT_CANCELED) {
			            Toast.makeText(getApplicationContext(), "Cancelled",
			                    Toast.LENGTH_SHORT).show();
			        }
			    } else if (requestCode == CAMERA_REQUEST) {
			        if (resultCode == RESULT_OK) {
			            if (data.hasExtra("data")) {

			                bitmap = (Bitmap) data.getExtras().get("data");
			                bitmap = Bitmap.createScaledBitmap(bitmap, 100,
			                        100, false);
			                fotoProducto.setImageBitmap(bitmap);
			                txtengadirImaxe.setVisibility(View.GONE);
			                
			            } else if (data.getExtras() == null) {

			                Toast.makeText(getApplicationContext(),
			                        "Non se obtuvo a fotograf�a", Toast.LENGTH_SHORT)
			                        .show();

			            }

			        } else if (resultCode == RESULT_CANCELED) {
			            Toast.makeText(getApplicationContext(), "Cancelouse a fotograf�a",
			                    Toast.LENGTH_SHORT).show();
			        }
			    }

			}

}
