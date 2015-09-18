package com.irina.xcep.test.enums;

public enum SignUpEnum {
	
	
	
		CORRECTO(
						System.currentTimeMillis()+"",
						"proba",
						"proba",
						System.currentTimeMillis()+"@gmail.com"
					),
		INCORRECTO_EXISTE_USER	(
						"irina",
						"irina",
						"irina",
						"irina@gmail.com"
					),
		INCORRECTO_PASS	(
						"xcep",
						"pass",
						"passmal",
						"xcep@gmail.com"
					),
		
		INCORRECTO_MAIL_EXISTE	(
					"xcepProba",
					"proba",
					"proba",
					"xcep@gmail.com"
					),
	
		testSignupEmptyName(
				"",
				"irina",
				"irina",
				"irina@gmail.com"
					),
		testSignupEmptyPass1(
				"irina",
				"",
				"irina",
				"irina@gmail.com"
					),
		testSignupEmptyPass2(
				"irina",
				"irina",
				"",
				"irina@gmail.com"
					),
		testSignupMailOk(
				System.currentTimeMillis()+"",
				"proba",
				"proba",
				System.currentTimeMillis()+"@gmail.com"
					),
		INCORRECTO_MAIL	(
				"probaMailIncorrecta",
				"proba",
				"proba",
				"k@l"
		),
		testSignupEmptyMail(
				"irina",
				"irina",
				"irina",
				""
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
