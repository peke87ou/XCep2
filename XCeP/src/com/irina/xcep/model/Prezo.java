package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Price")
public class Prezo extends ParseObject {

	public Number getPrice() {
		return getNumber("price");
	}

	public void setPrice(Number price) {
		put("price", price);
	}
	public ParseRelation<Units> getUnits() {
		return getRelation("idUnits");
	}
	public void setUnits(Units idUnits) {
		getUnits().add(idUnits);
		saveInBackground();
	}
}
