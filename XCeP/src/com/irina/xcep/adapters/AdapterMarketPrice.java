package com.irina.xcep.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.squareup.picasso.Picasso;

public class AdapterMarketPrice extends ArrayAdapter<String> {
	
	private Context mContext;
	private ArrayList<String> supermercadoUrlList;
	private ArrayList<String> supermercadoPrecioList;
	private String idMarketList;
	private ArrayList<String> supermercadoIdList;
    
	public AdapterMarketPrice(Context context, ArrayList<String> supermercadoUrlList, ArrayList<String> supermercadoPrecioList, String idMarketList, ArrayList<String > supermercadoIdList) {
       super(context, 0, supermercadoPrecioList);
       mContext = context;
       this.supermercadoPrecioList = supermercadoPrecioList;
       this.supermercadoUrlList = supermercadoUrlList;
       this.idMarketList = idMarketList;
       this.supermercadoIdList = supermercadoIdList;
    }

	@Override
    public View getView(int position, View celdaView, ViewGroup parent) {
		
		String urlSupermercado = supermercadoUrlList.get(position);
		String precioSupermercado = supermercadoPrecioList.get(position);
		String idSupermercado = supermercadoIdList.get(position);
		
       // Comproba se unha vista existente está a ser reutilizado , se non inflar a vista
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_price_market, parent, false);
       }
       
       if(idSupermercado.equals(idMarketList)){
    		   celdaView.setBackgroundColor(mContext.getResources().getColor(R.color.verde_oscuro));
    		   ((TextView) celdaView.findViewById(R.id.TextViewPrezo)).setTextColor(mContext.getResources().getColor(android.R.color.black));
       }
       
       
       //Buscar Vista para recheo de datos
       ((TextView) celdaView.findViewById(R.id.priceProduct)).setText( precioSupermercado + mContext.getResources().getString(R.string.simbol_euro));
       final ImageView imageView = (ImageView)celdaView.findViewById(R.id.ImageMarket);
       Picasso.with(mContext ).load(urlSupermercado).into(imageView);
	   
       return celdaView;
   }
}
