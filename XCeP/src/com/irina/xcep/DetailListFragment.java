package com.irina.xcep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

public class DetailListFragment extends Fragment implements SurfaceHolder.Callback{

	private Supermercado mMarketSelected;
	Camera cam;
	SurfaceHolder surfaceholder;
	String previewImagePath;
	private TabHost tabHost;
	String resultadoBarCode;
	String barcode;
	boolean isProductoEnParse;
	
	public static DetailListFragment newInstance (int Index){
		DetailListFragment fragment = new DetailListFragment();
		Bundle args = new Bundle();
		
		args.putInt("Index", Index);
		
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public void prepararCamara(){
		cam=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = cam.getParameters();
        cam.setDisplayOrientation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        cam.setParameters(parameters);
        
        cam.setPreviewCallback(new PreviewCallback(){
			
        	public void onPreviewFrame(byte[] data, Camera camera){
				
				int ancho=camera.getParameters().getPreviewSize().width;
				int alto=camera.getParameters().getPreviewSize().height;
				int formato=camera.getParameters().getPreviewFormat();
				YuvImage imagen=new YuvImage(data, formato, ancho, alto, null);
				previewImagePath = getActivity().getFilesDir().toString();
				File archivo=new File(previewImagePath+"/preview.jpg");
		        FileOutputStream filecon;
				try{
					filecon=new FileOutputStream(archivo);
					imagen.compressToJpeg(new Rect(0,0,imagen.getWidth(),imagen.getHeight()),90,filecon);
					Bitmap imagenBmp=BitmapFactory.decodeFile(archivo.toString(), null);
					Matrix imagenMatrix=new Matrix();
					imagenMatrix.postRotate(-90);
					imagenBmp=Bitmap.createBitmap(imagenBmp,0,0,imagenBmp.getWidth(),imagenBmp.getHeight(),imagenMatrix,true);
					LuminanceSource source=new RGBLuminanceSource(imagenBmp);
			        BinaryBitmap bmp=new BinaryBitmap(new HybridBinarizer(source));
					Reader barCodeReader=new MultiFormatReader();
					try{
						Result resultado=barCodeReader.decode(bmp);
						
						Log.e("valor de resultado",resultado.getText());
						if (resultado != null && resultadoBarCode == null){
							//Toast.makeText(getActivity(), resultado.getText(), Toast.LENGTH_LONG).show();
							resultadoBarCode = resultado.getText();
							showDialogoAgregarProducto();
							
							
						}
						
					}catch(Exception e){}
				}catch(Exception e){}
				
				
			}
		});
        
        try {
			cam.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getActivity(), "No se pudo acceder a la camara", Toast.LENGTH_LONG).show();
		}
        SurfaceView cameraPreview=(SurfaceView)getActivity().findViewById(R.id.surfaceView1);
        
        surfaceholder=cameraPreview.getHolder();
        //surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceholder.setSizeFromLayout();
        surfaceholder.addCallback(this);
	}
	
	public void desconectarCamara(){


		if(surfaceholder != null)
			surfaceholder.removeCallback(this);
		
		if(cam != null){
			cam.setPreviewCallback(null);
			cam.release();
			cam = null;
		}
	}
	
	public void showDialogoAgregarProducto(){
		
		// Mirar se existe na BD
		ParseQuery<Produto> productos = ParseQuery.getQuery(Produto.class);
		 
		productos.whereEqualTo("idBarCode",resultadoBarCode);
		productos.findInBackground(new FindCallback<Produto>() {
			@Override
			public void done(List<Produto> objects, ParseException e) {
					Log.i("jklsdfjklsdfsdfjkl",objects.size()+"resultado"+resultadoBarCode+"");
					if (objects.size() > 0){
						isProductoEnParse = false;
					}else{
						isProductoEnParse = true;
					}
				
			}
		});
	
	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Add the buttons
		builder.setPositiveButton("Engadir produto", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if(isProductoEnParse){
		        		   barcode = resultadoBarCode;
			        	   //TODO Agregar resultadoBarCode a parse
			        	   resultadoBarCode = null;
			        	   Intent intent = new Intent(getActivity(), AddProductActivity.class);
			        	   Log.i("QUE ENVIA", barcode);
			        	   intent.putExtra("MESSAGE",barcode);  
		                   startActivityForResult(intent, 1);
		        	   }else{
		        		   barcode = resultadoBarCode;
		        		   resultadoBarCode = null;
//		        		   Toast.makeText(getActivity(), "DETALLE DE PRODUTO", Toast.LENGTH_LONG).show();
		        		   Intent intent = new Intent(getActivity(), DetailProduct.class);
//			        	   Log.i("QUE ENVIA", barcode);
//			        	   intent.putExtra("MESSAGE",barcode);  
		                   startActivityForResult(intent, 1);
		        	   }
		        	  
		        	 
		        	  
		           }
		       });
		builder.setNegativeButton("Pechar", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		              resultadoBarCode = null;
		           }
		       });
		AlertDialog dialogo = builder.create();
		
		Log.i("Esta en parse", isProductoEnParse+"");
		if(isProductoEnParse){
			
			dialogo.setTitle("Produto atopado");
			dialogo.setMessage("Atopouse o produto "+resultadoBarCode +"\n¿Desexa engadilo a súa lista?");
			
		}else{
			
			dialogo.setTitle("Produto novo");
			dialogo.setMessage("Atopouse o produto  "+resultadoBarCode +"\n¿Desexa engadilo o sistema para o supermercado "+ mMarketSelected.getNome()+ "?");
		}
		
		dialogo.show();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout home = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
			
		Resources res = getResources();
		tabHost = (TabHost) home.findViewById(android.R.id.tabhost);
		tabHost.setup();
				
		
		TabHost.TabSpec spec=tabHost.newTabSpec("Lista da compra");
		spec.setContent(R.id.tab_list_buy);
		spec.setIndicator("",res.getDrawable(R.drawable.notebook));
		tabHost.addTab(spec);
		 
		spec=tabHost.newTabSpec("Catálogo");
		spec.setContent(R.id.tab_catalog);
		spec.setIndicator("",res.getDrawable(R.drawable.ic_maps_store_mall_directory));
		tabHost.addTab(spec);

		spec=tabHost.newTabSpec("Escaner");
		spec.setContent(R.id.tab_scan);
		spec.setIndicator("",res.getDrawable(R.drawable.ic_navigation_fullscreen));
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
		// Convertir currentUser en String
		TextView txtuser = (TextView) home.findViewById(R.id.idNameList);
		txtuser.setText(((MenuActivity)getActivity()).mNameList);
		ImageView imageMarket =  (ImageView) home.findViewById(R.id.imageMarket);
		if(mMarketSelected ==null){
			mMarketSelected = ((MenuActivity)getActivity()).mMarketSelected;
			if(mMarketSelected !=null){
				Picasso.with(getActivity()).load(mMarketSelected.getUrlLogo().getUrl()).into(imageMarket);
			}
		}else{
			Picasso.with(getActivity()).load(mMarketSelected.getUrlLogo().getUrl()).into(imageMarket);
		}
		
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				Toast.makeText(getActivity(), "ID: "+ tabId, Toast.LENGTH_SHORT).show();
				if (tabId == "Escaner") {
					if(cam==null){
						prepararCamara();
					}else{
						cam.startPreview();
					}
					
					/**
					 * Lector QR por intent
					IntentIntegratorBarCode integrator = new IntentIntegratorBarCode(getActivity());
					integrator.initiateScan();*/
				}else{
					if(cam != null)
						cam.stopPreview();
				}
			}
		});
			
		return home;
	}
	
	
	
	@Override
	public void onStop() {
		super.onStop();
		desconectarCamara();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(cam!=null)
			cam.stopPreview();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//reloadUserShoppingLists();
		if((tabHost.getCurrentTab() == 2) && (cam != null)){
			cam.startPreview();
		}
	}
	

	/**
	 * No se usa actualmente el lector QR por intent
	 */
	/*@Deprecated
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		  IntentResultBarcode scanResult = IntentIntegratorBarCode.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
			  
			  Toast.makeText(getActivity(), "Se lee"+scanResult.getContents(), Toast.LENGTH_LONG).show();
		  }else{
			  
			  Toast.makeText(getActivity(), "QR vacío", Toast.LENGTH_LONG).show();
		  }
	}*/
	
	

	
	/**
	 * Surfaceholder callback de la cámara
	 */
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
	
	
//	private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
//
//	        private int mLastFirstVisibleItem;
//	        private boolean mAnimationCalled = false;
//			private AdapterView<ListAdapter> mListView;
//			private boolean mListStateFlying;
//			private  Object mAddQuoteBtn;
//
//	        @Override
//	        public void onScrollStateChanged(AbsListView view, int scrollState) {
//	            //If we are flying
//	            boolean mListStateFlying = AbsListView.OnScrollListener.SCROLL_STATE_FLING == scrollState;
//	            mAnimationCalled = mListStateFlying ? mAnimationCalled : false;
//	            Log.i("ABDLISTVIEW", "State changed, new state: " + scrollState);
//
//	        }
//
//	        @Override
//	        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//	           
//				if (mAddQuoteBtn == null) return;
//
//
//	            if (!mAnimationCalled && mLastFirstVisibleItem < firstVisibleItem) {
//	                //Scrolling down
//	                ((ButtonFloat) mAddQuoteBtn).hide();
//	                mAnimationCalled = true;
//	            } else if (!mAnimationCalled && mLastFirstVisibleItem > firstVisibleItem) {
//	                //Scrolling up
//	                ((Toast) mAddQuoteBtn).show();
//	                mAnimationCalled = true;
//	            }
//	            mLastFirstVisibleItem = firstVisibleItem;
//
//
//
//	            if(mListStateFlying || mListView.getCount() == 0) return;
//
//	        }
//	    };
}