package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;


/**
 * 1.	testChangeNameListOk:  Para esta proba comprobamos que o engadir un nome “nova lista”, permítenos cambiar renomear a lista e o test é correcto.
 * testChangeNotNameList
 * 
 * */

public class ModifyListTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public ModifyListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public ModifyListTest() {
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
	
	public void testChangeNameListOk(){
	
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickLongInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnText("Cambiar Nombre");
		
		solo.waitForDialogToOpen();
		solo.sleep(1000);
		solo.clearEditText(0);
		String milisec = String.valueOf(System.currentTimeMillis()); //número aleatorio
		solo.enterText(0, "Novo nome lista "+String.valueOf(milisec).substring(milisec.length() - 3));
		solo.sleep(2000);
		solo.clickOnText("Aceptar");
		
		solo.waitForDialogToClose();
		solo.waitForDialogToClose();
		solo.sleep(4000);
	}

}
