package com.irina.xcep.model;

import android.content.Context;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Products")
public class Produto extends ParseObject {

	Context mContext;
	
	public Produto(Context context, Produto producto) {
			super();
			mContext = context;
	}
	
	public Produto() {
		
	}

	/**
	 * Conxunto de getters e setters da clase
	 */
	
	public String getIdentificadorScan() {
		return getString("idBarCode");
	}

	public void setIdentificadorScan(String identificadorScan) {
		put("idBarCode", identificadorScan);
	}

	public String getNome() {
		return getString("title");
	}

	public void setNome(String nome) {
		put("title", nome);
		saveInBackground();
	}

	public String getDescripcion() {
		return getString("description");
	}

	public void setDescripcion(String descripcion) {
		put("description", descripcion);
	}
	
	public ParseFile getIcon() {
		return getParseFile("icon");
	}

	public void setIcon(ParseFile icon) {
		put("icon", icon);
	}

	public String getMarca() {
		return getString("mark");
	}

	public void setMarca(String marca) {
		put("mark", marca);
	}

	public ParseRelation<Prezo> getPrezoPorSupermercado() {
		return getRelation("PriceMarket");
	}

	public void setPrezoPorSupermercado(Prezo prezoPorSupermercado) {
		getPrezoPorSupermercado().add(prezoPorSupermercado);
		saveInBackground();
	}

	public ParseRelation<Tag> getTags() {
		return getRelation("tags");
	}

	public void setTags(Tag tags) {
		getTags().add(tags);
		saveInBackground();
	}

	
}
