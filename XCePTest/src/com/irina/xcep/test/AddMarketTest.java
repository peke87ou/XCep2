package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

public class AddMarketTest extends ActivityInstrumentationTestCase2<SplashActivity> {

	
	/*
	 * 1.	tesAddMarketOk:  Para esta proba comprobamos que o engadir un nome e seleccionar unha imaxe para o supermercado, permítenos engadir un novo supermercado na aplicación, para elo introducimos como nome do supermercado ”Froiz” e seleccionamos unha imaxe na galería, permítenos engadir un novo supermercado correctamente na aplicación.
	 * 2.	testAddMarketNotImage: Se non seleccionamos ningunha imaxe para o  supermercado, para esta proba o sistema indícanos un erro de que debemos seleccionar unha imaxe para engadir o supermercado.
	 * 
	 * 
	 * testAddMarketNotName
	 * tesImageGaleryMarket
	 * tesImageCamareMarket
	 * testAddMarketNotImage
	 * 
	 * */
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public AddMarketTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}

	public AddMarketTest() {
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
	
	
	public void testAddMarketOk(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		Timeout.setSmallTimeout(15669);
		
		// Scroll to Novo
		//android.widget.ListView listView0 = (android.widget.ListView) solo.getView(android.widget.ListView.class, 0);
		android.widget.GridView listView0 = (android.widget.GridView) solo.getView(com.irina.xcep.R.id.grid_logo_market);
		//solo.scrollDownList(listView0);
		solo.sleep(2000);
		solo.scrollListToLine(listView0, 3);
//		// Click on Novo
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_market_add_list, 3));
		solo.sleep(2000);
		// Wait for activity: 'com.irina.xcep.AddMarketActivity'
		assertTrue("com.irina.xcep.AddMarketActivity is not found!", solo.waitForActivity(com.irina.xcep.AddMarketActivity.class));
		solo.sleep(2000);
//		// Enter the text: 'lidl'
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market), "lidl");
//		// Click on Engadir Imaxe
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.button_camera));
//
//		helper.logout();
	}
	

//	public void testAddMarketNotName(){
//		
//		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
//		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
//		
//		Timeout.setSmallTimeout(14559);
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
//		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
//
//		Timeout.setSmallTimeout(15669);
//		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list));
//		Timeout.setSmallTimeout(15669);
//		solo.clickInList(1, 0);
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.crear_new_list));
//		solo.searchText("É necesario encher o campo seleccionado");
//		
//		helper.logout();
//	}
	
//	public void testAddMarketNotImage(){
//
//		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
//		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
//		
//		Timeout.setSmallTimeout(14559);
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
//		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
//		
//		Timeout.setSmallTimeout(15669);
//		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list));
//		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list), "lista ejemplo");
//		Timeout.setSmallTimeout(15669);
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.crear_new_list));
//		solo.searchText("Non seleccionou ningún supermercado");
//		
//		helper.logout();
//	}
//	
//	
}
