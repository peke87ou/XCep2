package com.irina.xcep;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class SplashActivity extends Activity implements OnClickListener {

	ButtonRectangle btnLogin;
	ButtonRectangle btnSignUp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Determine se o usuario actual non é un usuario anónimo
		if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
		// Si el usuario actual no es usuario anónimo
		// Obter datos de usuario actuais de Parse.com
		ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null && currentUser.isAuthenticated()) {
			// Enviar os usuarios logueado a HomeActivity.class
				Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		} 
		btnLogin = (ButtonRectangle) findViewById(R.id.btnLogin);
        btnSignUp = (ButtonRectangle) findViewById(R.id.btnSignUp);
        
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

	public void onClick(View v) {
		Intent i = null;
		switch(v.getId()){
			case R.id.btnLogin:
				i = new Intent(this,LoginActivity.class);
				break;
			case R.id.btnSignUp:
				i = new Intent(this,SignupActivity.class);
				break;
		}
		startActivity(i);
	}
}
