package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

public class AddMarketTest extends ActivityInstrumentationTestCase2<SplashActivity> {

	
	/*
	 * 
	 * testAddMarketNotName
	 * testImageGaleryMarket
	 * testImageCamareMarket
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
	
	
	public void testAddMarketNotImage(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		Timeout.setSmallTimeout(15669);
		
		// Scroll to Novo
		android.widget.GridView listView0 = (android.widget.GridView) solo.getView(com.irina.xcep.R.id.grid_logo_market);
		solo.sleep(2000);
		solo.scrollListToLine(listView0, 3);
		// Click on Novo
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_market_add_list, 3));
		solo.sleep(2000);
		// Wait for activity: 'com.irina.xcep.AddMarketActivity'
		assertTrue("com.irina.xcep.AddMarketActivity is not found!", solo.waitForActivity(com.irina.xcep.AddMarketActivity.class));
		solo.sleep(2000);
		// Enter the text: 'lidl'
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market), "lidl");
		// Click on Engadir Imaxe
//		solo.clickOnView(solo.getView(com.irina.xcep.R.id.button_camera));
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_new_market));
		solo.searchText("Non se obtivo a fotografía");
		
		
	}
	

	public void testAddMarketNotName(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		Timeout.setSmallTimeout(15669);
		
		// Scroll to Novo
		android.widget.GridView listView0 = (android.widget.GridView) solo.getView(com.irina.xcep.R.id.grid_logo_market);
		solo.sleep(2000);
		solo.scrollListToLine(listView0, 3);
		// Click on Novo
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_market_add_list, 3));
		solo.sleep(2000);
		// Wait for activity: 'com.irina.xcep.AddMarketActivity'
		assertTrue("com.irina.xcep.AddMarketActivity is not found!", solo.waitForActivity(com.irina.xcep.AddMarketActivity.class));
		solo.sleep(2000);
		
		// Click on Engadir Imaxe
		//FIXME añadir una imagen cualquiera para que salga el error de name super
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_new_market));
		solo.searchText("É necesario encher o campo seleccionado");
		
		
	}
	
	public void testImageGaleryMarket(){

		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		Timeout.setSmallTimeout(15669);
		
		// Scroll to Novo
		android.widget.GridView listView0 = (android.widget.GridView) solo.getView(com.irina.xcep.R.id.grid_logo_market);
		solo.sleep(2000);
		solo.scrollListToLine(listView0, 3);
		// Click on Novo
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_market_add_list, 3));
		solo.sleep(2000);
		// Wait for activity: 'com.irina.xcep.AddMarketActivity'
		assertTrue("com.irina.xcep.AddMarketActivity is not found!", solo.waitForActivity(com.irina.xcep.AddMarketActivity.class));
		solo.sleep(2000);
		// Enter the text: 'lidl'
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market), "lidl");
		// Click on Engadir Imaxe
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.button_camera));
				
		
	}
	
	public void testImageCamareMarket(){

		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		Timeout.setSmallTimeout(15669);
		
		// Scroll to Novo
		android.widget.GridView listView0 = (android.widget.GridView) solo.getView(com.irina.xcep.R.id.grid_logo_market);
		solo.sleep(2000);
		solo.scrollListToLine(listView0, 3);
		// Click on Novo
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.image_market_add_list, 3));
		solo.sleep(2000);
		// Wait for activity: 'com.irina.xcep.AddMarketActivity'
		assertTrue("com.irina.xcep.AddMarketActivity is not found!", solo.waitForActivity(com.irina.xcep.AddMarketActivity.class));
		solo.sleep(2000);
		// Enter the text: 'lidl'
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_market), "lidl");
		// Click on Engadir Imaxe
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.button_camera));
				
		
	}
	
	
}
