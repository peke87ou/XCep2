package com.irina.xcep.model;

import com.irina.xcep.R;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Price")
public class Prezo extends ParseObject {

	public Prezo() {
		super("Price");
	}

	public Prezo(String theClassName) {
		super("Price");
	}

	public Number getPrice() {
		return getNumber("price");
	}

	public void setPrice(Number price) {
		put("price", price);
	}
	
	public Supermercado getPidMarket() {
		return (Supermercado)get("PidMarket");
	}

	public void setPidMarket(ParseObject supermercado) {
		put("PidMarket", supermercado);
		saveInBackground();
	}
	
}
