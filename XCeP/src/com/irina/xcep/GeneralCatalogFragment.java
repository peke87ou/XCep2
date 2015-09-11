package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import com.irina.xcep.adapters.AdapterProductsGeneralCatalog;
import com.irina.xcep.adapters.AdapterTags;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
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
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class GeneralCatalogFragment extends Fragment {

	public static final String TAG = DetailListFragment.class.getName();

	// Tags
	ArrayList<Tag> tagList = new ArrayList<Tag>();
	AdapterTags adapterTag;
	GridView mGridTags;
	CheckBox mCheckboxTag;
	
	//Búsqueda
	SearchView mSearchView;
	
	//Catalogo
	ListView catalogoListView;
	ArrayList<Produto> productCatalogList = new ArrayList<Produto>();
	
	//Agregar producto
	public static ArrayList<Lista> misListas = new ArrayList<Lista>();
	ParseUser currentUser = ParseUser.getCurrentUser();
	private Produto productoSeleccionado;

	public static GeneralCatalogFragment newInstance(int Index) {
		GeneralCatalogFragment fragment = new GeneralCatalogFragment();

		Bundle args = new Bundle();
		args.putInt("Index", Index);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_general_catalog, container, false);
		

		mGridTags = (GridView) layout.findViewById(R.id.grid_tags);
		mGridTags.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		mGridTags.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		mGridTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCheckboxTag = (CheckBox) view.findViewById(R.id.checkBoxTag);
				mCheckboxTag.setChecked(!mCheckboxTag.isChecked());
				actualizarCatalogoGeneral();
			}

		});

		catalogoListView = (ListView) layout.findViewById(R.id.lista_catalogo_general);
		catalogoListView.setTextFilterEnabled(true);
		catalogoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				mSearchView.clearFocus();
				Utils.hideSoftKeyboard(getActivity());
				productoSeleccionado = (Produto) view.getTag();
				lanzarDetalleProducto(productoSeleccionado);
			}
		});

		SearchManager searchManager = (SearchManager) (getActivity().getSystemService(Context.SEARCH_SERVICE));
		mSearchView = (SearchView) layout.findViewById(R.id.searchView1);
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setSubmitButtonEnabled(false);
		mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		mSearchView.setQueryHint("Búsqueda por palabras clave");
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				AdapterProductsGeneralCatalog adapter = (AdapterProductsGeneralCatalog) catalogoListView.getAdapter();
				adapter.getFilter().filter(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String query) {

				AdapterProductsGeneralCatalog adapter = (AdapterProductsGeneralCatalog) catalogoListView.getAdapter();
				adapter.getFilter().filter(query);
				return true;
			}
		});

		
		actualizarCatalogoGeneral();

		getTags();
		

		return layout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if((requestCode == DetailProductActivity.requestCode) && (resultCode == DetailProductActivity.resultCodeAdd)){
			
			checkProductToList(productoSeleccionado);
			
		}else if (requestCode == AddProductActivity.requestCode){
		
			/*final ProgressDialog progressDialog = Utils.crearDialogoEspera(getActivity(),
					"Recargando catálogo del supermercado "+mMarketSelected.getName());
			progressDialog.show();	
			reloadUserShoppingList(progressDialog, true);*/

		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	/**
	 * Comprueba las listas compatibles con el producto seleccionado por el usuario
	 * @param producto
	 */
	
	public void checkProductToList(final Produto producto){
		
		if(HomeFragment.misListas == null || HomeFragment.misListas.size() == 0){
			
			Toast.makeText(getActivity(), "Non posee ningunha lista"+HomeFragment.misListas.size(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		final ArrayList<Lista> listaSupermercadosCompatibles = new ArrayList<Lista>();
		for(Lista lista:HomeFragment.misListas){
			if(lista.getSupermercado().containsProduct(producto)){
				listaSupermercadosCompatibles.add(lista);
			}
		}
		
		if(listaSupermercadosCompatibles.size() == 0){
			String nombresSupermercadosCompatibles = "";
			
			for(Prezo precioSupermercado:producto.getAPrice()){
				nombresSupermercadosCompatibles += precioSupermercado.getPidMarket().getName() + ", ";
			}
			
			Toast.makeText(getActivity(), "Ningunha lista compatible. Cree unha lista primeiro dos seguintes supermercados: \n"+nombresSupermercadosCompatibles, Toast.LENGTH_LONG).show();

		}else{


			final List<String> listaNombres = new ArrayList<String>();
			
			for(int nLista=0; nLista < listaSupermercadosCompatibles.size(); nLista++){
				listaNombres.add(listaSupermercadosCompatibles.get(nLista).getName() + " ("+ listaSupermercadosCompatibles.get(nLista).getSupermercado().getName()+")");
			}
			
			
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 
	        builder.setTitle("Agregar "+producto.getTitle() +" a lista")
	           .setItems((String[])listaNombres.toArray(new String[listaNombres.size()]), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int nLista) {
	                    try{
	                    	addProductToList(listaSupermercadosCompatibles.get(nLista), producto);
	                    	
	                    }catch(IllegalStateException e){
	                    	Toast.makeText(getActivity(), "Erro ao agregar o produto"+HomeFragment.misListas.get(nLista).getName(), Toast.LENGTH_SHORT).show();

	                    }
	                }

	            });
	 
	        builder.create();
	        builder.show();
			
		}
	}

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

	
	public void actualizarCatalogoGeneral() {
		
		getActivity().getActionBar().setTitle(R.string.catalog_total);
		
		if(productCatalogList.size() > 0){
			
			ArrayList<Produto> productosSistema= new ArrayList<Produto>();
			productosSistema = (ArrayList<Produto>)filtrarProductos(productCatalogList);
			
			AdapterProductsGeneralCatalog adapterProductoCatalog = new AdapterProductsGeneralCatalog(getActivity(), productosSistema, this);
	    	adapterProductoCatalog.getFilter().filter(mSearchView.getQuery());
			catalogoListView.setAdapter(adapterProductoCatalog);
		
		}else{
		
			
			final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(), "Actualizando produtos");
			progress.show();
			ParseQuery<Produto> query = ParseQuery.getQuery(Produto.class);

			query.include("APrice");
			query.include("Atags");
			query.include("APrice.PidMarket");

			query.findInBackground(new FindCallback<Produto>() {
				@Override
				public void done(List<Produto> objects, ParseException e) {
					if (e != null) {
						Toast.makeText(getActivity(), "Error na actualización dos produtos", Toast.LENGTH_SHORT).show();
					}

					productCatalogList = (ArrayList<Produto>) objects;

					if (productCatalogList != null && productCatalogList.size() > 0) {

						actualizarCatalogoGeneral();
					}

					progress.dismiss();
				}
			});
			
		}
		
	}
	
	
	private List<Produto> filtrarProductos(List<Produto> listaProdutos){
		
		List<String> tagsSeleccionados = new ArrayList<String>();
		List<Produto> tempListaProdutos = new ArrayList<Produto>();
		
		/**
		 * Filtrado por tags
		 */
		
		for(int i=0; i < mGridTags.getChildCount(); i++){ //Calculamos tags seleccionados
			
			ViewGroup viewRow = (ViewGroup)mGridTags.getChildAt(i);
			
			for (int n=0; n < viewRow.getChildCount(); n++){
				
				View viewTag = viewRow.getChildAt(n);
				if(viewTag instanceof CheckBox){
					if(((CheckBox)viewTag).isChecked()){
						tagsSeleccionados.add((String)viewTag.getTag());
						Log.d(TAG, "Tag en filtro: "+(String)viewTag.getTag());
					}
				}
			}
		}
		
		if(tagsSeleccionados.size() != 0){ //algún tag seleccionado, filtramos
			
			for(Produto nProduto:listaProdutos){
				
				if(nProduto.getATags() == null)
					continue;
				
				for (Tag tagProduto:nProduto.getATags()){
					
					if(tagsSeleccionados.contains(tagProduto.getName())){
						tempListaProdutos.add(nProduto);
						break;
					}
				}
			}
		
		}else{ //ningún tag seleccionado, entonces no se filtra
			
			Log.d(TAG, "Ningún tag seleccionado");
			tempListaProdutos = listaProdutos;
		}

		return tempListaProdutos;
	}


	public void getTags() {

		adapterTag = new AdapterTags(getActivity(), tagList, this, true);

		ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
		query.findInBackground(new FindCallback<Tag>() {

			@Override
			public void done(List<Tag> objects, ParseException e) {
				tagList = (ArrayList<Tag>) objects;
				adapterTag.clear();
				adapterTag.addAll(tagList);
				mGridTags.setAdapter(adapterTag);
			}
		});
	}
	
	
	/**
	 * Engadese un produto a lista. Se xa está na lista, engadese unha unidade.
	 * @param producto
	 */
	public void addProductToList(final Lista listaSelected,Produto producto) throws IllegalStateException{
		
		if(producto == null){
			
			Toast.makeText(getActivity(), "Produto non válido", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getActivity(), "Agregada una unidad de "+unidadSeleccionada.getProduct().getTitle() + ". Total "+unidadSeleccionada.getNumberUnits(), Toast.LENGTH_SHORT).show();
		
		}else{ //Nuevo producto a la lista
			
			final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(),
					"Agregando produto novo a lista");
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
									Toast.makeText(getActivity(), "Erro ao gardar a lista", Toast.LENGTH_SHORT).show();
									
								}else{

									Toast.makeText(getActivity(), "Agregouse o novo produto", Toast.LENGTH_SHORT).show();
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

	//Getters & Setters
	
	public Produto getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(Produto productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}
	
	
}
