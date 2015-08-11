package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.irina.xcep.model.Produto;
import com.squareup.picasso.Picasso;

public class AdapterProductsCatalog extends ArrayAdapter<Produto> {

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();

	public AdapterProductsCatalog(Context context, ArrayList<Produto> productos) {
		super(context, 0, productos);
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
		
		
		// Recuperar o elemento de datos para esta posición
		Produto producto = getItem(position);
		
		precioTextView.setText(producto.get("Price2")+" € ");
		nombreProductoTextView.setText(producto.getNome());
		
		Bitmap bmp = mImagenes.get(producto.getIcon());

		if (bmp != null) {
			productoImageView.setImageBitmap(bmp);
		} else {

			Picasso.with(getContext()).load(producto.getIcon().getUrl()).into(productoImageView);
		}

		return celdaView;
	}

}
