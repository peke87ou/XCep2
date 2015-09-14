package com.irina.xcep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
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

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GeneralScanFragment extends Fragment implements SurfaceHolder.Callback {

	public static final String TAG = DetailListFragment.class.getName();

	// Camara
	Camera cam;
	SurfaceHolder surfaceholder;
	String previewImagePath;
	String resultadoBarCode;
	String barcode;
	boolean isProductoEnParse;
	Produto productBarcode;
	ArrayList<Supermercado> mSupermercados;

	// Agregar producto
	public static ArrayList<Lista> misListas = new ArrayList<Lista>();
	ParseUser currentUser = ParseUser.getCurrentUser();
	AlertDialog dialogoAgregarProductoCarrito, dialogoAgregarProductoSistema, dialogoAgregarProductoSupermercado;

	
	public static GeneralScanFragment newInstance(int Index) {
		GeneralScanFragment fragment = new GeneralScanFragment();

		Bundle args = new Bundle();
		args.putInt("Index", Index);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		crearDialogosAgregarProducto();
		iniciarScan();
		
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_general_scan, container, false);
		
		SurfaceView cameraPreview = (SurfaceView) layout.findViewById(R.id.surfaceView1GeneralScan);
		surfaceholder = cameraPreview.getHolder();
		surfaceholder.setSizeFromLayout();
		surfaceholder.addCallback(this);
		surfaceChanged(surfaceholder, 0, 0, 0);
		
		getActivity().getActionBar().setTitle(getString(R.string.scan_total)); //FIXME se está cambiando el texto
		return layout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		prepararCamara(); //FIXME no se está activando la cámara por segunda vez
		
		if((requestCode == DetailProductActivity.requestCode) && (resultCode == DetailProductActivity.resultCodeAdd)){
			
			checkProductToList(productBarcode);
			
		}else if ((requestCode == AddProductActivity.requestCode) && (resultCode == AddProductActivity.resultCodeAdd)){
		
			Toast.makeText(getActivity(), "Produto agregado ao supermercado", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		desconectarCamara();
	}

	@Override
	public void onResume() {
		super.onResume();
		prepararCamara();
	}

	@Override
	public void onStart() {
		super.onStart();
		prepararCamara();
	}

	@Override
	public void onStop() {
		super.onStop();
		desconectarCamara();
	}

	
	/**
	 * Inicia a cámara e o escaneo de barcodes
	 * @category camara
	 */
	
	private void iniciarScan() {
		if (cam == null) {
			prepararCamara();
		} else {
			cam.startPreview();
		}
	}

	/**
	 * Inicializa la configuración de la cámara
	 * @category camara
	 */
	public void prepararCamara() {

		if (cam == null) {
			cam = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			Camera.Parameters parameters = cam.getParameters();
			cam.setDisplayOrientation(90);
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			cam.setParameters(parameters);

			cam.setPreviewCallback(new PreviewCallback() {

				public void onPreviewFrame(byte[] data, Camera camera) {

					int ancho = camera.getParameters().getPreviewSize().width;
					int alto = camera.getParameters().getPreviewSize().height;
					int formato = camera.getParameters().getPreviewFormat();
					YuvImage imagen = new YuvImage(data, formato, ancho, alto, null);
					previewImagePath = getActivity().getFilesDir().toString();
					File archivo = new File(previewImagePath + "/preview.jpg");
					FileOutputStream filecon;
					try {
						filecon = new FileOutputStream(archivo);
						imagen.compressToJpeg(new Rect(0, 0, imagen.getWidth(), imagen.getHeight()), 90, filecon);
						Bitmap imagenBmp = BitmapFactory.decodeFile(archivo.toString(), null);
						Matrix imagenMatrix = new Matrix();
						imagenMatrix.postRotate(-90);
						imagenBmp = Bitmap.createBitmap(imagenBmp, 0, 0, imagenBmp.getWidth(), imagenBmp.getHeight(), imagenMatrix, true);
						LuminanceSource source = new RGBLuminanceSource(imagenBmp);
						BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(source));
						Reader barCodeReader = new MultiFormatReader();
						try {
							Result resultado = barCodeReader.decode(bmp);

							Log.e("valor de resultado", resultado.getText());
							
							if(dialogoAgregarProductoCarrito.isShowing() || dialogoAgregarProductoSistema.isShowing()){
								return;
							}
							
							if(dialogoAgregarProductoSupermercado!=null && dialogoAgregarProductoSupermercado.isShowing()){
								return;
							}
							
							if (resultado != null && resultadoBarCode == null) {
								resultadoBarCode = resultado.getText();
								showDialogoBarcodeEncontrado();

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		try {
			cam.reconnect();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), getString(R.string.non_se_pudo_acceder_camara), Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Desconecta la cámara, y borra la cámara de la vista
	 * @category camara
	 */
	public void desconectarCamara() {

		if (surfaceholder != null) {
			surfaceholder.removeCallback(this);
			surfaceholder = null;
		}

		if (cam != null) {
			cam.stopPreview();
			cam.setPreviewCallback(null);
			cam.lock();
			cam.release();
			cam = null;
		}
	}
	
	/**
	 * Inicia el display de la cámara
	 * @category camara
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try {
			cam.setPreviewDisplay(surfaceholder);
		} catch (IOException e) {
		}
		cam.startPreview();
	}

	/**
	 * @category camara
	 */
	public void surfaceCreated(SurfaceHolder holder) {
	}

	/**
	 * @category camara
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	
	/**
	 * Dialogos da actividade xunto coas funcións asociadas a cada diálogo
	 * @category dialogos
	 */
	public void crearDialogosAgregarProducto(){
		
		/**
		 * Dialogo agregar producto al carrito
		 */
		
		AlertDialog.Builder builderDialogoAgregarProducto = new AlertDialog.Builder(getActivity());
		builderDialogoAgregarProducto.setCancelable(false);
		builderDialogoAgregarProducto.setPositiveButton(getString(R.string.ver_detalle_do_produto), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				barcode = resultadoBarCode;
				resultadoBarCode = null;
				lanzarDetalleProducto(productBarcode);
			}
		});

		builderDialogoAgregarProducto.setNegativeButton(getString(R.string.pechar), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resultadoBarCode = null;
			}
		});
		
		builderDialogoAgregarProducto.setNeutralButton("Agregar produto", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resultadoBarCode = null;
				checkProductToList(productBarcode);
			}
		});

		dialogoAgregarProductoCarrito = builderDialogoAgregarProducto.create();
		dialogoAgregarProductoCarrito.setTitle(getActivity().getString(R.string.produto_atopado));	
		
		
		/**
		 * Dialogo agregar producto al sistema
		 */
		
		AlertDialog.Builder builderDialogoAgregarProductoSistema = new AlertDialog.Builder(getActivity());
		builderDialogoAgregarProductoSistema.setCancelable(false);
		builderDialogoAgregarProductoSistema.setPositiveButton("Agregar produto ao sistema", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				barcode = resultadoBarCode;
				resultadoBarCode = null;
				
				if(mSupermercados != null && mSupermercados.size() > 0){
					
					showDialogoListaSupermercado(mSupermercados);
				}else{
					
					final ProgressDialog progressDialog = Utils.crearDialogoEspera(getActivity(),
							"Obtendo supermercados");
					progressDialog.show();
					
					ParseQuery<Supermercado> query = ParseQuery.getQuery(Supermercado.class);
					query.findInBackground(new FindCallback<Supermercado>() {
						
						@Override
						public void done(List<Supermercado> objects, ParseException e) {
							
							progressDialog.dismiss();
							
							if(e==null && objects != null && objects.size() > 0){
								mSupermercados = (ArrayList<Supermercado>) objects;
								showDialogoListaSupermercado(mSupermercados);
							}else{
								Toast.makeText(getActivity(), "Non se puido calcular a lista de supermercados", Toast.LENGTH_SHORT).show();
							}
							
						}
					});
				}
				
			}
		});

		builderDialogoAgregarProductoSistema.setNegativeButton(getString(R.string.pechar), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resultadoBarCode = null;
			}
		});
		

		dialogoAgregarProductoSistema = builderDialogoAgregarProductoSistema.create();
		dialogoAgregarProductoSistema.setTitle(getActivity().getString(R.string.produto_novo));
	}
	
	/**
	 * Mostra unha lista de supermercados para agregar un produto novo a un supermercado
	 * @param supermercados
	 * @category dialogos
	 */
	public void showDialogoListaSupermercado(final ArrayList<Supermercado> supermercados){
		
		List<String> nombresSupermercados= new ArrayList<>();
		
		for(Supermercado supermercado:supermercados){
			nombresSupermercados.add(supermercado.getName());
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Supermercados").setItems((String[]) nombresSupermercados.toArray(new String[nombresSupermercados.size()]), 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int nSupermercado) {

						Intent intent = new Intent(getActivity(), AddProductActivity.class);
						Log.i("AddProduct QUE ENVIA", barcode);
						intent.putExtra("SUPERNAME", supermercados.get(nSupermercado).getName());
						intent.putExtra("SUPERIMAGE", supermercados.get(nSupermercado).getImage().getUrl());
						intent.putExtra("SUPERID", supermercados.get(nSupermercado).getObjectId());
						intent.putExtra("BARCODE", barcode);
						startActivityForResult(intent, AddProductActivity.requestCode);
					}
				});

		dialogoAgregarProductoSupermercado = builder.create();
		dialogoAgregarProductoSupermercado.show();
	}

	/**
	 * Mostra un diálogo de progreso, mentres se busca en parse se existe dito producto
	 * @category dialogos
	 */
	
	public void showDialogoBarcodeEncontrado() {
		
		ParseQuery<Produto> queryProductos = ParseQuery.getQuery(Produto.class); // Mirar se existe na BD o producto
		final ProgressDialog progressDialog = Utils.crearDialogoEspera(getActivity(), getActivity().getString(R.string.buscando_produto_no_sistema));
		progressDialog.show();
		queryProductos.include("APrice");
		queryProductos.include("Atags");
		queryProductos.include("APrice.PidMarket");
		queryProductos.whereEqualTo("idBarCode", resultadoBarCode);
		queryProductos.findInBackground(new FindCallback<Produto>() {
			@Override
			public void done(List<Produto> objects, ParseException e) {
				progressDialog.dismiss();
				if (e != null) {
					Toast.makeText(getActivity(), getString(R.string.erro_ao_consultar_o_produto), Toast.LENGTH_SHORT).show();
				} else {
					Log.i(TAG, objects.size() + "resultado" + resultadoBarCode);
					if (objects.size() > 0) {
						isProductoEnParse = true;
						productBarcode = objects.get(0);

					} else {
						isProductoEnParse = false;
					}

					if (isProductoEnParse) { // Producto en el sistema

						dialogoAgregarProductoCarrito.setMessage(getActivity().getString(R.string.atopouse_o_produto) + productBarcode.getTitle() + "("
								+ resultadoBarCode	+ "). \n" + "\n¿Desexa engadilo a unha lista existente?");	
						dialogoAgregarProductoCarrito.show();

					} else { // Producto que non se encontra no sistema
						
						dialogoAgregarProductoSistema.setMessage(getActivity().getString(R.string.atopouse_o_produto_con_identificador) + resultadoBarCode + "\n"
								+ getActivity().getString(R.string.desexa_engadilo_o_sistema_para_o_supermercado));
						dialogoAgregarProductoSistema.show();
					}

				}
			}
		});

	}

	
	/**
	 * Comprueba las listas compatibles con el producto seleccionado por el usuario
	 * @param producto Producto seleccionado polo usuario
	 * @category especifica
	 */
	public void checkProductToList(final Produto producto) {


		if (HomeFragment.misListas == null || HomeFragment.misListas.size() == 0) {

			Toast.makeText(getActivity(), getString(R.string.non_posee_ningunha_lista) + HomeFragment.misListas.size(), Toast.LENGTH_SHORT).show();
			return;
		}

		final ArrayList<Lista> listaSupermercadosCompatibles = new ArrayList<Lista>();
		for (Lista lista : HomeFragment.misListas) {
			if (lista.getSupermercado().containsProduct(producto)) {
				listaSupermercadosCompatibles.add(lista);
			}
		}

		if (listaSupermercadosCompatibles.size() == 0) {
			String nombresSupermercadosCompatibles = "";

			for (Prezo precioSupermercado : producto.getAPrice()) {
				nombresSupermercadosCompatibles += precioSupermercado.getPidMarket().getName() + ", ";
			}

			Toast.makeText(getActivity(), getString(R.string.ningunha_lista_compatible) + "\n" + nombresSupermercadosCompatibles, Toast.LENGTH_LONG).show();

		} else {

			final List<String> listaNombres = new ArrayList<String>();

			for (int nLista = 0; nLista < listaSupermercadosCompatibles.size(); nLista++) {
				listaNombres.add(listaSupermercadosCompatibles.get(nLista).getName() + " ("
						+ listaSupermercadosCompatibles.get(nLista).getSupermercado().getName() + ")");
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(getResources().getString(R.string.engadir) + producto.getTitle() + getString(R.string.a_lista)).setItems(
					(String[]) listaNombres.toArray(new String[listaNombres.size()]), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int nLista) {
							try {
								addProductToList(listaSupermercadosCompatibles.get(nLista), producto);

							} catch (IllegalStateException e) {
								Toast.makeText(getActivity(), getString(R.string.erro_ao_engadir_o_produto) + HomeFragment.misListas.get(nLista).getName(),
										Toast.LENGTH_SHORT).show();

							}
						}

					});

			builder.create();
			builder.show();

		}
	}

	/**
	 * Lanza unha actividade para mostrar o detalle dun producto
	 * @param producto móstrase o detalle deste producto
	 * @category especifica
	 */
	public void lanzarDetalleProducto(Produto producto) {

		Intent intent = new Intent(getActivity(), DetailProductActivity.class);
		intent.putExtra("NOMEPRODUCTO", producto.getTitle());
		// CATEGORIA
		Bundle b = new Bundle();
		ArrayList<String> listaTags = new ArrayList<String>();
		if (producto.getATags() != null) {
			for (Tag tag : producto.getATags()) {
				listaTags.add(tag.getName());
			}
		}

		b.putStringArrayList("CATEGORIAPRODUCTO", listaTags);
		intent.putExtras(b);
		// IMAGEN
		intent.putExtra("IMAGEPRODUCTO", producto.getIcon().getUrl());
		intent.putExtra("DESCRIPCIONPRODUCTO", producto.getDescripcion());
		intent.putExtra("MARCAPRODUCTO", producto.getMarca());
		// SUPERMERCADO
		intent.putExtra("SUPERIMAGE", "");
		// PRECIO
		ArrayList<String> listaPrice = new ArrayList<String>();
		ArrayList<String> listaNombresSupermercados = new ArrayList<String>();
		ArrayList<String> listaUrlsSupermercados = new ArrayList<String>();
		ArrayList<String> listaIdSupermercados = new ArrayList<String>();
		for (Prezo price : producto.getAPrice()) {
			listaPrice.add(price.getPrice().toString());
			listaNombresSupermercados.add(price.getPidMarket().getName());
			listaUrlsSupermercados.add(price.getPidMarket().getImage().getUrl());
			listaIdSupermercados.add(price.getPidMarket().getObjectId());
		}
		b.putStringArrayList("PREZOPRODUCTO", listaPrice);
		// b.putStringArrayList("NOMESUPERMERCADO", listaNombresSupermercados);
		b.putStringArrayList("URLSUPERMERCADO", listaUrlsSupermercados);
		b.putStringArrayList("IDSUPERMERCADO", listaIdSupermercados);
		intent.putExtra("SUPERID", "");
		intent.putExtras(b);

		startActivityForResult(intent, DetailProductActivity.requestCode);
	}


	/**
	 * Engadese un produto a lista. Se xa está na lista, engadese unha unidade.
	 * @param producto
	 * @category especifica
	 */
	public void addProductToList(final Lista listaSelected,Produto producto) throws IllegalStateException{
		
		if(producto == null){
			
			Toast.makeText(getActivity(), getString(R.string.produto_non_valido), Toast.LENGTH_SHORT).show();
			return;
		}
		
		//Comprobar si existe en la lista, si existe sumar una unidad más.
		
		boolean isProductoAlreadyAdd=false;
		Units unidadSeleccionada=null;
		if(listaSelected.getAIdUnits() != null){
			for(Units unitProducto:listaSelected.getAIdUnits()){
				
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
			Toast.makeText(getActivity(), getString(R.string.engadida_unha_unidade_de) +unidadSeleccionada.getProduct().getTitle() + getString(R.string.total)+unidadSeleccionada.getNumberUnits(), Toast.LENGTH_SHORT).show();
		
		}else{ //Nuevo producto a la lista
			
			final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(),
					getResources().getString(R.string.engadindo_produto_novo_a_lista));
			progress.show();
			
			final Units unidadProducto = new Units();
			unidadProducto.put("numberUnits", 1);
			unidadProducto.put("PidProduct", ParseObject.createWithoutData("Products", producto.getObjectId()));
			unidadProducto.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if(e == null){
						listaSelected.addAidUnits(unidadProducto.getObjectId());
						listaSelected.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								
								if(e!= null){
									e.printStackTrace();
									Toast.makeText(getActivity(), getString(R.string.erro_ao_gardar_a_lista), Toast.LENGTH_SHORT).show();
									
								}else{

									Toast.makeText(getActivity(), getString(R.string.engadiuse_o_novo_produto), Toast.LENGTH_SHORT).show();
								}
								
								progress.dismiss();
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


}
