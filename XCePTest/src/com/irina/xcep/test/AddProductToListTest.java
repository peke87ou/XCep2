package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

/**
 * 1.	testAddProductListCatalogOk:  Para esta proba comprobamos que o usuario pode engadir produto a lista dende o catalogo, facendo clic na icona  , é o test é correcto.
 * 2.	testAddProductListDetailOk:  Para esta proba comprobamos que o usuario pode engadir un produto a lista dende o detalle de produto, facendo clic no botón “Engadir”, é o test é correcto.
 */

public class AddProductToListTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;
	
	public AddProductToListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public AddProductToListTest() {
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
	
	public void testAddProductListCatalogOk(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(1);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_add_product));
		solo.sleep(2000);
		// Click on Agua 0.28  
		//solo.clickInList(1, 1);
	}
	
	public void testAddProductListDetailOk(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(1);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		
		solo.clickInList(2, 1);
		solo.sleep(2000);
		
		assertTrue("com.irina.xcep.DetailProductActivity is not found!", solo.waitForActivity(com.irina.xcep.DetailProductActivity.class));
		solo.sleep(2000);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_product_list));
		
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		solo.sleep(1000);
	}
	
}
