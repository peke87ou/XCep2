package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UnitsProduct")
public class Units extends ParseObject {
	
	
	public Units() {
		super("UnitsProduct");
	}

	public Units(String theClassName) {
		super("UnitsProduct");
		
	}

	public Number getNumberUnits() {
		return getNumber("numberUnits");
	}

	public void setNumberUnits(Number numberUnits) {
		put("numberUnits", numberUnits);
	}
	
	public Produto getProduct(){
		return (Produto)get("PidProduct");
	}
	
	/**
	 * Incrementa el n�mero de unidades del producto de la unidad
	 * @param n N�mero de unidades a incrementar
	 */
	
	public void addNumberUnits(int n){
		increment("numberUnits", n);
	}
	
}
