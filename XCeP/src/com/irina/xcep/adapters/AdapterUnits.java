package com.irina.xcep.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.DetailListFragment;
import com.irina.xcep.R;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.model.Units;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

public class AdapterUnits extends ArrayAdapter<Units> {

	//private static final String TAG = AdapterUnits.class.getName();
	Lista listaPadre;
	DetailListFragment mFragmentLista;
	

	public AdapterUnits(Context context, Lista listaPadre, DetailListFragment fragmentLista) {
		super(context, 0, listaPadre.getAIdUnits());
		mFragmentLista = fragmentLista;
		this.listaPadre = listaPadre;
	}

	@Override
	public View getView(int position, View celdaView, final ViewGroup parent) {
		
		if (celdaView == null) {
			celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_shopping_list, parent, false);
		}
		
		// Recuperar o elemento de datos para esta posición
		final Units productoUnidad = listaPadre.getAIdUnits().get(position);//getItem(position);
		Produto producto=null;
		
		/*try {
			productoUnidad.fetchIfNeeded();
			producto = (Produto)productoUnidad.getProduct();
			producto.fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		producto = (Produto)productoUnidad.getProduct();
		TextView nombreProductoTextView = ((TextView) celdaView.findViewById(R.id.name_product));
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView unidadesTextView = ((TextView) celdaView.findViewById(R.id.products_list));
		TextView precioTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		CheckBox productoCheckBox = (CheckBox) celdaView.findViewById(R.id.checkBoxProdutoCarrito);
		productoCheckBox.setOnCheckedChangeListener(null);
		productoCheckBox.setChecked(productoUnidad.isChecked());
		productoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				productoUnidad.setChecked(isChecked);
				mFragmentLista.actualizarPrecios();
			}
		});
		
		
		nombreProductoTextView.setText(producto.getTitle());
		List<Prezo> listaPrezos = producto.getAPrice();
		Prezo precioProducto=null;
		
		for(Prezo nPrezo:listaPrezos){
			
			Supermercado nSupermercado=null;
			try {
				nPrezo.fetchIfNeeded();
				nSupermercado = (Supermercado)nPrezo.getPidMarket().fetchIfNeeded();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			 
			
			if(nSupermercado.getObjectId().equals(listaPadre.getSupermercado().getObjectId())){
				precioProducto = nPrezo;
				break;
			}			
		}
		
		
		if(precioProducto != null){
			precioTextView.setText(precioProducto.getPrice().toString() +mFragmentLista.getResources().getString(R.string.simbol_euro));
		}else{
			precioTextView.setText(mFragmentLista.getResources().getString(R.string.no_d_isponible));
		}
		
 		if (productoUnidad.getNumberUnits().intValue() == 1){
			unidadesTextView.setText(productoUnidad.getNumberUnits() + mFragmentLista.getResources().getString(R.string.unidade));
		}else{
			unidadesTextView.setText(productoUnidad.getNumberUnits()+  mFragmentLista.getResources().getString(R.string.unidades));
		}
 		
 		
 		Picasso.with(getContext()).load(producto.getIcon().getUrl()).into(productoImageView);
		return celdaView;
	}
	
}
