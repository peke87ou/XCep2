package com.irina.xcep.test.enums;

public enum LoginEnum {

		CORRECTO		(
						"irina",
						"irina"
					),
		INCORRECTO	(
						"NonUsuario",
						"passmal"
					),
		INCORRECTO_EMPTY	(
				"",
				""
		);
		
		private String user;
		private String pass;
		
		LoginEnum(String user,String pass){
			this.user=user;
			this.pass=pass;
		}
		
		
		public String getUser(){
			return user;
		}
		
		public String getPass(){
			return pass;
		}
	
}
