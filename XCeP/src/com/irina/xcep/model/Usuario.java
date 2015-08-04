package com.irina.xcep.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class Usuario extends ParseObject {

	public Usuario() {
		
	}
	public String getObjectId() {
		return getString("objectId");
	}

	public void setObjectId(String objectId) {
		put("objectId", objectId);
	}
	public String getUserName() {
		return getString("username");
	}

	public void setUserName(String username) {
		put("username", username);
	}
	public String getEmail() {
		return getString("email");
	}

	public void setEmail(String email) {
		put("email", email);
	}

	public String getContrasinal() {
		return getString("password");
	}

	public void setContrasinal(String password) {
		put("password", password);
	}

	public boolean isAdmin() {
		return getBoolean("eAdmin");
	}

	public void setAdmin(boolean eAdmin) {
		put("eAdmin", eAdmin);
	}
	
}
