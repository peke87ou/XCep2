package com.irina.xcep.model;

import java.util.ArrayList;

import android.content.Context;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Products")
@SuppressWarnings("unchecked")
public class Produto extends ParseObject {

	Context mContext;
	
	public Produto(Context context, Produto producto) {
			super("Products");
			mContext = context;
	}
	
	public Produto() {
		super("Products");
	}

	/**
	 * Conxunto de getters e setters da clase
	 */
	
	public String getIdentificadorScan() {
		return getString("idBarCode");
	}

	public void setIdentificadorScan(String identificadorScan) {
		put("idBarCode", identificadorScan);
		saveInBackground();
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
		saveInBackground();
	}

	public String getDescripcion() {
		return getString("description");
	}

	public void setDescripcion(String descripcion) {
		put("description", descripcion);
		saveInBackground();
	}
	
	public ParseFile getIcon() {
		return getParseFile("icon");
	}

	public void setIcon(ParseFile icon) {
		put("icon", icon);
		saveInBackground();
	}

	public String getMarca() {
		return getString("mark");
	}

	public void setMarca(String marca) {
		put("mark", marca);
		saveInBackground();
	}
	
	
	public ArrayList<Tag> getATags() {
		return (ArrayList<Tag>)get("Atags");
	}
	
	public void addATags(String objectId) {
		ParseObject punteroTag = ParseObject.createWithoutData("Tag", objectId);
		add("Atags", punteroTag);
	}

	public ArrayList<Prezo> getAPrice() {
		return (ArrayList<Prezo>)get("APrice");
	}
	
	public void addAPrice(String objectId) {
		ParseObject punteroPrecio = ParseObject.createWithoutData("Price", objectId);
		add("APrice", punteroPrecio);
	}
}
