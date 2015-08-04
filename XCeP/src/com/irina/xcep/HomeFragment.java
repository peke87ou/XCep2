package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.adapters.AdapterListas;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Produto;
import com.irina.xcep.utils.FragmentIndexes;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeFragment extends Fragment {
	
	// Declaraci�n de variables
	ButtonRectangle logout;
	ListView list;
	List<ParseObject> ob;
	AdapterListas adapter;
	ArrayList<Lista> misListas = new ArrayList<Lista>();
	ImageButton addlist;
	// Solicitar usuario actual do Parse.com
	ParseUser currentUser = ParseUser.getCurrentUser();
	
	public static HomeFragment newInstance (int Index){
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		
		args.putInt("Index", Index);
		
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout home = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
		
		// Convertir currentUser en String
		String struser = currentUser.getUsername().toString();
		TextView txtuser = (TextView) home.findViewById(R.id.txtuser);
		txtuser.setText(this.getString(R.string.text_login_home_user )+ " "  + struser);
		
		//Bot�n desconectarse da app
		logout = (ButtonRectangle) home.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Desconectar o current user
				ParseUser.logOut();
				getActivity().finish();
			}
		});
		
		//Bot�n engadir nova lista
		addlist = (ImageButton) home.findViewById(R.id.add_list);
		addlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Ir a p�xina engadir unha lista
				Intent intent = new Intent(getActivity(), AddShoppingListActivity.class);
				startActivity(intent);
				
			}
		});
		
		//Listas da compra
		list = (ListView) home.findViewById(R.id.lista_list);
		
		
		return home;
	}
	
	public void reloadUserShoppingLists() {
		//Recreamos o conxunto de listas de compra do usuario
		
		adapter = new AdapterListas(getActivity(), misListas);
		list.setAdapter(adapter);
				
		ParseQuery<Lista> query = ParseQuery.getQuery(Lista.class);
		query.include("Market");
		//Filtramos as lista para cada usuario logueado na app
		query.include("User");
		query.whereEqualTo("idUser", currentUser);
		query.include("Products");
		query.findInBackground(new FindCallback<Lista>() {
			@Override
			public void done(List<Lista> objects, ParseException e) {
				misListas = (ArrayList<Lista>) objects;
				adapter.clear();
				if(misListas != null){
					adapter.addAll(misListas);
				}else{
					Toast.makeText(getActivity(), R.string.empty_list, Toast.LENGTH_LONG).show();
				}
			}
		});
		//Click proglongado para a modificaci�n dunha lista
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
            	showDialogoModificarProducto();
            	return true;
            }
        }); 
		
		
		//Click para acceder o detalle da lista
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	((MenuActivity)getActivity()).mNameList = misListas.get(position).getNome();
	        	try {
					((MenuActivity)getActivity()).mMarketSelected = misListas.get(position).getIdSupermercado().getQuery().getFirst();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	        	((MenuActivity)getActivity()).loadFragment(FragmentIndexes.FRAGMENT_LIST);
	            }
	         });
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reloadUserShoppingLists();
	}
	
	
	
	public void showDialogoModificarProducto(){
		
//		// Mirar se existe na BD
//		ParseQuery<Produto> productos = ParseQuery.getQuery(Produto.class);
//		 
//		productos.whereEqualTo("idBarCode",resultadoBarCode);
//		productos.findInBackground(new FindCallback<Produto>() {
//			@Override
//			public void done(List<Produto> objects, ParseException e) {
//					Log.i("jklsdfjklsdfsdfjkl",objects.size()+"resultado"+resultadoBarCode+"");
//					if (objects.size() > 0){
//						isProductoEnParse = false;
//					}else{
//						isProductoEnParse = true;
//					}
//				
//			}
//		});
//	
	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Modificaci�ns da lista" );
		builder.setMessage("�Que desexa facer? ");
		// Add the buttons
		builder.setPositiveButton("Eliminar a lista", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
//		        	   if(isProductoEnParse){
//		        		   barcode = resultadoBarCode;
//			        	   //TODO Agregar resultadoBarCode a parse
//			        	   resultadoBarCode = null;
//			        	   Intent intent = new Intent(getActivity(), AddProductActivity.class);
//			        	   Log.i("QUE ENVIA", barcode);
//			        	   intent.putExtra("MESSAGE",barcode);  
//		                   startActivityForResult(intent, 1);
//		        	   }else{
//		        		   barcode = resultadoBarCode;
//		        		   resultadoBarCode = null;
////		        		   Toast.makeText(getActivity(), "DETALLE DE PRODUTO", Toast.LENGTH_LONG).show();
//		        		   Intent intent = new Intent(getActivity(), DetailProduct.class);
////			        	   Log.i("QUE ENVIA", barcode);
////			        	   intent.putExtra("MESSAGE",barcode);  
//		                   startActivityForResult(intent, 1);
//		        	   }
		        	  
		        	 
		        	  
		           }
		       });
		builder.setNeutralButton("Cambiar Nome", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		builder.setNegativeButton("Pechar", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	 dialog.dismiss();
		           }
		       });
		AlertDialog dialogo = builder.create();
		dialogo.show();
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
//	           
//
//	        }
//	    };
}