package com.irina.xcep;

import java.io.ByteArrayOutputStream;

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
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.utils.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;


public class AddMarketActivity extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	Bitmap bitmap;
	ImageView img_logo;
    String selectedImagePath;
    private Intent pictureActionIntent = null;
    EditText nameMarket;
    ImageView fotomarket;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_market);
		nameMarket = (EditText) findViewById(R.id.text_name_market);
		fotomarket = (ImageView) findViewById(R.id.image_view_market);
		
		btncancel = (ButtonRectangle) findViewById(R.id.cancel_new_market);
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		btnacept = (ButtonRectangle) findViewById(R.id.add_new_market);
		btnacept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(bitmap == null){ //Se comprueba imagen del supermercado
					
					 Toast.makeText(getApplicationContext(), "Non se obtuvo a fotografía", Toast.LENGTH_SHORT).show();
					 return;
					 
				}else{ //Se comprueba nombre del supermercado
					
					boolean allfilled = true;
					allfilled =  Utils.isNotEmpty(nameMarket, nameMarket.getText().toString());
					if(!allfilled){
						return;
					}
				}
				
				//Engadimos a nova lista a BD
				engadirmarket();
				
			}
		});
		
		fotomarket.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		            //openImageIntent();
		        	 startDialog();
		            
		        }

		    });
		
	}
	
	private void startDialog() {
	    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	    myAlertDialog.setTitle("Adxuntar fotografía");
	    myAlertDialog.setMessage("Seleccione donde buscar a fotografía");

	    myAlertDialog.setPositiveButton("Galería",
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
		ImageView fotomarket = (ImageView) findViewById(R.id.image_view_market);

	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == GALLERY_PICTURE) {
	        if (resultCode == RESULT_OK) {
	            if (data != null) {
                    fotomarket.setImageURI(data.getData());
                    bitmap =  ((BitmapDrawable)fotomarket.getDrawable()).getBitmap();
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
	                fotomarket.setImageBitmap(bitmap);
	                
	            } else if (data.getExtras() == null) {

	                Toast.makeText(getApplicationContext(),
	                        "Non se obtuvo a fotografía", Toast.LENGTH_SHORT)
	                        .show();

	            }

	        } else if (resultCode == RESULT_CANCELED) {
	            Toast.makeText(getApplicationContext(), "Cancelouse a fotografía",
	                    Toast.LENGTH_SHORT).show();
	        }
	    }

	}
	

	
	//Métodos empregados nesta clase
			/**
			 * Engade un supermercado a BD
			 * @param market: supermercado que se engade a BD
			 */
			public void engadirmarket(){
				
				
				Supermercado addmarket = new Supermercado();
				
								
				//Nome
				 addmarket.setNome(nameMarket.getText().toString());
							
				
				//foto
				 ByteArrayOutputStream stream = new ByteArrayOutputStream();
				 bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				 byte[] byteArray = stream.toByteArray();
				 ParseFile imagenSupermercado = new ParseFile("camper2.png", byteArray);
				 addmarket.setUrlLogo(imagenSupermercado);
			 
				 addmarket.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException arg0) {
						if (arg0 == null){
							Toast.makeText(AddMarketActivity.this, "Engadimos O supermercado a BD ", Toast.LENGTH_SHORT).show();
							Log.i("market", "Engadimos O supermercado a BD ");
							finish();
							
						}else{
							Toast.makeText(AddMarketActivity.this, R.string.error_add_list+" " + arg0.getMessage(), Toast.LENGTH_SHORT).show();
							Log.e("market", "ERROR O ENGADIR NA BD ");
						}
					}
				});
			}
						
}



