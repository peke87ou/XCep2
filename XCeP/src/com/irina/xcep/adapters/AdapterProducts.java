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
       //Recuperar o elemento de datos para esta posici�n
       final Produto productosList = getItem(position);    
              
       // Comproba se unha vista existente est� a ser reutilizado , se non inflar a vista
       if (celdaView == null) {
          celdaView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_shopping_list, parent, false);
       }
       
       //Buscar Vista para recheo de datos
       Log.i("Productos", productosList+"");
       ((TextView) celdaView.findViewById(R.id.name_product)).setText(productosList.getNome());

       ImageView imageView = (ImageView)celdaView.findViewById(R.id.image_product);
       

     //PRICE
           try {
        	 String precioProducto = productosList.getPrezoPorSupermercado().getQuery().getFirst().getPrice() + "";
        	 
        	 Log.e("NumeroEncontrado", precioProducto);
        	 
	     ((TextView) celdaView.findViewById(R.id.price_product)).setText(precioProducto);
	    
	     } catch (ParseException e) {
	     ((TextView) celdaView.findViewById(R.id.price_product))
	     .setText("ERROR");
	     e.printStackTrace();
	     }
     
     String unidadesProducto =  "";
	try {
		unidadesProducto = productosList.getPrezoPorSupermercado().getQuery().getFirst().getUnits().getQuery().getFirst().getNumberProduct() + "";
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}    
     ((TextView) celdaView.findViewById(R.id.products_list)).setText(unidadesProducto);
   
   Bitmap bmp = mImagenes.get(productosList.getUrlImaxe());
     
   if(bmp != null){
	   imageView.setImageBitmap(bmp);
   }else{
	   Log.d("Imagen producto: ", productosList.getUrlImaxe());
	   Picasso.with(getContext()).load(productosList.getUrlImaxe()).into(imageView);
   }
       
       
      
//       ParseRelation<ParseObject> relation = productosList.getRelation("idMarket");
       
//       final String objectId = productosList.getObjectId();
     // Bitmap bmp = mImagenes.get(productosList.getUrlImaxe());
       
//       if(bmp != null){
//    	   imageView.setImageBitmap(bmp);
//       }else{
//    	   downloadBitmap(relation, imageView);
//       }
//       int i = 0;
       //Buscar Vista para recheo de datos
//        try {
//			 i = productosList.getIdProducts().getQuery().count();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        String productos = "";
//        if(i == 1){
//        	productos = i + " Producto";
//        }else{
//        	productos = i + " Productos";
//        }
        	
//		((TextView) celdaView.findViewById(R.id.products_list)).setText(productos);
	
       
       return celdaView;
   }
    
}
