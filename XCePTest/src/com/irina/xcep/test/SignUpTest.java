package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.R;
import com.irina.xcep.SignupActivity;
import com.irina.xcep.test.enums.SignUpEnum;
import com.robotium.solo.Solo;

public class SignUpTest extends ActivityInstrumentationTestCase2<SignupActivity> {
	
	private Solo solo;
	private SignupActivity singup;
	private TestHelper helper;
	private EditText username,password,repassword, email;
	private ButtonRectangle buttonsign;

	static int TIME_OUT_SINGUP = 30000;

	public SignUpTest(Class<SignupActivity> activityClass) {
		super(activityClass);
	}

	public SignUpTest() {
		this(SignupActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		singup = (SignupActivity) solo.getCurrentActivity();
		helper = new TestHelper(solo);
		
		
		username  = (EditText) singup.findViewById(R.id.signup_username_input);
		password  = (EditText) singup.findViewById(R.id.signup_password_input);
		repassword  = (EditText) singup.findViewById(R.id.signup_confirm_password_input);
		email  = (EditText) singup.findViewById(R.id.signup_email_input);
		buttonsign = (ButtonRectangle) singup.findViewById(R.id.create_account);
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	
	public void testSingupOk() {
		
		helper.setSignData(SignUpEnum.CORRECTO);
		
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		//Utilizado para esperar por el retardo de las vistas en mostrar el error al estar vacias
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			Log.e(SignUpTest.class.getName(),"Interrupted Exception");
		}
//		solo.assertCurrentActivity("Actividad incorrecta", HomeActivity.class);
		helper.logout();

	}
	
	
	public void testSingupFailedUser() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_EXISTE_USER);

		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		solo.searchText("O usuario que intenta rexistrar xa existe, Loguese!");
	}
	
	public void testSingupFailedMailFormat() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_MAIL);
	
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		solo.searchText("O seu enderezo electrónico é inválido");
	}
	
	public void testSingupFailedMailExist() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_MAIL_EXISTE);
	
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		solo.searchText("O email que intenta rexistrar xa existe");
	}
	
	public void testSingupFailedNotEqualPass() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_PASS);
		
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		solo.searchText("As contrasinais non coinciden");
	}
	
	public void testSingupFailedEmpty() {
		
		helper.setSignData(SignUpEnum.INCORRECTO_EMPTY);
		
		solo.enterText(username, helper.getUserSign());
		solo.enterText(password, helper.getPassSing());
		solo.enterText(repassword, helper.getRePassSign());
		solo.enterText(email, helper.getEmailSign());
		solo.clickOnView(buttonsign);
		
		//Utilizado para esperar por el retardo de las vistas en mostrar el error al estar vacias
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			Log.e(SignUpTest.class.getName(),"Interrupted Exception");
		}
		
		assertEquals(true, !TextUtils.isEmpty(username.getError().toString()) && username.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		assertEquals(true, !TextUtils.isEmpty(password.getError().toString()) && password.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		assertEquals(true, !TextUtils.isEmpty(repassword.getError().toString()) && repassword.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		assertEquals(true, !TextUtils.isEmpty(email.getError().toString()) && email.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		
	}
}
