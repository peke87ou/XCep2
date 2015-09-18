package com.irina.xcep.test.enums;

public enum AddListEnum {

	
	tesAddListOk(
					"Compra"
				),
	testAddListNotMarket(
					"Compra"
				),
	
	testAddListNotName(
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
