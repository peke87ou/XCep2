package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;


/**
 * 1.	testDeleteListOk:  Para esta proba comprobamos que o usuario pode acceder a eliminación da lista que premeu, facendo clic na opción “Eliminar a lista”, é o test é correcto. 
 */

public class DeleteListTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public DeleteListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public DeleteListTest() {
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
	
	public void testDeleteListOk(){
	
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickLongInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnText("Eliminar la lista");
		
		solo.waitForDialogToClose();
		solo.waitForDialogToClose();
		solo.sleep(4000);
	}
	 
}
