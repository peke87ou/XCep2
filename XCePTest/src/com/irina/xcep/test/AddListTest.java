package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

public class AddListTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public AddListTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}

	public AddListTest() {
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
	
	
	public void testAddListOk(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		Timeout.setSmallTimeout(14559);
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		
		Timeout.setSmallTimeout(15669);
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list), "Lista de proba");
		Timeout.setSmallTimeout(15669);
		solo.clickInList(1, 0);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.crear_new_list));
		
		helper.logout();
	}
	
	public void testAddListNotMarket(){

		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		
		Timeout.setSmallTimeout(14559);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));
		
		Timeout.setSmallTimeout(15669);
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list));
		solo.enterText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list), "lista ejemplo");
		Timeout.setSmallTimeout(15669);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.crear_new_list));
		solo.searchText("Non seleccionou ningún supermercado");
		
		helper.logout();
	}
	
	
	public void testAddListNotName(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		
		Timeout.setSmallTimeout(14559);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_list));
		assertTrue("com.irina.xcep.AddShoppingListActivity is not found!", solo.waitForActivity(com.irina.xcep.AddShoppingListActivity.class));

		Timeout.setSmallTimeout(15669);
		solo.clearEditText((android.widget.EditText) solo.getView(com.irina.xcep.R.id.text_name_list));
		Timeout.setSmallTimeout(15669);
		solo.clickInList(1, 0);
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.crear_new_list));
		solo.searchText("É necesario encher o campo seleccionado");
		
		helper.logout();
	}

}
