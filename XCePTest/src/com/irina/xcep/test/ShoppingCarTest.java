package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

/**
 * 1. 	testCkeckProductListOk: Para esta proba comprobamos que o usuario pode seleccionar 
 * o cadro do check do produto da lista, facendo clic nun produto deseleccionado, 
 * é o test é correcto.
 *  
 * 2.	testNotCkeckProductListOk: Para esta proba comprobamos que o usuario pode
 * desseleccionar o cadro do check do produto da lista, facendo clic nun
 * produto seleccionado, é o test é correcto.
 */

public class ShoppingCarTest extends ActivityInstrumentationTestCase2<SplashActivity>{
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;
	
	public ShoppingCarTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public ShoppingCarTest() {
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
	
	public void testCkeckProductListOk(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();

		solo.clickInList(0);
		solo.sleep(2000);
		
		// Click on Empty Text View
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.checkBoxProdutoCarrito));
		solo.sleep(2000);
	}
	
	public void testNotCkeckProductListOk(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();

		solo.clickInList(0);
		solo.sleep(2000);
		
		// Click on Empty Text View
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.checkBoxProdutoCarrito));
		solo.sleep(2000);
		// Click on Empty Text View
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.checkBoxProdutoCarrito));
		solo.sleep(2000);
	}
	
}
