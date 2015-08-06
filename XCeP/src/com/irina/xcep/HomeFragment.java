package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeFragment extends Fragment {
	
	// Declaración de variables
	ButtonRectangle logout;
	ListView list;
	List<ParseObject> ob;
	AdapterListas adapter;
	ArrayList<Lista> misListas = new ArrayList<Lista>();
	ImageButton addlist;
	// Solicitar usuario actual do Parse.com
	ParseUser currentUser = ParseUser.getCurrentUser();
	
	String objid = "";
	
	String nameListtxt= "";
	
	
	
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
		
		//Botón desconectarse da app
		logout = (ButtonRectangle) home.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Desconectar o current user
				ParseUser.logOut();
				getActivity().finish();
			}
		});
		
		//Botón engadir nova lista
		addlist = (ImageButton) home.findViewById(R.id.add_list);
		addlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Ir a páxina engadir unha lista
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
		//Click proglongado para a modificación dunha lista
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
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Modificacións da lista" );
		builder.setMessage("¿Que desexa facer? ");
		builder.setPositiveButton("Eliminar a lista", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	  
		        		    ParseQuery<Lista> query=ParseQuery.getQuery(Lista.class);
		        		    query.whereEqualTo("objectId",objid);
		        		    query.findInBackground(new FindCallback<Lista>() {
		        		        @Override
		        		        public void done(List<Lista> parseObjects, ParseException e) {
		        		            if(e==null) {

		        		                for (ParseObject delete : parseObjects) {
		        		                    delete.deleteInBackground();
		        		                    Toast.makeText(getActivity(), "Borramos a lista seleccionada", Toast.LENGTH_SHORT).show();
		        		                    onResume();
		        		                }
		        		            }else{
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
				              
				            	   EditText newNameList = (EditText) ((AlertDialog) dialog).findViewById(R.id.NameListNew);
				                   nameListtxt = newNameList.getText().toString();
				                   
				                   ParseQuery<Lista> query=ParseQuery.getQuery(Lista.class);
				        		   query.whereEqualTo("objectId",objid);
				        		   query.findInBackground(new FindCallback<Lista>() {
				        		   @Override
				        		   public void done(List<Lista> parseObjects, ParseException e) {
				        		            if(parseObjects.size()==1)	{
				        		            		parseObjects.get(0).setNome(nameListtxt);
				        		            		parseObjects.get(0).saveInBackground();
				        		            		onResume();
				        		                    Toast.makeText(getActivity(), "Cambiamos o nombre", Toast.LENGTH_LONG).show();
				        		                   
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