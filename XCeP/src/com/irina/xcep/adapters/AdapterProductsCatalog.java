package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.squareup.picasso.Picasso;

public class AdapterProductsCatalog extends ArrayAdapter<Produto> {

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();

	private Supermercado supermercado;
	
	public AdapterProductsCatalog(Context context, ArrayList<Produto> productos, Supermercado supermercado) {
		super(context, 0, productos);
		this.supermercado = supermercado;
	}

	@Override
	public View getView(int position, View celdaView, ViewGroup parent) {
		
		if (celdaView == null) {
			celdaView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_product_catalog_list, parent, false);
		}
		
		TextView precioTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView nombreProductoTextView = ((TextView) celdaView.findViewById(R.id.name_product));
		
		
		// Recuperar o elemento de datos para esta posici�n
		Produto producto = getItem(position);
		
		nombreProductoTextView.setText(producto.getTitle());

		List<Prezo> listaPrezos = producto.getAPrice();
		Prezo precioProducto=null;
		
		for(Prezo nPrezo:listaPrezos){
			if(nPrezo.getPidMarket().getObjectId().equals(supermercado.getObjectId())){
				precioProducto = nPrezo;
				break;
			}			
		}
		
		if(precioProducto != null){
			precioTextView.setText(precioProducto.getPrice().toString()+ " �");
		}else{
			precioTextView.setText("N.D.");
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
