package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Market")
public class Supermercado extends ParseObject{
	
	public Supermercado() {
		
	}
	
	public Supermercado(String nome, ParseFile urlLogo) {
		
	}

	public String getNome() {
		return getString("name");
	}

	public void setNome(String nome) {
		put("name", nome);
	}

	public ParseFile getUrlLogo() {
		return getParseFile("Image");
	}

	public void setUrlLogo(ParseFile urlLogo) {
		put("Image", urlLogo);
	}

	public ParseRelation<Produto> getProductos() {
		return getRelation("Products");
	}

	public void setProductos(Produto productos) {
		getProductos().add(productos);
		saveInBackground();
	}

}
