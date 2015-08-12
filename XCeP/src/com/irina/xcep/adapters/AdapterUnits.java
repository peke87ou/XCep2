package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Units;
import com.squareup.picasso.Picasso;

public class AdapterUnits extends ArrayAdapter<Units> {

	//private static final String TAG = AdapterUnits.class.getName();
	Lista listaPadre;

	public AdapterUnits(Context context, ArrayList<Units> productos, Lista listaPadre) {
		super(context, 0, productos);
		this.listaPadre = listaPadre;
	}

	@Override
	public View getView(int position, View celdaView, ViewGroup parent) {
		
		if (celdaView == null) {
			celdaView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_product_shopping_list, parent, false);
		}
		
		// Recuperar o elemento de datos para esta posición
		Units productoUnidad = getItem(position);
		Produto producto = (Produto)productoUnidad.getProduct();
		
		TextView nombreProductoTextView = ((TextView) celdaView.findViewById(R.id.name_product));
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView unidadesTextView = ((TextView) celdaView.findViewById(R.id.products_list));
		TextView precioTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		
		nombreProductoTextView.setText(producto.getTitle());
		List<Prezo> listaPrezos = producto.getAPrice();
		Prezo precioProducto=null;
		
		for(Prezo nPrezo:listaPrezos){
			if(nPrezo.getPidMarket().getObjectId().equals(listaPadre.getSupermercado().getObjectId())){
				precioProducto = nPrezo;
				break;
			}			
		}
		
		if(precioProducto != null){
			precioTextView.setText(precioProducto.getPrice().toString());
		}else{
			precioTextView.setText("Precio no disponible");
		}
		
 		if (productoUnidad.getNumberUnits().intValue() == 1){
			unidadesTextView.setText(productoUnidad.getNumberUnits()+" Unidade");
		}else{
			unidadesTextView.setText(productoUnidad.getNumberUnits()+" Unidades");
		}
 			
 		Picasso.with(getContext()).load(producto.getIcon().getUrl()).into(productoImageView);
		return celdaView;
	}

}
