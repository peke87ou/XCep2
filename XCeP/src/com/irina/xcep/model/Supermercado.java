package com.irina.xcep.model;

import java.util.ArrayList;

import com.irina.xcep.R;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Market")
@SuppressWarnings("unchecked")
public class Supermercado extends ParseObject{
	
	public Supermercado() {
		super("Market");
	}

	public Supermercado(String theClassName) {
		super("Market");
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
		saveInBackground();
	}

	public ParseFile getImage() {
		return getParseFile("Image");
	}

	public void setImage(ParseFile image) {
		put("Image", image);
		saveInBackground();
	}
	

	public ArrayList<Produto> getAProduct() {
		return (ArrayList<Produto>)get("AProduct");
	}
	
	public void addAproduct(String objectId) {
		ParseObject punteroUnidad = ParseObject.createWithoutData("Products", objectId);
		add("AProduct", punteroUnidad);
	}
	
	public boolean containsProduct(Produto productoInput){
		if(productoInput == null){
			return false;
		}
		
		for (Produto producto:getAProduct()){
			if (producto.getObjectId().equals(productoInput.getObjectId())){
				return true;
			}
		}
		
		return false;
	}
}
