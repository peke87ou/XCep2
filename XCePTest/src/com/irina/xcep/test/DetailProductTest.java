package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;


/**	
  	1.	testShareFacebookOk:  Para esta proba comprobamos que o usuario pode acceder a Facebook dende a opción de compartir, facendo clic na opción de facebook, é o test é correcto.
	2.	testShareTwitterOk:  Para esta proba comprobamos que o usuario pode acceder a Twitter dende a opción de compartir, facendo clic na opción de twitter, é o test é correcto.
	3.	testAddProductListShoppingOk:  Para esta proba comprobamos que o usuario pode engadir o produto a lista da compra onde se atopa, facendo clic no botón de engadir, é o test é correcto.
	4.	testAccessScanDetailProductOk:  Para esta proba comprobamos que o usuario pode acceder o detalle do produto dixitalizando, facendo clic no botón unha vez atopado o produto “Ver Detalle do Produto”, é o test é correcto.
	5.	testAccessCatalogDetailProductOk: Para esta proba comprobamos que o usuario pode acceder o detalle do produto dende o catalogo, facendo clic nun produto, é o test é correcto.
	6.	testAccessShoppingCartDetailProductOk:  Para esta proba comprobamos que o usuario pode acceder o detalle do produto , facendo clic prolongado no botón do dialogo “Ver Detalle do Produto”, é o test é correcto.
*/

public class DetailProductTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public DetailProductTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public DetailProductTest() {
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
	
	public void testShareFacebookOk(){
		
		abrirDetalle();
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.imageButtonShare1));
		
		solo.waitForDialogToOpen();
		solo.sleep(2000);
	}
	
	public void testShareTwitterOk(){
		
		abrirDetalle();
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.imageButtonShare1));
		solo.waitForDialogToOpen();
		solo.sleep(2000);
	}
	
	public void testAddProductListShoppingOk(){
		
		abrirDetalle();
		
		solo.clickOnView(solo.getView(com.irina.xcep.R.id.add_product_list));
		
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		solo.sleep(1000);
	}
	
	public void atestAccessScanDetailProductOk(){
		
		abrirDetalle();
	}
	
	public void testAccessCatalogDetailProductOk(){
		
		abrirDetalle();
	}
	
	public void testAccessShoppingCartDetailProductOk(){
		
		abrirLista();
		
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(0);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		solo.clickLongInList(0, 0);
		
		solo.waitForDialogToOpen();
		solo.clickOnText("Ver detalle del Producto");
		assertTrue("com.irina.xcep.DetailProductActivity is not found!", solo.waitForActivity(com.irina.xcep.DetailProductActivity.class));
		solo.sleep(2000);
	}
	
	
	
	private void abrirLista(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		solo.clickInList(0);
	}
	
	private void abrirDetalle(){
		
		abrirLista();
		
		solo.sleep(2000);
		ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
		View tabCatalogo = tabs.getChildAt(1);
		solo.clickOnView(tabCatalogo);
		solo.sleep(1000);
		solo.clickInList(0, 1);
		
		assertTrue("com.irina.xcep.DetailProductActivity is not found!", solo.waitForActivity(com.irina.xcep.DetailProductActivity.class));
		solo.sleep(2000);
	}
	
}

