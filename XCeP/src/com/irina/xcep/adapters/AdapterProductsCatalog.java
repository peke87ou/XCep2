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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Prezo;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.squareup.picasso.Picasso;

public class AdapterProductsCatalog extends BaseAdapter implements Filterable{

	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();
	private Supermercado supermercado;
	private  List<Produto> mProductos, mProductosFiltrados;
	private Context mContext;
	
	public AdapterProductsCatalog(Context context, ArrayList<Produto> productos, Supermercado supermercado) {
		super();
		this.supermercado = supermercado;
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
		
		TextView precioTextView = ((TextView) celdaView.findViewById(R.id.price_product));
		ImageView productoImageView = (ImageView) celdaView.findViewById(R.id.image_product);
		TextView nombreProductoTextView = ((TextView) celdaView.findViewById(R.id.name_product));
		
		
		// Recuperar o elemento de datos para esta posición
		Produto producto = (Produto)getItem(position);
		
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
			precioTextView.setText(precioProducto.getPrice().toString()+ " €");
		}else{
			precioTextView.setText("N.D.");
		}
		
		Bitmap bmp = mImagenes.get(producto.getIcon());

		if (bmp != null) {
			productoImageView.setImageBitmap(bmp);
		} else {

			Picasso.with(mContext).load(producto.getIcon().getUrl()).into(productoImageView);
		}

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
				
				String filtroTexto = constraint.toString().toLowerCase();
				//mProductosFiltrados = mProductos;
				List<Produto> listaProdutosTemp = new ArrayList<Produto>();
				
				for(Produto produto:mProductos){ //El texto escrito por el usuario se encuentre en el título, marca o descripción
					if(produto.getTitle().toLowerCase().contains(filtroTexto) || 
						produto.getDescripcion().toLowerCase().contains(filtroTexto) || 
						produto.getMarca().toLowerCase().contains(filtroTexto)){
						
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
