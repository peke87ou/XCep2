package com.irina.xcep.test;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * 1.	testChangeUnitsProductListOk:  Para esta proba comprobamos que o cambiar as unidades mediante o selector num�rico,
 * perm�tenos cambiar as unidades dun produto concreto dunha lista e o test � correcto.
 * */

public class ModifyProductListTest extends ActivityInstrumentationTestCase2<SplashActivity> {

	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;
	
	public ModifyProductListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public ModifyProductListTest() {
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
	
	public void testChangeUnitsProductListOk(){
	
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		solo.clickLongInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnText("Cambiar unidades");
		
		solo.waitForDialogToOpen();
		solo.sleep(1000);
		solo.clickOnText("Aceptar");
		
		solo.waitForDialogToClose();
		solo.waitForDialogToClose();
		solo.sleep(4000);
	}
}
