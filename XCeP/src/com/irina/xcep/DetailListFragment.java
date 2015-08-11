package com.irina.xcep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.CheckBox;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.irina.xcep.adapters.AdapterUnits;
import com.irina.xcep.adapters.AdapterProductsCatalog;
import com.irina.xcep.adapters.AdapterTags;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.model.Tag;
import com.irina.xcep.model.Units;
import com.irina.xcep.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class DetailListFragment extends Fragment implements SurfaceHolder.Callback{
	
	public static final String TAG = DetailListFragment.class.getName();

	private Supermercado mMarketSelected;
	private Lista mListaSelected;
	Camera cam;
	SurfaceHolder surfaceholder;
	String previewImagePath;
	private TabHost tabHost;
	String resultadoBarCode;
	String barcode;
	boolean isProductoEnParse;
	Produto productBarcode;
	
	AdapterProductsCatalog adapterProductoCatalog;
	ArrayList<Produto> productCatalogList = new ArrayList<Produto>();
	ListView listCatalog;
	ListView list;
	ParseUser currentUser = ParseUser.getCurrentUser();
	String nameList ;
	
	
	ArrayList<Tag> tagList = new ArrayList<Tag>();
	GridView grid;
	TextView emptyList;
	AdapterTags adapterTag;
	CheckBox checkboxTag;
	AlertDialog dialogo;
	
	AdapterUnits adapter;
	ArrayList<Units> listaUnidades = new ArrayList<Units>();
	
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
        //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
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
		final ProgressDialog progressDialog = Utils.crearDialogoEspera(getActivity(), "Buscando producto en el sistema");
		progressDialog.show();
		
		productos.whereEqualTo("idBarCode",resultadoBarCode);
		productos.findInBackground(new FindCallback<Produto>() {
			@Override
			public void done(List<Produto> objects, ParseException e) {
				progressDialog.dismiss();
				if(e!=null){
					Toast.makeText(getActivity(), "Erro ao consultar o producto", Toast.LENGTH_SHORT).show();
				}else{
					Log.i("jklsdfjklsdfsdfjkl",objects.size()+"resultado"+resultadoBarCode);
					if (objects.size() > 0){
						isProductoEnParse = true;
						productBarcode =  objects.get(0);
					}else{
						isProductoEnParse = false;
					}
					
					Log.i("Esta en parse", isProductoEnParse+"");
					if(isProductoEnParse){
						
						dialogo.setTitle("Produto atopado");
						dialogo.setMessage("Atopouse o produto "+resultadoBarCode +"\n�Desexa engadilo a s�a lista?");
						
					}else{
						
						dialogo.setTitle("Produto novo");
						dialogo.setMessage("Atopouse o produto  "+resultadoBarCode +"\n�Desexa engadilo o sistema para o supermercado ");
					}
					
					dialogo.show();
				}
			}
		});
	
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
		 
		spec=tabHost.newTabSpec("Cat�logo");
		spec.setContent(R.id.tab_catalog);
		spec.setIndicator("",res.getDrawable(R.drawable.ic_maps_store_mall_directory));
		tabHost.addTab(spec);

		spec=tabHost.newTabSpec("Escaner");
		spec.setContent(R.id.tab_scan);
		spec.setIndicator("",res.getDrawable(R.drawable.ic_navigation_fullscreen));
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
		// Convertir currentUser en String
		TextView txtNameList = (TextView) home.findViewById(R.id.idNameList);
		nameList = ((MenuActivity)getActivity()).mNameList;
		txtNameList.setText(nameList);
		
		
		ImageView imageMarket =  (ImageView) home.findViewById(R.id.imageMarket);
		if(mMarketSelected ==null){
			mMarketSelected = ((MenuActivity)getActivity()).mMarketSelected;
		}

		Picasso.with(getActivity()).load(mMarketSelected.getUrlLogo().getUrl()).into(imageMarket);
		
		
		if(mListaSelected ==null){
			mListaSelected = ((MenuActivity)getActivity()).mListSelected;
		}
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				
				getScan(tabId);
				
				switch (tabId) {
				
//				case "Escaner":
//					getScan(tabId);
//					break;
				case "Lista da compra":
					cargarProdutosLista(nameList, false);
					break;

				case "Cat�logo":
					getCatalogo();
					break;
					
				default:
					//cargarProdutosLista(nameList);
					break;
				}
				
				
				
			}
		});
		
		//Lista de produtos
		list = (ListView) home.findViewById(R.id.list_products);
		
		grid=(GridView) home.findViewById(R.id.grid_tags);
		
//		emptyList=(TextView) home.findViewById(R.id.text_empty_list);
		listCatalog = (ListView) home.findViewById(R.id.listProductCatalog);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Add the buttons
		builder.setCancelable(false);
		builder.setPositiveButton("Engadir produto", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
	        		   barcode = resultadoBarCode;
	        		   resultadoBarCode = null;
	        		   
		        	   if(!isProductoEnParse){
			        	   Intent intent = new Intent(getActivity(), AddProductActivity.class);
			        	   Log.i("AddProduct QUE ENVIA", barcode);
			        	   intent.putExtra("MESSAGE",barcode);  
		                   startActivityForResult(intent, 1);
		        	   }else{
		        		   Log.i("DetailProduct QUE ENVIA", productBarcode.getNome());
		        		   Intent intent = new Intent(getActivity(), DetailProduct.class);
		        		   intent.putExtra("NOMEPRODUCTO",productBarcode.getNome());  
		        		   //CATEGORIA
		        		   //IMAGEN
		        		   intent.putExtra("DESCRIPCIONPRODUCTO",productBarcode.getDescripcion()); 
		        		   intent.putExtra("MARCAPRODUCTO",productBarcode.getMarca()); 
		        		   //SUPERMERCADO Y PRECIO
		                   startActivityForResult(intent, 1);
		        	   }
		        	  
		           }
		       });
		
		builder.setNegativeButton("Pechar", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		              resultadoBarCode = null;
		           }
		       });
		
		dialogo = builder.create();
		
		if (tabHost.getCurrentTab() == 0){
			cargarProdutosLista(nameList, false);
		}
		
		return home;
	}
	
	private void getScan(String tabId){
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
	
	private void getCatalogo(){
		
		adapterTag = new AdapterTags(getActivity(), tagList);
        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        
        ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
		query.findInBackground(new FindCallback<Tag>() {
			
			@Override
			public void done(List<Tag> objects, ParseException e) {
					tagList = (ArrayList<Tag>) objects;
					//FIXME Ver como actualizar la lista de supermercados dentro del adapter
					adapterTag.clear();
					adapterTag.addAll(tagList);
					grid.setAdapter(adapterTag);
			}
		});
        
		productCatalogList.clear();
		if(mMarketSelected == null){
			mMarketSelected = ((MenuActivity)getActivity()).mMarketSelected;
		}
		Supermercado supermercado = mMarketSelected;
		List<Produto> productosSupermercado = (ArrayList<Produto>) supermercado.get("AProduct");
		if(productosSupermercado != null){
			productCatalogList.addAll(productosSupermercado);
		}
		adapterProductoCatalog = new AdapterProductsCatalog(getActivity(), productCatalogList);
		listCatalog.setAdapter(adapterProductoCatalog);
		
		
    	//Todos los productos
    	/*ParseQuery<Produto> query2 = ParseQuery.getQuery(Produto.class);
		query2.findInBackground(new FindCallback<Produto>() {
			@Override
			public void done(List<Produto> objects, ParseException e) {
				Log.e("Adaptador productos Catalogo", objects.size()+"");
				productCatalogList = (ArrayList<Produto>) objects;
				adapterProductoCatalog.clear();
				adapterProductoCatalog.addAll(productCatalogList);
			}
		});*/
		
		
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	Toast.makeText(getActivity(), "You Clicked at " + tagList.get(position), Toast.LENGTH_SHORT).show();
        	checkboxTag =  (CheckBox) view.findViewById(R.id.checkBoxTag);
        	checkboxTag.setChecked(!checkboxTag.isCheck());
        	grid.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//        	if (supermercados.size()== position){ //ultima
//        		Intent intent = new Intent(AddShoppingListActivity.this, AddMarketActivity.class);
//        		//FIXME invocar como startActivityForResult, y manejar el callback para hacer reload de la lista
//                startActivity(intent);
//          	  
//                
//            }else {
//            	idSuper = supermercados.get(position);
//            }
        	
        	
          }
         });
        
        
		
	}
	private void cargarProdutosLista(String nameList, boolean forzarRecarga) {
		
		listaUnidades.clear();
		if(mListaSelected.get("AidUnits") != null){
			listaUnidades.addAll((ArrayList<Units>)mListaSelected.get("AidUnits"));
		}
		
		adapter = new AdapterUnits(getActivity(), listaUnidades);
		list.setAdapter(adapter);
		
		if(listaUnidades.size()==0){
			Toast.makeText(getActivity(), R.string.empty_list, Toast.LENGTH_LONG).show();
		}
		
		if(!forzarRecarga && listaUnidades.size() > 0){
			Log.d(TAG, "Non se volve a executar un find de produtos");
			return;
		}
		
		
		//ParseObject precio = mListaSelected.getParseObject("Products");
		/*ParseQuery<Produto> parseQueryProduto = mListaSelected.getIdProducts().getQuery();//mListaSelected.getIdProducts().getQuery();
		parseQueryProduto.include("Price.price");
		parseQueryProduto.findInBackground(new FindCallback<Produto>() {

			@Override
			public void done(List<Produto> objects, ParseException e) {
				if(objects.size()==0){
					//emptyList.setVisibility(View.GONE);
					Toast.makeText(getActivity(), R.string.empty_list, Toast.LENGTH_LONG).show();
				}
				
				
				listaUnidades = (ArrayList<Produto>) objects;
				adapter.clear();
				if(listaUnidades != null){
					Log.e("Adaptador productos", objects.size()+"");
					adapter.addAll(listaUnidades);
					
				}else{
					Toast.makeText(getActivity(), R.string.empty_list, Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
	     	  
			
	    });*/
		
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		desconectarCamara();
	}

	@Override
	public void onPause() {
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
	 * Surfaceholder callback de la c�mara
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
	

}
