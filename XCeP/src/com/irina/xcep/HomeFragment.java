package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.adapters.AdapterListas;
import com.irina.xcep.model.Lista;
import com.irina.xcep.utils.FragmentIndexes;
import com.irina.xcep.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HomeFragment extends Fragment {
	
	// Declaraci�n de variables
	public static final String TAG = HomeFragment.class.getName();
	ButtonRectangle logout;
	ListView list;
	List<ParseObject> ob;
	AdapterListas adapter;
	public static ArrayList<Lista> misListas = new ArrayList<Lista>();
	ImageButton addlist;
	// Solicitar usuario actual do Parse.com
	ParseUser currentUser = ParseUser.getCurrentUser();
	
	String objid = "";
	String nameListtxt= "";
	
	
	
	public static HomeFragment newInstance (int Index){
		
		Log.d(TAG, "HomeFragment newInstance");
		
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		
		args.putInt("Index", Index);
		
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Log.d(TAG, "OncreateView");
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
		adapter = new AdapterListas(getActivity(), misListas);
		//Click proglongado para a modificaci�n dunha lista
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
            	objid = misListas.get(pos).getObjectId();
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
					((MenuActivity)getActivity()).mListSelected = misListas.get(position);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
	        	((MenuActivity)getActivity()).loadFragment(FragmentIndexes.FRAGMENT_LIST);
	            }
	    });
		
		return home;
	}
	
	public void reloadUserShoppingLists(boolean actualizarServidor) {
		//Recreamos o conxunto de listas de compra do usuario
		list.setAdapter(adapter);
		Log.d(TAG, "reloadUserShoppingLists"); 

		if(actualizarServidor){
			final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(), "Actualizando listas");
     	   	progress.show();
			ParseQuery<Lista> query = ParseQuery.getQuery(Lista.class);
			query.include("Market");
			//Filtramos as lista para cada usuario logueado na app
			query.include("User");
			query.whereEqualTo("idUser", currentUser);
			query.include("Products");
			query.include("Price");
			query.include("UnitsProduct");
			query.findInBackground(new FindCallback<Lista>() {
				@Override
				public void done(List<Lista> objects, ParseException e) {
					if(e!= null){
						Toast.makeText(getActivity(), "Error no borrado", Toast.LENGTH_SHORT).show();
					}
					
					misListas = (ArrayList<Lista>) objects;
					
					if(misListas != null){
						adapter.clear();
						adapter.addAll(misListas);
						//adapter.setNotifyOnChange(true);
					}
					
					progress.dismiss();
				}
			});
		}else{
			adapter.setNotifyOnChange(true);
		}
				
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//reloadUserShoppingLists();
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		reloadUserShoppingLists(true);
	}


	public void showDialogoModificarProducto(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Modificaci�ns da lista" );
		builder.setMessage("�Que desexa facer? ");
		builder.setPositiveButton("Eliminar a lista", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   		Utils.hideSoftKeyboard(getActivity());
		        	   		final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(), "Eliminando lista");
		        	   		progress.show();
		        	   		
		        		    ParseQuery<Lista> query=ParseQuery.getQuery(Lista.class);
		        		    query.whereEqualTo("objectId",objid);
		        		    query.findInBackground(new FindCallback<Lista>() {
		        		        @Override
		        		        public void done(List<Lista> parseObjects, ParseException e) {
		        		            if(e==null) {

		        		                for (ParseObject delete : parseObjects) {
		        		                    delete.deleteInBackground();
		        		                }
		        		                
		        		                progress.dismiss();
		        		                reloadUserShoppingLists(true);
		        		            }else{
		        		            	progress.dismiss();
		        		                Toast.makeText(getActivity(), "Error no borrado", Toast.LENGTH_SHORT).show();
		        		            }
		        		        }
		        		    });
		        	}
		       });
		builder.setNeutralButton("Cambiar Nome", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
				final LayoutInflater inflater = getActivity().getLayoutInflater();
				
				popDialog.setTitle("Cambiar o nome da lista ");
				popDialog.setView(inflater.inflate(R.layout.activity_dialog_change_name_list, null))
				           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				               @Override
				               public void onClick(DialogInterface dialog, int id) {
				              
				            	   Utils.hideSoftKeyboard(getActivity());
				            	   final ProgressDialog progress = Utils.crearDialogoEspera(getActivity(), "Cambiando nombre");
				            	   progress.show();
				            	   EditText newNameList = (EditText) ((AlertDialog) dialog).findViewById(R.id.NameListNew);
				                   nameListtxt = newNameList.getText().toString();
				                   
				                   ParseQuery<Lista> query=ParseQuery.getQuery(Lista.class);
				        		   query.whereEqualTo("objectId",objid);
				        		   query.findInBackground(new FindCallback<Lista>() {
				        		   @Override
				        		   public void done(List<Lista> parseObjects, ParseException e) {
				        			   
				        			   if(e!= null){
				        				   progress.dismiss();
				        				   Toast.makeText(getActivity(), "Produciuse un erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
				        				   return;
				        			   }
				        			   
				        		       if(parseObjects.size()==1)	{
				        		            		parseObjects.get(0).setNome(nameListtxt);
				        		            		//parseObjects.get(0).saveInBackground();
				        		            		parseObjects.get(0).saveInBackground(new SaveCallback() {
														
														@Override
														public void done(ParseException e) {
															progress.dismiss();
															if(e!= null){
																Toast.makeText(getActivity(), "Produciuse un erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
															}else{
																reloadUserShoppingLists(true);
															}
														}
														
													});
				        		            }    				        		           
				        		        }
				        		    });
				               }
				           })
				           .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				               public void onClick(DialogInterface dialog, int id) {
				                   dialog.cancel();
				               }
				           });      
				
				 popDialog.create();
				 popDialog.show(); 
				            	
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
	
	


}
