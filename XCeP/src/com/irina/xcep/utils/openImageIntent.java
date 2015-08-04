package com.irina.xcep.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class openImageIntent extends Activity {
	
		static Context context;
		ImageView img_logo;
		protected static final int CAMERA_REQUEST = 0;
		protected static final int GALLERY_PICTURE = 1;
		private static Intent pictureActionIntent = null;
		Bitmap bitmap;


		    String selectedImagePath;

		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
//		    setContentView(R.layout.main1);
//
//		    img_logo= (ImageView) findViewById(R.id.imageView1);
//		    img_logo.setOnClickListener(new OnClickListener() {
//		        public void onClick(View v) {
		            startDialog();
//		        }
//
//		    });
		}
		    
		    

		 public  void startDialog() {
		    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		    myAlertDialog.setTitle("Upload Pictures Option");
		    myAlertDialog.setMessage("How do you want to set your picture?");

		    myAlertDialog.setPositiveButton("Gallery",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface arg0, int arg1) {
		                    pictureActionIntent = new Intent(
		                            Intent.ACTION_GET_CONTENT, null);
		                    pictureActionIntent.setType("image/*");
		                    pictureActionIntent.putExtra("return-data", true);
		                    startActivityForResult(pictureActionIntent,GALLERY_PICTURE);
		                }
		            });

		    myAlertDialog.setNegativeButton("Camera",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface arg0, int arg1) {
		                    pictureActionIntent = new Intent(
		                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		                    startActivityForResult(pictureActionIntent,CAMERA_REQUEST);

		                }
		            });
		    myAlertDialog.show();
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {

		    super.onActivityResult(requestCode, resultCode, data);
		    if (requestCode == GALLERY_PICTURE) {
		        if (resultCode == RESULT_OK) {
		            if (data != null) {
		                // our BitmapDrawable for the thumbnail
		                BitmapDrawable bmpDrawable = null;
		                // try to retrieve the image using the data from the intent
		                Cursor cursor = getContentResolver().query(data.getData(),
		                        null, null, null, null);
		                if (cursor != null) {

		                    cursor.moveToFirst();

		                    int idx = cursor.getColumnIndex(ImageColumns.DATA);
		                    String fileSrc = cursor.getString(idx);
		                    bitmap = BitmapFactory.decodeFile(fileSrc); // load
		                                                                        // preview
		                                                                        // image
		                    bitmap = Bitmap.createScaledBitmap(bitmap,
		                            100, 100, false);
		                    // bmpDrawable = new BitmapDrawable(bitmapPreview);
		                    img_logo.setImageBitmap(bitmap);
		                } else {

		                    bmpDrawable = new BitmapDrawable(getResources(), data
		                            .getData().getPath());
		                    img_logo.setImageDrawable(bmpDrawable);
		                }

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

		                // retrieve the bitmap from the intent
		                bitmap = (Bitmap) data.getExtras().get("data");


		 Cursor cursor = getContentResolver()
		                        .query(Media.EXTERNAL_CONTENT_URI,
		                                new String[] {
		                                        Media.DATA,
		                                        Media.DATE_ADDED,
		                                        MediaStore.Images.ImageColumns.ORIENTATION },
		                                Media.DATE_ADDED, null, "date_added ASC");
		                if (cursor != null && cursor.moveToFirst()) {
		                    do {
		                        Uri uri = Uri.parse(cursor.getString(cursor
		                                .getColumnIndex(Media.DATA)));
		                        selectedImagePath = uri.toString();
		                    } while (cursor.moveToNext());
		                    cursor.close();
		                }

		                Log.e("path of the image from camera ====> ",
		                        selectedImagePath);


		                bitmap = Bitmap.createScaledBitmap(bitmap, 100,
		                        100, false);
		                // update the image view with the bitmap
		                img_logo.setImageBitmap(bitmap);
		            } else if (data.getExtras() == null) {

		                Toast.makeText(getApplicationContext(),
		                        "No extras to retrieve!", Toast.LENGTH_SHORT)
		                        .show();

		                BitmapDrawable thumbnail = new BitmapDrawable(
		                        getResources(), data.getData().getPath());

		                // update the image view with the newly created drawable
		                img_logo.setImageDrawable(thumbnail);

		            }

		        } else if (resultCode == RESULT_CANCELED) {
		            Toast.makeText(getApplicationContext(), "Cancelled",
		                    Toast.LENGTH_SHORT).show();
		        }
		    }

		}


		}