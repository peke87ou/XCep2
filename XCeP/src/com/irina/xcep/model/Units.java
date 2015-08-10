package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UnitsProduct")
public class Units extends ParseObject {
	public Number getNumberProduct() {
		return getNumber("numberProduct");
	}

	public void setPrice(Number numberProduct) {
		put("numberProduct", numberProduct);
	}
}
