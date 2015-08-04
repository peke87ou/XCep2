package com.irina.xcep.test.enums;

public enum SignUpEnum {
	
	
	
		CORRECTO(
						System.currentTimeMillis()+"",
						"prueba",
						"prueba",
						System.currentTimeMillis()+"@gmail.com"
					),
		INCORRECTO_EXISTE_USER	(
						"irina",
						"irina",
						"irina",
						"irina@gmail.com"
					),
		INCORRECTO_MAIL	(
						"pruebaMailIn",
						"prueba",
						"prueba",
						"k@l.c"
			),
		INCORRECTO_MAIL_EXISTE	(
					"pruebaMail",
					"prueba",
					"prueba",
					"prueba@gmail.com"
		),
		INCORRECTO_PASS	(
						"pruebaPass",
						"pass",
						"passmal",
						"prueba@gmail.com"
				),
		INCORRECTO_EMPTY	(
						"",
						"",
						"",
						""
		);
		
		private String user;
		private String pass;
		private String repass;
		private String mail;
		
		SignUpEnum(String user,String pass, String repass, String mail){
			this.user=user;
			this.pass=pass;
			this.repass=repass;
			this.mail=mail;
		}
		
		
		public String getUser(){
			return user;
		}
		
		public String getPass(){
			return pass;
		}


		public String getRepass() {
			return repass;
		}

		public String getMail() {
			return mail;
		}


}
