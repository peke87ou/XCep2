package com.irina.xcep.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Tag;

public class AdapterTags extends ArrayAdapter<Tag> {
	

    
	public AdapterTags(Context context, ArrayList<Tag> tags) {
       super(context, 0, tags);
    }

    @Override
    public View getView(int position, View celdaView, ViewGroup parent) {
       //Recuperar o elemento de datos para esta posición
       final Tag tagList = getItem(position);    
              
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, parent, false);
       }
       
       //Buscar Vista para recheo de datos
    
       ((TextView) celdaView.findViewById(R.id.products_list)).setText(tagList.getNome());

       return celdaView;
   }
    

}
