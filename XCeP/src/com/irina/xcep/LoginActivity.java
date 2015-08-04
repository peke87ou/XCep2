package com.irina.xcep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.utils.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	//Declaramos as variables
	ButtonRectangle loginbutton;
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;
	TextView linksingup;
	
	/** Chamase cando creouse por primera vez a actividad */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setTitle(R.string.title_action_bar_login);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		loginbutton = (ButtonRectangle) findViewById(R.id.login);
		linksingup = (TextView) findViewById(R.id.link_signup);

		loginbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
					
				boolean allfilled = true;
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();
					
				allfilled =  Utils.isNotEmpty(username, usernametxt);
				allfilled =  Utils.isNotEmpty(password, passwordtxt) && allfilled;
				if(!allfilled) return;
	
				ParseUser.logInInBackground(usernametxt, passwordtxt,
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
									startActivity(intent);
									Toast.makeText(getApplicationContext(),	R.string.conect_Ok, Toast.LENGTH_LONG).show();
									finish();
								} else {
									Log.i("Error Login", e.getMessage().intern() +"   l   "   + e.getLocalizedMessage()+ "  code   "+ e.getCode()+"cause " + e.getCause()+""+ e.hashCode());
									Toast.makeText(getApplicationContext(),	R.string.conect_ko_login, Toast.LENGTH_LONG).show();
								}
							}
						});
				}
		});
		
		linksingup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Ir a páxina de rexistro
				Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
				startActivity(intent);
				
			}
		});
	}
}
