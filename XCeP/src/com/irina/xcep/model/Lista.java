package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("List")
public class Lista extends ParseObject {

	public Lista() {
		
	}

	/**
	 * Conxunto de getters e setters da clase
	 *
	 */
	
	public String getNome() {
		return getString("name");
	}

	public void setNome(String nome) {
		put("name", nome);
	}

	public ParseRelation<Supermercado> getIdSupermercado() {
		return getRelation("idMarket");
	}

	public void setIdSupermercado(Supermercado idSupermercado) {
		getIdSupermercado().add(idSupermercado);
		saveInBackground();
	}
	
	public ParseRelation<ParseUser> getIdUser() {
		return getRelation("idUser");
	}
	
	public void setIdUser( ParseUser idUser) {
		getIdUser().add(idUser);
		saveInBackground();
	}

	public ParseRelation<Produto> getIdProducts() {
		return getRelation("idProducts");
	}

	public void setIdProducts(Produto idProducts) {
		put("idProducts", idProducts);
	}

}
