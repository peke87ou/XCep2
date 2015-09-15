package com.irina.xcep;


import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class SplashActivity extends Activity implements OnClickListener {
	
	public static final String NAME_PREFERENCES = "Xcep_preferencias";
	public static final String NAME_LANGUAGE = "language";

	ButtonRectangle btnLogin;
	ButtonRectangle btnSignUp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Determine se o usuario actual non � un usuario an�nimo
		if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
		// Si el usuario actual no es usuario an�nimo
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
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(!isSetLanguage()){
			showLanguage();
		}
	}

	public boolean isSetLanguage(){
		
		SharedPreferences prefs = getSharedPreferences(NAME_PREFERENCES, MODE_PRIVATE); 
		String language = prefs.getString(NAME_LANGUAGE, null);
		
		if (language != null) {
			return true;
		}else{
			return false;
		}
	}


	public void showLanguage(){
		
		final String[] idiomas = {"Espa�ol", "Galego"};
		final String[] idiomasCodigo = {"es", "gl"};
		
		TextView titleDialogo = new TextView(this);
		titleDialogo.setText("Idioma");
		titleDialogo.setTextColor(getResources().getColor(R.color.verde_oscuro));
		titleDialogo.setTextSize(26);
		titleDialogo.setPadding(20, 10, 0, 10);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(titleDialogo).setItems(idiomas, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int nLenguaje) {

						Locale locale = new Locale(idiomasCodigo[nLenguaje]); 
			            Locale.setDefault(locale);
			            Configuration config = new Configuration();
			            config.locale = locale;
			            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
			            
			            SharedPreferences.Editor editor = getSharedPreferences(NAME_PREFERENCES, MODE_PRIVATE).edit();
			            editor.putString(NAME_LANGUAGE, idiomasCodigo[nLenguaje]);
			            editor.commit();
			            
			            Intent intent = getIntent();
			            finish();
			            startActivity(intent);
					}
				});

		AlertDialog dialogoIdioma = builder.create();
		dialogoIdioma.show();
	}
}
