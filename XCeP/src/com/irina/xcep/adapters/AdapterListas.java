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
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.model.Units;
import com.squareup.picasso.Picasso;

public class AdapterListas extends ArrayAdapter<Lista> {
	
	private Context mContext;
    
	public AdapterListas(Context context, ArrayList<Lista> lista) {
       super(context, 0, lista);
       mContext = context;
    }

	@Override
    public View getView(int position, View celdaView, ViewGroup parent) {
       //Recuperar o elemento de datos para esta posici�n
       final Lista lista = getItem(position);    
       
       // Comproba se unha vista existente est� a ser reutilizado , se non inflar a vista
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_list, parent, false);
       }
       
       //Buscar Vista para recheo de datos
       ((TextView) celdaView.findViewById(R.id.name_list)).setText(lista.getName());
       final ImageView imageView = (ImageView)celdaView.findViewById(R.id.imageMarketList);
       TextView textViewProductos = ((TextView) celdaView.findViewById(R.id.products_list));
       Supermercado supermercado = (Supermercado)lista.getSupermercado();
       List<Units> unidadesProducto = (ArrayList<Units>)lista.getAIdUnits();
    
       
	   if(supermercado!=null){
		    Picasso.with(mContext ).load(supermercado.getImage().getUrl()).into(imageView);
	   }
		
       
	   String numeroProductos;
	   
	   if(unidadesProducto != null){
		   
		   int nUnidades = unidadesProducto.size();
		   
		   if(nUnidades == 1){
			   numeroProductos = nUnidades + getContext().getString(R.string.produto);
		   }else{
			   numeroProductos = nUnidades + getContext().getString(R.string.produtos);
		   }
		   
	   }else{
		   numeroProductos = getContext().getString(R.string.cero_produtos);
	   }
       
	   textViewProductos.setText(numeroProductos);
	   
      
		return celdaView;
   }
    
}
