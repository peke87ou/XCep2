package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Units;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

public class AdapterProducts extends ArrayAdapter<Produto> {

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();

	public AdapterProducts(Context context, ArrayList<Produto> productos) {
		super(context, 0, productos);
	}

	@Override
	public View getView(int position, View celdaView, ViewGroup parent) {
		// Recuperar o elemento de datos para esta posición
		final Produto producto = getItem(position);

		// Comproba se unha vista existente está a ser reutilizado , se non
		// inflar a vista
		if (celdaView == null) {
			celdaView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_product_shopping_list, parent, false);
		}

		((TextView) celdaView.findViewById(R.id.name_product)).setText(producto.getNome());
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView unidadesTextView = ((TextView) celdaView.findViewById(R.id.products_list));
		TextView precioTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		
		Prezo prezoProducto;
		Units unitsProducto;
		
		try {
			prezoProducto = producto.getPrezoPorSupermercado().getQuery().getFirst();
			unitsProducto = prezoProducto.getUnits().getQuery().getFirst();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			prezoProducto = null;
			unitsProducto = null;
		}
		
 		if(prezoProducto !=null){
 			
 			precioTextView.setText(prezoProducto.getPrice()+"");
 		}else{
 			
 			precioTextView.setText("--");
 		}

 		if(unitsProducto != null){
 			
 			unidadesTextView.setText(unitsProducto.getNumberProduct()+"");
 		}else{
 			
 			unidadesTextView.setText("--");
 		}


		Bitmap bmp = mImagenes.get(producto.getIcon());

		if (bmp != null) {
			productoImageView.setImageBitmap(bmp);
		} else {

			Picasso.with(getContext()).load(producto.getIcon().getUrl()).into(productoImageView);
		}

		return celdaView;
	}

}
