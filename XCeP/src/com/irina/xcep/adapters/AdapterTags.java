package com.irina.xcep.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.irina.xcep.DetailListFragment;
import com.irina.xcep.R;
import com.irina.xcep.model.Tag;

public class AdapterTags extends ArrayAdapter<Tag> {
	

	Context mContext;
	DetailListFragment fragmentDetalleLista;
    
	public AdapterTags(Context context, ArrayList<Tag> tags, DetailListFragment fragmentDetalleLista) {
       super(context, 0, tags);
       this.mContext = context;
       this.fragmentDetalleLista = fragmentDetalleLista;
    }

    @Override
    public View getView(int position, View celdaView, ViewGroup parent) {
       //Recuperar o elemento de datos para esta posición
       final Tag tagList = getItem(position);    
              
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, parent, false);
       }
       
       //Buscar Vista para recheo de datos
    
       ((TextView) celdaView.findViewById(R.id.tagNameGrid)).setText(tagList.getName());
       CheckBox checkbox = (CheckBox)celdaView.findViewById(R.id.checkBoxTag);
       checkbox.setTag(tagList.getName());
       checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			fragmentDetalleLista.actualizarCatalogo();
			return;
		}
	});
       return celdaView;
   }
    

}
