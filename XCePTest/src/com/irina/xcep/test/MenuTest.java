package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

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
		Timeout.setSmallTimeout(14559);
		
		// Click on HomeView XCeP 
		solo.clickOnActionBarHomeButton();
		// Click on As mi�as listas
		solo.clickInList(2, 1);
		solo.sleep(2000);
		Timeout.setSmallTimeout(14559);
		// Click on HomeView XCeP 
		solo.clickOnActionBarHomeButton();
		Timeout.setSmallTimeout(14559);
		// Click on Cat�logo
		solo.clickInList(3, 1);
		solo.sleep(2000);
		// Click on HomeView Cat�logo Xeral 
		solo.clickOnActionBarHomeButton();
		// Click on Escaner
		solo.clickInList(4, 2);
		// Click on HomeView XCeP 
		solo.clickOnActionBarHomeButton();
		// Click on Idioma
		solo.clickInList(9, 0);
		// Wait for dialog
		solo.waitForDialogToOpen(5000);
		// Click on Espa�ol
		solo.clickOnView(solo.getView(android.R.id.text1));
		// Wait for activity: 'com.irina.xcep.MenuActivity'
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		// Click on HomeView XCeP 
		solo.clickOnActionBarHomeButton();
		// Click on Idioma
		solo.clickInList(9, 1);
		// Wait for dialog
		solo.waitForDialogToOpen(5000);
		// Click on Galego
		solo.clickOnView(solo.getView(android.R.id.text1, 1));
		// Wait for activity: 'com.irina.xcep.MenuActivity'
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		// Click on HomeView XCeP 
		solo.clickOnActionBarHomeButton();
		// Click on Acerca de XCeP
		solo.clickInList(10, 1);
		// Wait for dialog
		solo.waitForDialogToOpen(5000);
		// Click on Pechar
		helper.logout();
	}
	
	/*
	 * 
	 * 
	 * testMenuOk:  
	 * Para esta proba comprobamos que o usuario 
	 * pode acceder a cada unha das opci�ns do men�, 
	 * facendo clic en cada unha delas, 
	 * � o test � correcto.
	 * 
	 * 
	 * */
	
	
	
}
