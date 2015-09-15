package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.adapters.AdapterGridAddShoppingList;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class AddShoppingListActivity extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	ArrayList<Supermercado> supermercados = new ArrayList<Supermercado>();
	GridView gridSupermercados;
	AdapterGridAddShoppingList adapterSupermercados;
	private EditText nameList;
	private String nameListtxt;
	private Supermercado supermercadoNuevaLista;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_shopping_list);
		getActionBar().setTitle(getString(R.string.title_action_bar_add_shopping_list));
		
		btncancel = (ButtonRectangle) findViewById(R.id.cancelar_new_list);
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		btnacept = (ButtonRectangle) findViewById(R.id.crear_new_list);
		btnacept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Engadimos a nova lista a BD
				engadirLista();
			}
		});
		
		gridSupermercados=(GridView)findViewById(R.id.grid_logo_market);
        gridSupermercados.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		gridSupermercados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		        	if (supermercados.size()== position){ 
		        		Intent intent = new Intent(AddShoppingListActivity.this, AddMarketActivity.class);
		                startActivity(intent);
		                
		            }else {
		            	supermercadoNuevaLista = supermercados.get(position);
		            }
		          }
		});
		 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reloadAddshoppingList();
	}
	
	protected void reloadAddshoppingList() {
			
		adapterSupermercados = new AdapterGridAddShoppingList(AddShoppingListActivity.this, supermercados);
        gridSupermercados.setAdapter(adapterSupermercados);
        
        ParseQuery<Supermercado> query = ParseQuery.getQuery(Supermercado.class);
		query.findInBackground(new FindCallback<Supermercado>() {
			
			@Override
			public void done(List<Supermercado> objects, ParseException e) {
				
				supermercados = (ArrayList<Supermercado>) objects;
				adapterSupermercados = new AdapterGridAddShoppingList(AddShoppingListActivity.this, supermercados);
		        gridSupermercados.setAdapter(adapterSupermercados);
			}
		});
        
	}


	protected void engadirLista() {
			
		Lista novaLista = new Lista();
		
		//Nome da lista
		nameList = (EditText) findViewById(R.id.text_name_list);
		nameListtxt = nameList.getText().toString();
		
		
		boolean allfilled = true;
		allfilled =  Utils.isNotEmpty(nameList, nameListtxt);
		if(!allfilled){
			supermercadoNuevaLista = null;
			return;
		}
		
		novaLista.setName(nameListtxt);
		
		//Id supermercado seleccionado
		if (supermercadoNuevaLista == null ){
			
			Toast.makeText(AddShoppingListActivity.this, getString(R.string.non_seleccionou_super), Toast.LENGTH_SHORT).show();
		}else{
			
			final ProgressDialog progress = Utils.crearDialogoEspera(this,
					getResources().getString(R.string.creando_lista));
			progress.show();
			
			novaLista.setSupermercado(supermercadoNuevaLista);
			novaLista.setIdUser(ParseUser.getCurrentUser());
			novaLista.setSupermercado(ParseObject.createWithoutData("Market", supermercadoNuevaLista.getObjectId()));
			//novaLista.put("PidMarket", ParseObject.createWithoutData("Market", supermercadoNuevaLista.getObjectId()));
			novaLista.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException arg0) {
					if (arg0 == null){
						Toast.makeText(AddShoppingListActivity.this, getString(R.string.engadimos_a_nova_lista) + nameListtxt, Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(AddShoppingListActivity.this, getString(R.string.error_add_list)+" " + nameListtxt, Toast.LENGTH_SHORT).show();
					}
					
					progress.dismiss();
				}
			});
		}
	}
}
