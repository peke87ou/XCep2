package com.irina.xcep.model;

import java.util.ArrayList;
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

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
		saveInBackground();
	}

	public Supermercado getSupermercado() {
		return (Supermercado)get("PidMarket");
	}

	public void setSupermercado(ParseObject supermercado) {
		put("PidMarket", supermercado);
		saveInBackground();
	}
	
	public ParseRelation<ParseUser> getIdUser() {
		return getRelation("idUser");
	}
	
	public void setIdUser( ParseUser idUser) {
		getIdUser().add(idUser);
		saveInBackground();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Units> getAIdUnits() {
		return (ArrayList<Units>)get("AidUnits");
	}

	public void addAidUnits(Units unitProduct) {
		//TODO generar código
	}

	public void deleteAidUnits(Units unitProduct){
		//TODO generar código
	}
	
}
