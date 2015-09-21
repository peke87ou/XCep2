package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

/**
 * 1.	testSearchWordsOk:  Para esta proba comprobamos que o buscar por unha palabra, devólvenos ningún , un o varios resultados dos produtos coincidente coa búsquea, para elo introducimos como palabra ”Fanta”, devolvéndonos resultados positivos da búsquea.
 * 2.	testSearchTagsOk:  Para esta proba comprobamos que o buscar por tag, devólvenos ningún , un o varios resultados dos produtos coincidente coa búsquea, para elo introducimos como tag ”Bebidas”, devolvéndonos resultados positivos da búsquea.
 */

public class CatalogTest extends ActivityInstrumentationTestCase2<SplashActivity>{

	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;
	
	public CatalogTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public CatalogTest() {
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
	public void testSearchWordsOk(){
		
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(1);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		solo.clickOnView(solo.getView("search_src_text"));
		// Enter the text: 'agua'
		solo.clearEditText((android.widget.EditText) solo.getView("search_src_text"));
		solo.enterText((android.widget.EditText) solo.getView("search_src_text"), "agua");
		solo.sleep(2000);
		
		// Press search button
		solo.pressSoftKeyboardSearchButton();

	
	}
	public void testSearchTagsOk(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		
		solo.clickInList(0);
		solo.sleep(2000);
		
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(1);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		//refrescos
		solo.clickInList(2, 0);
		solo.sleep(2000);
		
	}
}
