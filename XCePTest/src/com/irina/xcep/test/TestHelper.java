package com.irina.xcep.test;

import android.app.Fragment;
import android.util.Log;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.HomeFragment;
import com.irina.xcep.R;
import com.irina.xcep.test.enums.AddListEnum;
import com.irina.xcep.test.enums.LoginEnum;
import com.irina.xcep.test.enums.SignUpEnum;
import com.robotium.solo.Solo;

public class TestHelper {
	
	private Solo solo;
	private LoginEnum loginData;
	private SignUpEnum registerData;
	private AddListEnum addListData;
	
	public TestHelper(Solo s) {
		solo= s;
	}
	
	public void setLoginData(LoginEnum loginData){
		this.loginData = loginData;
	}

	public String getUser(){
		return this.loginData.getUser();
	}
	
	public String getPass(){
		return this.loginData.getPass();
	}
	
	public void setSignData(SignUpEnum SignData){
		this.registerData = SignData;
	}
	public String getUserSign(){
		return this.registerData.getUser();
	}
	
	public String getPassSing(){
		return this.registerData.getPass();
	}
	public String getRePassSign(){
		return this.registerData.getRepass();
	}
	
	public String getEmailSign(){
		return this.registerData.getMail();
	}
	public void setAddListData(AddListEnum addListData){
		this.addListData = addListData;
	}

	public String getNameList(){
		return this.addListData.getNameList();
	}
	public void logout() {
		
		Fragment home = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_home);
		
		if(solo.waitForActivity(HomeFragment.class.toString())){
			solo.assertCurrentActivity("Actividad incorrecta", HomeFragment.class);
			//Fragment home = solo.getCurrentActivity().getFragmentManager();
			
			final ButtonRectangle buttondesconectar =  (ButtonRectangle) home.getView().findViewById(R.id.logout);
			solo.clickOnView(buttondesconectar);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				Log.e(SignUpTest.class.getName(),"Interrupted Exception");
			}
		}
	}
}
