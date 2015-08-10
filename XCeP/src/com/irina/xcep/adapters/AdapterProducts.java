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
import com.irina.xcep.model.Produto;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

public class AdapterProducts extends ArrayAdapter<Produto> {

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();

	public AdapterProducts(Context context, ArrayList<Produto> productos) {
		super(context, 0, productos);
	}

	@Override
	public View getView(int position, View celdaView, ViewGroup parent) {
		// Recuperar o elemento de datos para esta posición
		final Produto productosList = getItem(position);

		// Comproba se unha vista existente está a ser reutilizado , se non
		// inflar a vista
		if (celdaView == null) {
			celdaView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_product_shopping_list, parent, false);
		}

		// Buscar Vista para recheo de datos
		Log.i("Productos", productosList + "");
		((TextView) celdaView.findViewById(R.id.name_product))
				.setText(productosList.getNome());

		ImageView imageView = (ImageView) celdaView
				.findViewById(R.id.image_product);

		try {
			String precioProducto = productosList.getPrezoPorSupermercado()
					.getQuery().getFirst().getPrice()
					+ "";

			Log.e("NumeroEncontrado", precioProducto);

			((TextView) celdaView.findViewById(R.id.price_product))
					.setText(precioProducto + " € ");

		} catch (ParseException e) {
			((TextView) celdaView.findViewById(R.id.price_product))
					.setText("ERROR");
			e.printStackTrace();
		}

		String unidadesProductos = "";
		try {
			Number unidadesProducto = productosList.getPrezoPorSupermercado()
					.getQuery().getFirst().getUnits().getQuery().getFirst()
					.getNumberProduct();
			if (unidadesProducto.intValue() == 1){
				unidadesProductos = unidadesProducto +  " Unidade ";
			}else {
				unidadesProductos = unidadesProducto +  " Unidades ";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		((TextView) celdaView.findViewById(R.id.products_list))
				.setText(unidadesProductos);

		Bitmap bmp = mImagenes.get(productosList.getIcon());

		if (bmp != null) {
			imageView.setImageBitmap(bmp);
		} else {
			Log.d("Imagen producto: ", productosList.getIcon().getUrl());
			Picasso.with(getContext()).load(productosList.getIcon().getUrl())
					.into(imageView);
		}

		return celdaView;
	}

}
