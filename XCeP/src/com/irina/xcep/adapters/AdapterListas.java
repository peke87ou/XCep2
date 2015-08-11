package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.model.Lista;
import com.irina.xcep.model.Produto;
import com.irina.xcep.model.Supermercado;
import com.irina.xcep.model.Units;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.squareup.picasso.Picasso;

public class AdapterListas extends ArrayAdapter<Lista> {
	
	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();
	private Context mContext;
    
	public AdapterListas(Context context, ArrayList<Lista> lista) {
       super(context, 0, lista);
       mContext = context;
    }

    @Override
    public View getView(int position, View celdaView, ViewGroup parent) {
       //Recuperar o elemento de datos para esta posición
       final Lista lista = getItem(position);    
       
       // Comproba se unha vista existente está a ser reutilizado , se non inflar a vista
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_list, parent, false);
       }
       
       //Buscar Vista para recheo de datos
       ((TextView) celdaView.findViewById(R.id.name_list)).setText(lista.getNome());
       final ImageView imageView = (ImageView)celdaView.findViewById(R.id.imageMarketList);
       TextView textViewProductos = ((TextView) celdaView.findViewById(R.id.products_list));
       //List<Produto> objeto = (ArrayList<Produto>) lista0.get("idProducts2");
       Supermercado supermercado = (Supermercado)lista.get("PidMarket");
       List<Units> unidadesProducto = (ArrayList<Units>)lista.get("AidUnits");
    
       
	   if(supermercado!=null){
		    Picasso.with(mContext ).load(supermercado.getUrlLogo().getUrl()).into(imageView);
	   }
		
       
	   String numeroProductos;
	   
	   if(unidadesProducto != null){
		   
		   int nUnidades = unidadesProducto.size();
		   
		   if(nUnidades == 1){
			   numeroProductos = nUnidades + " producto";
		   }else{
			   numeroProductos = nUnidades + " productos";
		   }
		   
	   }else{
		   numeroProductos = "0 productos";
	   }
       
	   textViewProductos.setText(numeroProductos);
	   
       if(true)
    	   return celdaView;
       
       ParseRelation<ParseObject> relation = lista.getRelation("idMarket");
       
       final String objectId = lista.getObjectId();
       Bitmap bmp = mImagenes.get(objectId);
       
       if(bmp != null){
    	   imageView.setImageBitmap(bmp);
       }else{
    	   downloadBitmap(relation, imageView);
       }
       
       
       //Buscar Vista para recheo de datos	
		if(textViewProductos.getText().length()==0){
			textViewProductos.setText("Buscando productos...");
			new AsynkTaskGetProductos(textViewProductos, lista).execute();
		}
		return celdaView;
   }
    
    public void downloadBitmap(ParseRelation<ParseObject> relation, final ImageView imageView){
    	
    	ParseQuery<ParseObject> query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
     	   public void done(List<ParseObject> list, ParseException e) {
     	       if (e == null) {
     	           for (ParseObject object : list) {
     	             final Supermercado superRelacionado = ((Supermercado)object);
     	             System.out.println(R.string.text_adapter_list_market + superRelacionado.getNome());
     	              
     	             final ParseFile fileObject = superRelacionado.getUrlLogo();
     	             String urlBitmap = fileObject.getUrl(); 
     	               
     	             Picasso.with(mContext ).load(urlBitmap).into(imageView);
     	             
     	           }
     	       } 
     	   }
     	});
    }
    
    
    
    private class AsynkTaskGetProductos extends AsyncTask<Void, Void, String> {

    	TextView textoProductosCelda;
    	Lista listaProductos;
    	
    	
        public AsynkTaskGetProductos(TextView textoProductosCelda,
				Lista listaProductos) {
			super();
			this.textoProductosCelda = textoProductosCelda;
			this.listaProductos = listaProductos;
		}

		@Override
        protected String doInBackground(Void... params) {
           
	    	if(listaProductos == null)
	    		return "Error de productos";
	        
			String productos = "";
	        try {
	        	int i = 0;
	 			i = listaProductos.getIdProducts().getQuery().count();
	 			if(i == 1){
	 	         	productos = i + " Produto ";
	 	        }else{
	 	         	productos = i + " Produtos";
	 	        }
	 			 
	 		} catch (ParseException e) {
	 			e.printStackTrace();
	 		}
	         
            return productos;
        }

        @Override
        protected void onPostExecute(String result) {
            if(textoProductosCelda!= null)
            	textoProductosCelda.setText(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
