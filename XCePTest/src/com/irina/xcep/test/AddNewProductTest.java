package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

/**
 * 1.	testAddNewProductOk:  Para esta proba introducimos  un nome “Auga”, unha marca “Mondariz”, descrición “Botella 1000ml”,
 *  sacar unha imaxe da botella “url”, prezo “1.20”  e tag “bebida”, con ditos datos permítenos engadir un novo produto 
 *  correctamente na aplicación e o test é correcto.
 * 
 * 2.	testAddNotImageNewProduct: Se non seleccionamos ningunha imaxe para o  produto, para esta proba o sistema indícanos
 *   un erro de que debemos seleccionar unha imaxe para engadir o novo produto.
 * 
 * 3.   testNotNameNewProduct
 * 
 * 4.   testNotMarkNewProduct
 * 
 * 5.   testNotDescriNewProduct
 * 
 * 6.   tesImageGaleryNewProduct
 * 
 * 7.   tesImageCamNewProduct
 * 
 * 8.   testNotImageNewProduct
 * 
 * 9.   testNotPriceNewProduct
 * */

public class AddNewProductTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	TestHelper helper;
	static int TIME_OUT_LOGIN = 30000;

	public AddNewProductTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}
	
	public AddNewProductTest() {
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
	
	public void testAddNewProductOk(){
		
		abrirLista();
	}
	
	public void testAddNotImageNewProduct(){
		
		abrirLista();
	}
	
	public void testNotNameNewProduct(){
		
		abrirLista();
	}
	
	public void testNotMarkNewProduct(){
		
		abrirLista();
	}
	
	public void testNotDescriNewProduct(){
		
		abrirLista();
	}
	
	public void testImageGaleryNewProduct(){
		
		abrirLista();
	}
	
	public void tesImageCamNewProduct(){
		
		abrirLista();
	}
	
	public void testNotImageNewProduct(){
		
		abrirLista();
	}
	
	public void testNotPriceNewProduct(){
		
		abrirLista();
	}

	
	private void abrirLista(){
		solo.waitForActivity(com.irina.xcep.SplashActivity.class, 2000);
		assertTrue("com.irina.xcep.MenuActivity is not found!", solo.waitForActivity(com.irina.xcep.MenuActivity.class));
		solo.waitForDialogToClose();
		
		solo.clickInList(0);
	}
}

