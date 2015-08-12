package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UnitsProduct")
public class Units extends ParseObject {
	
	public Number getNumberUnits() {
		return getNumber("numberUnits");
	}

	public void setNumberUnits(Number numberUnits) {
		put("numberUnits", numberUnits);
		saveInBackground();
	}
	
	public Produto getProduct(){
		return (Produto)get("PidProduct");
	}
}
