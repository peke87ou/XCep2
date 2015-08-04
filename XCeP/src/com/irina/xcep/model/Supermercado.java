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
		put("Products", productos);
	}
	
	//TODO agregar el resto de metodos
	
	/**
	 * @param tags Lista de tags
	 * @return Devuelve una lista de producto que tienen este tag
	 */
	public ParseRelation<Tag> getProductosPorTag(Tag tags){
		
		return null;
	}
	
}
