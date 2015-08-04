package com.irina.xcep;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class AddShoppingListActivity extends Activity{
	
	ButtonRectangle btncancel, btnacept;
	ArrayList<Supermercado> supermercados = new ArrayList<Supermercado>();
	GridView grid;
	Supermercado market;
	AdapterGridAddShoppingList adapter;
	boolean click_item = false;
	private EditText nameList;
	private String nameListtxt;
	private Supermercado idSuper;
	//private int posicion = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_shopping_list);
		getActionBar().setTitle(R.string.title_action_bar_add_shopping_list);
		
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
		reloadAddshoppingList();
	}
		protected void reloadAddshoppingList() {
			
		
		adapter = new AdapterGridAddShoppingList(AddShoppingListActivity.this, supermercados);
		
		grid=(GridView)findViewById(R.id.grid_logo_market);
        grid.setAdapter(adapter);
        grid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        
        
        ParseQuery<Supermercado> query = ParseQuery.getQuery(Supermercado.class);
		query.findInBackground(new FindCallback<Supermercado>() {
			
			@Override
			public void done(List<Supermercado> objects, ParseException e) {

				supermercados = (ArrayList<Supermercado>) objects;
				
				//FIXME Ver como actualizar la lista de supermercados dentro del adapter
				//adapter.clear();
				//adapter.addAll(supermercados);
				adapter = new AdapterGridAddShoppingList(AddShoppingListActivity.this, supermercados);
				grid=(GridView)findViewById(R.id.grid_logo_market);
		        grid.setAdapter(adapter);
		        grid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
			}
		});
        
		
		
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	//Toast.makeText(AddShoppingListActivity.this, "You Clicked at " + supermercados.get(position), Toast.LENGTH_SHORT).show();
        	if (supermercados.size()== position){ //ultima
        		Intent intent = new Intent(AddShoppingListActivity.this, AddMarketActivity.class);
        		//FIXME invocar como startActivityForResult, y manejar el callback para hacer reload de la lista
                startActivity(intent);
          	  
                
            }else {
            	idSuper = supermercados.get(position);
            }
          }
         });
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadAddshoppingList();
	}
	
	protected void engadirLista() {
			
		Lista addlist = new Lista();
		
		//Nome da lista
		nameList = (EditText) findViewById(R.id.text_name_list);
		nameListtxt = nameList.getText().toString();
		
		
		boolean allfilled = true;
		allfilled =  Utils.isNotEmpty(nameList, nameListtxt);
		if(!allfilled){
			idSuper = null;
			return;
		}
		
		addlist.setNome(nameListtxt);
		
		//Id supermercado seleccionado
		if (idSuper == null ){
			
			Toast.makeText(AddShoppingListActivity.this, "Non seleccionou ningún supermercado", Toast.LENGTH_SHORT).show();
		}else{
			addlist.setIdSupermercado(idSuper);
			//Id usuario logeuado
			addlist.setIdUser(ParseUser.getCurrentUser());
			
			addlist.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException arg0) {
					if (arg0 == null){
						Toast.makeText(AddShoppingListActivity.this, "Engadimos a nova lista " + nameListtxt, Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(AddShoppingListActivity.this, R.string.error_add_list+" " + nameListtxt, Toast.LENGTH_SHORT).show();

					}
				}
			});
		}
	}
}
