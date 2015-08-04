package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.LoginActivity;
import com.irina.xcep.R;
import com.irina.xcep.test.enums.LoginEnum;
import com.irina.xcep.test.enums.SignUpEnum;
import com.robotium.solo.Solo;

public class AddListTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	
	private Solo solo;
	private LoginActivity login;
	private TestHelper helper;
	private EditText username,password;
	private ButtonRectangle buttonlogin;
	
	static int TIME_OUT_LOGIN = 30000;

	public AddListTest(Class<LoginActivity> activityClass) {
		super(activityClass);
	}

	public AddListTest() {
		this(LoginActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		login = (LoginActivity) solo.getCurrentActivity();
		helper = new TestHelper(solo);
		
		username  = (EditText) login.findViewById(R.id.username);
		password  = (EditText) login.findViewById(R.id.password);
		buttonlogin = (ButtonRectangle) login.findViewById(R.id.login);
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	
	public void testAddListOk() {
		
		helper.setLoginData(LoginEnum.CORRECTO);
		
		solo.enterText(username, helper.getUser());
		solo.enterText(password, helper.getPass());
		solo.clickOnView(buttonlogin);
		
		helper.logout();

	}
	
	
	public void testAddListNotName() {
		
		helper.setLoginData(LoginEnum.INCORRECTO);
		
		solo.enterText(username, helper.getUser());
		solo.enterText(password, helper.getPass());
		solo.clickOnView(buttonlogin);
		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
	}
	public void testAddListNotMarket() {
		
		helper.setLoginData(LoginEnum.INCORRECTO);
		
		solo.enterText(username, helper.getUser());
		solo.enterText(password, helper.getPass());
		solo.clickOnView(buttonlogin);
		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
	}
	
	public void testAddListFailedEmpty() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_EMPTY);
		
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.clickOnView(buttonlogin);
		
		//Utilizado para esperar por el retardo de las vistas en mostrar el error al estar vacias
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			Log.e(SignUpTest.class.getName(),"Interrupted Exception");
		}
		
		assertEquals(true, !TextUtils.isEmpty(username.getError().toString()) && username.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		assertEquals(true, !TextUtils.isEmpty(password.getError().toString()) && password.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		
	}
}
