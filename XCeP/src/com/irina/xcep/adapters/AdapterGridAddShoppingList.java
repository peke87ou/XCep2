package com.irina.xcep.adapters;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Supermercado;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

public class AdapterGridAddShoppingList extends ArrayAdapter<Supermercado>{
	 Context mContext;
	 List<Supermercado> listaSuper;
	 private Supermercado addSuper;
	 
      public AdapterGridAddShoppingList(Context context, List<Supermercado> lista) {
		super(context,0, lista);
		mContext = context;
		listaSuper = lista;
	}

	@Override
      public View getView(int position, View convertView, ViewGroup parent) {
          View grid;
          LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
          
        	  
          if (convertView == null) {
        	  
              grid = new View(mContext);
              grid = inflater.inflate(R.layout.item_grid_add_shopping_list, null);
              TextView textView = (TextView) grid.findViewById(R.id.title_market_add_list);
              ImageView imageView = (ImageView)grid.findViewById(R.id.image_market_add_list);
              
              if (listaSuper.size() == position){ //ultima
            	
            	  
                  textView.setText("Novo");
                  imageView.setBackgroundResource(R.drawable.ic_content_add_circle);
              }else{
            	  
            	  final Supermercado market = getItem(position);
            	  textView.setText(market.getNome());

                  final ParseFile fileObject = market.getUrlLogo(); 
                  String urlBitmap = fileObject.getUrl(); 
                  Picasso.with(getContext()).load(urlBitmap).into(imageView);
                  
              }
          
          } else {
        	  grid = (View) convertView;

	      }
          return grid;
      }

	@Override
	public int getCount() {
		
		//return super.getCount();
		return listaSuper.size() + 1;
	}

	@Override
	public void addAll(Collection<? extends Supermercado> collection) {
		// TODO Auto-generated method stub
		super.addAll(collection);
	}

	@Override
	public void addAll(Supermercado... items) {
		// TODO Auto-generated method stub
		super.addAll(items);
	}
	
	

}
