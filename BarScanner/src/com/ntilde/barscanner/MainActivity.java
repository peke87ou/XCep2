package com.ntilde.barscanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SurfaceHolder.Callback{
	
	Camera cam;
	SurfaceHolder surfaceholder;
	String previewImagePath;
	TextView leido;
	Button camara1;
	Button camara2;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewImagePath=this.getFilesDir().toString();
        leido=(TextView)findViewById(R.id.leido);
        camara1=(Button)findViewById(R.id.camara1);
        camara2=(Button)findViewById(R.id.camara2);
        if(Camera.getNumberOfCameras()>1){
        	camara1.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					cam=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
				}
			});
        	camara2.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					cam=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
				}
			});
        }
        else{
        	camara1.setVisibility(View.INVISIBLE);
        	camara2.setVisibility(View.INVISIBLE);
        }
        
        cam=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = cam.getParameters();
        //TODO FOCUS_MODE_CONTINUOUS_PICTURE (API 14, 4.0...)
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        cam.setParameters(parameters);
        cam.setPreviewCallback(new PreviewCallback(){
			public void onPreviewFrame(byte[] data, Camera camera){
				int ancho=camera.getParameters().getPreviewSize().width;
				int alto=camera.getParameters().getPreviewSize().height;
				int formato=camera.getParameters().getPreviewFormat();
				YuvImage imagen=new YuvImage(data, formato, ancho, alto, null);
				File archivo=new File(previewImagePath+"/preview.jpg");
		        FileOutputStream filecon;
				try{
					filecon=new FileOutputStream(archivo);
					imagen.compressToJpeg(new Rect(0,0,imagen.getWidth(),imagen.getHeight()),90,filecon);
					Bitmap imagenBmp=BitmapFactory.decodeFile(archivo.toString(), null);
					LuminanceSource source=new RGBLuminanceSource(imagenBmp); 
			        BinaryBitmap bmp=new BinaryBitmap(new HybridBinarizer(source));
					Reader barCodeReader=new MultiFormatReader();
					try{
						Result resultado=barCodeReader.decode(bmp);
						leido.setText(resultado.getText());
					}catch(Exception e){}
				}catch(Exception e){}
			}
		});
        
        SurfaceView cameraPreview= (SurfaceView)findViewById(R.id.cameraPreview);
        surfaceholder=cameraPreview.getHolder();
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceholder.setSizeFromLayout();
        surfaceholder.addCallback(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void liberarCamara(){
    	cam.release();
    }
    
    public void onPause(){
    	super.onPause();
    	liberarCamara();
    }
    
    public void onStop(){
    	super.onStop();
    	liberarCamara();
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	liberarCamara();
    }

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
		try{
			cam.setPreviewDisplay(surfaceholder);
		}catch(IOException e){}
		cam.startPreview();
	}

	public void surfaceCreated(SurfaceHolder holder){
	}

	public void surfaceDestroyed(SurfaceHolder holder){
	}
}
