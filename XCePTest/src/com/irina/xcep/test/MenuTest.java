package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

/**
 * 
 * 
 * testMenuOk:  Para esta proba comprobamos que o usuario pode acceder a cada unha das opcións do menú, 
 * facendo clic en cada unha delas, é o test é correcto.
 * 
 * */

public class MenuTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public MenuTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public MenuTest() {
		this(SplashActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
		
		helper = new TestHelper(solo);
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	public void testMenu(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		
		
		//As miñas listas
		solo.waitForDialogToClose();
		solo.clickOnActionBarHomeButton();
		solo.clickInList(2, 1);

		//Escáner
		solo.sleep(3000);
		solo.clickOnActionBarHomeButton();
		solo.clickOnText("Escaner");
		solo.sleep(3000);
		
		//Catálogo
		solo.clickOnActionBarHomeButton();
		solo.clickInList(3, 1);
		solo.sleep(2000);
		
		
		//Language 
		solo.waitForDialogToClose();
		solo.clickOnActionBarHomeButton();
		solo.clickOnText("Idioma");
		solo.waitForDialogToOpen(5000);
		// Click on Español
		solo.clickOnView(solo.getView(android.R.id.text1));
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		solo.clickOnActionBarHomeButton();
		solo.clickOnText("Idioma");
		solo.waitForDialogToOpen(5000);
		// Click on Galego
		solo.clickOnView(solo.getView(android.R.id.text1, 1));
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		
		//Acerca de...
		solo.waitForDialogToClose();
		solo.sleep(5000);
		solo.clickOnActionBarHomeButton();
		solo.clickOnText("Acerca de XCeP");
		//solo.clickOnActionBarItem(com.irina.xcep.MenuActivity.HELP);
		solo.waitForDialogToOpen();
		solo.clickOnButton("Pechar");
		solo.waitForDialogToClose();
		
		//Cerrar Robotium
		solo.sleep(1000);
	}	
	
}
