package com.irina.xcep.adapters;

import java.util.ArrayList;

import android.app.Fragment;
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
import com.irina.xcep.GeneralCatalogFragment;
import com.irina.xcep.R;
import com.irina.xcep.model.Tag;

public class AdapterTags extends ArrayAdapter<Tag> {
	

	Context mContext;
	DetailListFragment mFragmentDetalleLista;
	GeneralCatalogFragment mGeneralCatalogFragment;
	boolean isGeneral;
    
	/**
	 * 
	 * @param context
	 * @param tags
	 * @param fragment
	 * @param isGeneral Indica si hace referencia a un catalogo general, o al catálogo de un supermercado
	 */
	
	public AdapterTags(Context context, ArrayList<Tag> tags, Fragment fragment, boolean isGeneral) {
       super(context, 0, tags);
       this.mContext = context;
       this.isGeneral = isGeneral;
       
       if(isGeneral){
    	   
    	   mGeneralCatalogFragment = (GeneralCatalogFragment) fragment;
    	   
       }else{
    	   mFragmentDetalleLista = (DetailListFragment) fragment;
       }
       
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
			
			//fragmentDetalleLista.actualizarCatalogo();
			
		       if(isGeneral){
		    	   
		    	   mGeneralCatalogFragment.actualizarCatalogoGeneral(false);
		    	   
		       }else{
		    	   mFragmentDetalleLista.actualizarCatalogo();
		       }
		       
			return;
		}
	});
       return celdaView;
   }
    

}
