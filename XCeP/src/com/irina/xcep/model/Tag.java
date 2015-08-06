package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Tag")
public class Tag extends ParseObject {

	public String getNome() {
		return getString("name");
	}

	public void setNome(String nome) {
		put("name", nome);
	}
	
}
