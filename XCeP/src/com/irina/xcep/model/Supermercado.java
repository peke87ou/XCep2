package com.irina.xcep.model;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Market")
public class Supermercado extends ParseObject{
	
	public Supermercado() {
		
	}
	
	public Supermercado(String nome, ParseFile urlLogo) {
		
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<Produto> getAProduct() {
		return (ArrayList<Produto>)get("AProduct");
	}
}
