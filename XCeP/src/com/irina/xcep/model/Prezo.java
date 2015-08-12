package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Price")
public class Prezo extends ParseObject {

	
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
