package com.irina.xcep.test.enums;

public enum AddListEnum {

	
	CORRECTO(
					"Compra"
				),
	INCORRECTO_NOT_NAME	(
					""
				),
	
	INCORRECTO_EMPTY	(
					""
	);
	
	private String namelist;
	
	
	AddListEnum(String namelist){
		this.namelist=namelist;
		
	}
	public String getNameList(){
		return namelist;
	}
	


	
}
