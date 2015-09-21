package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

public class DeleteProductListTest extends ActivityInstrumentationTestCase2<SplashActivity>{

	/**
	 * 1.	testDeleteProductListOk:  Para esta proba comprobamos que o usuario pode acceder a 
	 * eliminación do produto da lista que seleccionou, facendo clic na opción “Eliminar o Produto”, é o test é correcto.*/
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;
	
	public DeleteProductListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public DeleteProductListTest() {
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
	
	public void testDeleteProductListOk(){
	
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		solo.clickLongInList(0);
		solo.waitForDialogToOpen();
		solo.clickOnText("Eliminar el Producto");
			
		solo.waitForDialogToClose();
		solo.waitForDialogToClose();
		solo.sleep(4000);
	}
}
