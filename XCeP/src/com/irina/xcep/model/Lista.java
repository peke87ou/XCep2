package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("List")
public class Lista extends ParseObject {


//	//FIXME CopyOnWriteArrayList
//	List<Producto> productos;
	
	public Lista() {
		
	}

	//Métodos empregados nesta clase
	/**
	 * Engade un producto a lista
	 * @param producto: Producto que se engade a lista
	 */
	public void engadirProducto(Produto producto){
		//TODO
		
	}

	/**
	 * Quita un producto da lista
	 * @param producto: Producto que se quita da lista
	 */
	public void quitarProducto(Produto producto){
		//TODO
		
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
		//put("idMarket", idSupermercado);
	}
	
	
	public ParseRelation<ParseUser> getIdUser() {
		return getRelation("idUser");
	}

	public ParseRelation<Usuario> getUserApp() {
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
