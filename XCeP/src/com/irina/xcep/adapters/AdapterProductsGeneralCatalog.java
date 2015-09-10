package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Produto;
import com.squareup.picasso.Picasso;

@SuppressWarnings("unchecked")
public class AdapterProductsGeneralCatalog extends BaseAdapter implements Filterable{

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();
	private  List<Produto> mProductos, mProductosFiltrados;
	private Context mContext;
	
	public AdapterProductsGeneralCatalog(Context context, ArrayList<Produto> productos) {
		super();
		mProductos = productos;
		mProductosFiltrados = productos;
		mContext = context;
	}
	
	
	@Override
	public int getCount() {
		
		return mProductosFiltrados.size();
	}

	@Override
	public Object getItem(int position) {
		return mProductosFiltrados.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View celdaView, ViewGroup parent) {
		
		if (celdaView == null) {
			celdaView = LayoutInflater.from(mContext).inflate(
					R.layout.item_product_catalog_list, parent, false);
		}
		
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView nombreProductoTextView = ((TextView) celdaView.findViewById(R.id.name_product));
		TextView precioProductoTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		precioProductoTextView.setVisibility(View.INVISIBLE);
		
		// Recuperar o elemento de datos para esta posición
		Produto producto = (Produto)getItem(position);
		
		nombreProductoTextView.setText(producto.getTitle());
		
		Bitmap bmp = mImagenes.get(producto.getIcon());

		if (bmp != null) {
			productoImageView.setImageBitmap(bmp);
		} else {

			Picasso.with(mContext).load(producto.getIcon().getUrl()).into(productoImageView);
		}

		celdaView.setTag(producto);
		return celdaView;
	}
	

	@Override
	public Filter getFilter() {
		
		return new Filter(){

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				
				if((constraint == null) || (constraint.length() == 0)){
					return null;
				}
				
				String filtroTexto = constraint.toString().toLowerCase(Locale.getDefault());
				List<Produto> listaProdutosTemp = new ArrayList<Produto>();
				
				for(Produto produto:mProductos){ //El texto escrito por el usuario se encuentre en el título, marca o descripción
					if(produto.getTitle().toLowerCase(Locale.getDefault()).contains(filtroTexto) || 
						produto.getDescripcion().toLowerCase(Locale.getDefault()).contains(filtroTexto) || 
						produto.getMarca().toLowerCase(Locale.getDefault()).contains(filtroTexto)){
						
							listaProdutosTemp.add(produto);
					}else{
							continue;
					}
				}
				
				FilterResults results = new FilterResults();
				results.values = listaProdutosTemp;
				results.count = listaProdutosTemp.size();
				
				return results;
			}


			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				
				if(results == null){
					mProductosFiltrados = mProductos;
				}else{
					mProductosFiltrados = (List<Produto>) results.values;
				}
				
				notifyDataSetChanged();
			}
			
		};
		
	}
	

}
