package com.irina.xcep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.utils.Utils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity {
	//Declaramos as variables
	ButtonRectangle signup;
	EditText username;
	EditText password;
	EditText repassword;
	EditText email;
	
	String usernametxt;
	String passwordtxt;
	String repasswordtxt;
	String emailtxt;
	TextView linklogin;
	
	/** Chamase cando creouse por primera vez a actividad */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		getActionBar().setTitle(R.string.title_action_bar_signup);
		
		username = (EditText) findViewById(R.id.signup_username_input);
		password = (EditText) findViewById(R.id.signup_password_input);
		repassword = (EditText) findViewById(R.id.signup_confirm_password_input);
		email = (EditText) findViewById(R.id.signup_email_input);
		signup = (ButtonRectangle) findViewById(R.id.create_account);
		linklogin = (TextView) findViewById(R.id.link_login);
		
		signup.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				boolean allfilled = true;
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();
				repasswordtxt = repassword.getText().toString();
				emailtxt = email.getText().toString();
				
				allfilled =  Utils.isNotEmpty(username, usernametxt)&& allfilled;
				allfilled =  Utils.isNotEmpty(password, passwordtxt) && allfilled;
				allfilled =  Utils.isNotEmpty(repassword, repasswordtxt) && allfilled;
				allfilled =  Utils.isNotEmpty(email,emailtxt) && allfilled;
				if(!allfilled) return;
				
				if (!passwordtxt.equals(repasswordtxt)){
					Toast.makeText(getApplicationContext(),	R.string.pass_no_ok,	Toast.LENGTH_LONG).show();
				}else {
					// Gardar novos datos do usuario en Parse.com Almacenamento de Datos
					final ParseUser user = new ParseUser();
					user.setUsername(usernametxt);
					user.setPassword(passwordtxt);
					user.setEmail(emailtxt);
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Mostrar unha mensaxe sinxela no momento do rexistro exitoso
								Intent intent = new Intent(SignupActivity.this,	MenuActivity.class);
								startActivity(intent);
								Toast.makeText(getApplicationContext(),	R.string.conect_ko_signup,	Toast.LENGTH_LONG).show();
								finish();
							} else {
								String mensaje = "";
								switch (e.getCode()){
								case 125:
									mensaje = "O seu enderezo electrónico é inválido";
									break;
								case 202:
									mensaje = "O usuario que intenta rexistrar xa existe, Loguese!";
									break;
								case 203:
									mensaje = "O email que intenta rexistrar xa existe";
									break;
								default:
									mensaje = "Erro no rexistro";
									break;
								}
								Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
								ParseUser.logOut();
							}
						}
					});
				}
			}
		});
		
		linklogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Ir a páxina de rexistro
				Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
}
