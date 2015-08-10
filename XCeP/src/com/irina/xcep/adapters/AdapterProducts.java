package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.irina.xcep.model.Supermercado;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.squareup.picasso.Picasso;

public class AdapterProducts extends ArrayAdapter<Produto> {
	
	private static Map<String, Bitmap> mImagenes = new HashMap<String, Bitmap>();
    
	public AdapterProducts(Context context, ArrayList<Produto> productos) {
       super(context, 0, productos);
    }

    @Override
    public View getView(int position, View celdaView, ViewGroup parent) {
       //Recuperar o elemento de datos para esta posición
       final Produto productosList = getItem(position);    
              
       // Comproba se unha vista existente está a ser reutilizado , se non inflar a vista
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
    	  String unidadesProducto = productosList.getPrezoPorSupermercado().getQuery().getFirst().getUnits().getQuery().getFirst().getNumberProduct() + "";
    	  Log.e("NumeroEncontrado", precioProducto);
    	  Log.e("unidadesEncontradas", unidadesProducto);
		((TextView) celdaView.findViewById(R.id.price_product)).setText(precioProducto);
	} catch (ParseException e) {
		((TextView) celdaView.findViewById(R.id.price_product))
		.setText("ERROR");
		e.printStackTrace();
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
     	               
     	             Picasso.with(getContext()).load(urlBitmap).into(imageView);
     	             //new AsyncTaskDownloadImage(imageView).execute(urlBitmap,superRelacionado);
     	           }
     	       } 
     	   }
     	});
    }
}
