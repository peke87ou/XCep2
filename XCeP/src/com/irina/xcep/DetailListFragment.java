package com.irina.xcep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
import com.irina.xcep.adapters.AdapterProductsCatalog;
import com.irina.xcep.adapters.AdapterTags;
import com.irina.xcep.adapters.AdapterUnits;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Prezo;
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
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;


//FIXME Agregar correctamente un producto nuevo. Agregar al array de productos del market, agregar el objeto a la tabla de productos, y completar bien los field.

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
	boolean isProductoEnParse, isProductoEnSupermercado;
	Produto productBarcode;
	
	AdapterProductsCatalog adapterProductoCatalog;
	ArrayList<Produto> productCatalogList = new ArrayList<Produto>();
	ListView catalogoListView;
	ListView productosListaListView;
	ParseUser currentUser = ParseUser.getCurrentUser();
	String nameList ;
	
	
	ArrayList<Tag> tagList = new ArrayList<Tag>();
	GridView gridTags;
	TextView emptyList;
	AdapterTags adapterTag;
	CheckBox checkboxTag;
	AlertDialog dialogoAgregarProducto, dialogoAgregarPrecio;
	
	AdapterUnits adapter;
	ArrayList<Units> listaUnidades = new ArrayList<Units>();
	
	String newPriceString = "";
	
	public static DetailListFragment newInstance (int Index){
		DetailListFragment fragment = new DetailListFragment();
		
		Bundle args = new Bundle();
		args.putInt("Index", Index);
		fragment.setArguments(args);
		
		return fragment;
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
		TextView txtNameList = (TextView) home.findViewById(R.id.idNameMarket);
		nameList = ((MenuActivity)getActivity()).mNameList;
		txtNameList.setText(nameList);
		
		if(mListaSelected ==null){
			mListaSelected = ((MenuActivity)getActivity()).mListSelected;
		}
		
		ImageView imageMarket =  (ImageView) home.findViewById(R.id.imageMarket);
		if(mMarketSelected ==null){
			mMarketSelected = mListaSelected.getSupermercado();
		}

		Picasso.with(getActivity()).load(mMarketSelected.getImage().getUrl()).into(imageMarket);
		
		
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
		productosListaListView = (ListView) home.findViewById(R.id.list_products);
		
		gridTags=(GridView) home.findViewById(R.id.grid_tags);
        gridTags.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Toast.makeText(getActivity(), "You Clicked at " + tagList.get(position), Toast.LENGTH_SHORT).show();
            	checkboxTag =  (CheckBox) view.findViewById(R.id.checkBoxTag);
            	checkboxTag.setChecked(!checkboxTag.isCheck());
            	gridTags.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            	
              }
            
        });
		

        catalogoListView = (ListView) home.findViewById(R.id.listProductCatalog);
        catalogoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				Toast.makeText(getActivity(), "Presionado producto "+position, Toast.LENGTH_SHORT).show();
				addProductToList(productCatalogList.get(position));
			}
		});
        
		
		if (tabHost.getCurrentTab() == 0){
			cargarProdutosLista(nameList, false);
		}
		
		return home;
	}
	
	/**
	 * Se agrega un producto a la lista. Si ya est� en la lista, agrega una unidad.
	 * @param producto
	 */
	public void addProductToList(Produto producto){
		
		if(producto == null)
			return;
		
		//Comprobar si existe en la lista, si existe sumar una unidad m�s.
		
		boolean isProductoAlreadyAdd=false;
		Units unidadSeleccionada=null;
		if(mListaSelected.getAIdUnits() != null){
			for(Units unitProducto:mListaSelected.getAIdUnits()){
				if(unitProducto.getProduct().getObjectId().equals(producto.getObjectId())){
					isProductoAlreadyAdd = true;
					unidadSeleccionada = unitProducto; 
					break;
				}
			}
		}
		
		if(isProductoAlreadyAdd){ //Aumentar una unidad al producto de la lista
			
			unidadSeleccionada.addNumberUnits(1);
			unidadSeleccionada.saveInBackground();
			
		}else{ //Nuevo producto a la lista
			final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(),
					"Agregando producto nuevo a la lista");
			progress.show();
			
			final Units unidadProducto = new Units();
			unidadProducto.put("numberUnits", 1);
			unidadProducto.put("PidProduct", ParseObject.createWithoutData("Products", producto.getObjectId()));
			unidadProducto.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if(e == null){
						mListaSelected.addAidUnits(unidadProducto.getObjectId());
						mListaSelected.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								
								if(e!= null){
									e.printStackTrace();
									progress.dismiss();
								}else{
									reloadUserShoppingList(progress);
									Log.d(TAG, "Se agrega la unidad a la lista");
								}
							}
						});
					}else{
						e.printStackTrace();
						progress.dismiss();
					}
				}
			});
		}
		
	}
	
	/**
	 * Se actualizan los datos de la lista actual
	 */
	
	public void reloadUserShoppingList(final ProgressDialog progressDialog) {
		//Recreamos o conxunto de listas de compra do usuario
		Log.d(TAG, "reloadUserShoppingList"); 

		ParseQuery<Lista> query = ParseQuery.getQuery(Lista.class);

		query.include("PidMarket");
		query.include("PidMarket.AProduct");
		query.include("PidMarket.AProduct.APrice");
		query.include("PidMarket.AProduct.APrice.PidMarket");

		query.include("AidUnits");
		query.include("AidUnits.PidProduct");
		query.include("AidUnits.PidProduct.APrice");
		query.include("AidUnits.PidProduct.APrice.PidMarket");

		// Filtramos as lista para cada usuario logueado na app
		// query.include("User");
		query.whereEqualTo("objectId", mListaSelected.getObjectId());
		query.findInBackground(new FindCallback<Lista>() {
			
			@Override
			public void done(List<Lista> objects, ParseException e) {
				if (e != null) {
					Toast.makeText(getActivity(), "Error ao engadir produto",
							Toast.LENGTH_SHORT).show();
				}

				if(objects.size() > 0){
					mListaSelected = objects.get(0);
				}
				
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
		});

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
        
        ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
		query.findInBackground(new FindCallback<Tag>() {
			
			@Override
			public void done(List<Tag> objects, ParseException e) {
					tagList = (ArrayList<Tag>) objects;
					adapterTag.clear();
					adapterTag.addAll(tagList);
					gridTags.setAdapter(adapterTag);
			}
		});
        
		productCatalogList.clear();
		if(mMarketSelected == null){
			mMarketSelected = mListaSelected.getSupermercado();
		}
		Supermercado supermercado = mMarketSelected;
		List<Produto> productosSupermercado = supermercado.getAProduct();
		if(productosSupermercado != null){
			productCatalogList.addAll(productosSupermercado);
		}
		adapterProductoCatalog = new AdapterProductsCatalog(getActivity(), productCatalogList, mMarketSelected);
		catalogoListView.setAdapter(adapterProductoCatalog);
         	
	}
	
	

	private void cargarProdutosLista(String nameList, boolean forzarRecarga) {
		
		listaUnidades.clear();
		if(mListaSelected.get("AidUnits") != null){
			listaUnidades.addAll(mListaSelected.getAIdUnits());
		}
		
		adapter = new AdapterUnits(getActivity(), listaUnidades, mListaSelected);
		productosListaListView.setAdapter(adapter);
		
		if(listaUnidades.size()==0){
			Toast.makeText(getActivity(), R.string.empty_list, Toast.LENGTH_LONG).show();
		}
		
		if(!forzarRecarga && listaUnidades.size() > 0){
			Log.d(TAG, "Non se volve a executar un find de produtos");
			return;
		}
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		desconectarCamara();
	}

	@Override
	public void onPause() {
		super.onPause();
		desconectarCamara();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//reloadUserShoppingLists();
		prepararCamara();
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
		
		if(dialogoAgregarProducto == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(false);
			builder.setPositiveButton("Engadir produto", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
		        		   barcode = resultadoBarCode;
		        		   resultadoBarCode = null;
		        		   
			        	   if(!isProductoEnParse){ //Producto novo
			        		   
				        	   Intent intent = new Intent(getActivity(), AddProductActivity.class);
				        	   Log.i("AddProduct QUE ENVIA", barcode);
				        	   intent.putExtra("SUPERNAME",mMarketSelected.getName()); 
				        	   intent.putExtra("SUPERIMAGE",mMarketSelected.getImage().getUrl()); 
				        	   intent.putExtra("SUPERID",mMarketSelected.getObjectId());  
				        	   intent.putExtra("BARCODE",barcode);  
			                   startActivityForResult(intent, 1);
			        	   
			        	   }else if(isProductoEnSupermercado){ //Producto encontrado, e que pertence o supermercado
			        		   
			        		   Log.i("DetailProduct QUE ENVIA", productBarcode.getTitle());
			        		   Intent intent = new Intent(getActivity(), DetailProduct.class);
			        		   intent.putExtra("NOMEPRODUCTO",productBarcode.getTitle());  
			        		   //CATEGORIA
			        		   //IMAGEN
			        		   intent.putExtra("DESCRIPCIONPRODUCTO",productBarcode.getDescripcion()); 
			        		   intent.putExtra("MARCAPRODUCTO",productBarcode.getMarca()); 
			        		   //SUPERMERCADO Y PRECIO
			                   startActivityForResult(intent, 1);
			                   
			        	   }else{ //producto encontrado, pero non pertence o supermercado 
			        		   
			        		   showDialogoAgregarPrecio();
			        	   }
			        	  
			           }
			       });
			
			builder.setNegativeButton("Pechar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			              resultadoBarCode = null;
			           }
			});
			
			dialogoAgregarProducto = builder.create();
		}
		
		// Mirar se existe na BD
		ParseQuery<Produto> queryProductos = ParseQuery.getQuery(Produto.class);
		final ProgressDialog progressDialog = Utils.crearDialogoEspera(getActivity(), "Buscando producto en el sistema");
		progressDialog.show();
		queryProductos.include("APrice");
		queryProductos.include("APrice.PidMarket");
		queryProductos.whereEqualTo("idBarCode",resultadoBarCode/*"3545664346"*/);
		queryProductos.findInBackground(new FindCallback<Produto>() {
			@Override
			public void done(List<Produto> objects, ParseException e) {
				progressDialog.dismiss();
				if(e!=null){
					Toast.makeText(getActivity(), "Erro ao consultar o producto", Toast.LENGTH_SHORT).show();
				}else{
					Log.i(TAG,objects.size()+"resultado"+resultadoBarCode);
					if (objects.size() > 0){
						isProductoEnParse = true;
						isProductoEnSupermercado = false;
						productBarcode =  objects.get(0);
						
						List<Prezo> precioPorSupermercado = productBarcode.getAPrice();
						
						for(Prezo nPrecio:precioPorSupermercado){
							if(nPrecio.getPidMarket().getObjectId().equals(mMarketSelected.getObjectId())){
								isProductoEnSupermercado = true;
								break;
							}
						}
						
					}else{
						isProductoEnParse = false;
					}
					
					Log.i("Esta en parse", isProductoEnParse+"");
					if(isProductoEnParse && isProductoEnSupermercado){ //Producto en el sistema, y en el supermercado de la lista
						
						dialogoAgregarProducto.setTitle("Produto atopado");
						dialogoAgregarProducto.setMessage("Atopouse o produto "+productBarcode.getTitle()+"("+resultadoBarCode +")\n�Desexa engadilo a s�a lista?");
						
					}else if(isProductoEnParse){ //Producto en el sistema, pero no en el supermercado
						
						dialogoAgregarProducto.setTitle("Producto atopado");
						dialogoAgregarProducto.setMessage("Atopuse o producto "+productBarcode.getTitle()+"("+resultadoBarCode + ", pero non consta no supermercado "+mMarketSelected.getName()
								+". \n"+"\n�Desexa engadilo ao supermercado "+mMarketSelected.getName()+"?");
					
					}else{ //Producto que non se encontra no sistema
						
						dialogoAgregarProducto.setTitle("Produto novo");
						dialogoAgregarProducto.setMessage("Atopouse o produto  "+productBarcode.getTitle()+"("+resultadoBarCode +"\n�Desexa engadilo o sistema para o supermercado? ");
					}
					
					dialogoAgregarProducto.show();
				}
			}
		});
	
	}
	
	/**
	 * M�strase un di�logo para agregar un precio a un producto que non se encontra no supermercado da lista actual, pero si se encontra en parse
	 */
	public void showDialogoAgregarPrecio(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setCancelable(false);
		final View viewDialog = inflater.inflate(R.layout.activity_dialog_add_price_product_market, null);
		builder.setView(viewDialog);
		builder.setPositiveButton("Engadir prezo", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
		});

		TextView supermercadoTextView = (TextView) viewDialog.findViewById(R.id.txtMarket);
		supermercadoTextView.setText(mMarketSelected.getName());
		EditText newPriceEditText = (EditText) viewDialog.findViewById(R.id.priceProductNew);
		newPriceEditText.setHint(productBarcode.getAPrice().get(0).getPrice().toString());
		dialogoAgregarPrecio = builder.create();
		dialogoAgregarPrecio.setTitle(productBarcode.getTitle() + "  "+ productBarcode.getMarca());
		dialogoAgregarPrecio.show();
		
		dialogoAgregarPrecio.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() { //Se sobreescribe el onclick para evitar cierre del di�logo
				@Override
				public void onClick(View v) {

					Utils.hideSoftKeyboard(getActivity());
					EditText newPriceEditText = (EditText) viewDialog.findViewById(R.id.priceProductNew);
					boolean isEmpty;
					if (newPriceEditText.getText() != null) {
						newPriceString = newPriceEditText.getText().toString();
						isEmpty = !Utils.isNotEmpty(newPriceEditText,newPriceString);
					} else {
						isEmpty = true;
					}

					if (!isEmpty) {

							// 1� Engadir un precio-supermercado en la tabla de precios para este producto
							Prezo prezoProductoBarcode = new Prezo();
							prezoProductoBarcode.setPrice(Double.parseDouble(newPriceString));
							prezoProductoBarcode.setPidMarket(ParseObject.createWithoutData("Market", mMarketSelected.getObjectId()));
							prezoProductoBarcode.saveInBackground();
							
							productBarcode.addAPrice(prezoProductoBarcode.getObjectId());
							productBarcode.saveInBackground();
							
							// 2� Agregar en la tabla market, al array AProducts
							mMarketSelected.addAproduct(productBarcode.getObjectId());
							mMarketSelected.saveInBackground();
							dialogoAgregarPrecio.dismiss();
					}
				}
		});
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
