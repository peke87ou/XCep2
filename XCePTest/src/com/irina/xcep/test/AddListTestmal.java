package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.irina.xcep.AddShoppingListActivity;
import com.irina.xcep.R;
import com.irina.xcep.test.enums.AddListEnum;
import com.robotium.solo.Solo;

public class AddListTestmal extends ActivityInstrumentationTestCase2<AddShoppingListActivity> {
	
	private Solo solo;
	private AddShoppingListActivity addlist;
	private TestHelper helper;
	private EditText namelist;
	//private ButtonRectangle buttonlogin;
	
	static int TIME_OUT_LOGIN = 30000;

	public AddListTestmal(Class<AddShoppingListActivity> activityClass) {
		super(activityClass);
	}

	public AddListTestmal() {
		this(AddShoppingListActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		addlist = (AddShoppingListActivity) solo.getCurrentActivity();
		helper = new TestHelper(solo);
		
		namelist  = (EditText) addlist.findViewById(R.id.name_list);
		
		//buttonlogin = (ButtonRectangle) addlist.findViewById(R.id.login);
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	
	public void tesAddListOk() {
		
		helper.setAddListData(AddListEnum.CORRECTO);
		
		solo.enterText(namelist, helper.getNameList());
		
//		solo.clickOnView(buttonlogin);
		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
	}
	
	
//	public void testAddListNotName() {
//		
//		helper.setLoginData(LoginEnum.INCORRECTO);
//		
//		solo.enterText(namelist, helper.getUser());
////		solo.enterText(password, helper.getPass());
////		solo.clickOnView(buttonlogin);
//		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
//	}
//	
//	public void testAddListNotMarket() {
//		
//		helper.setLoginData(LoginEnum.INCORRECTO);
//		
//		solo.enterText(namelist, helper.getUser());
////		solo.enterText(password, helper.getPass());
////		solo.clickOnView(buttonlogin);
//		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
//	}
	
	public void tesAddListFailedEmpty() {
		
		helper.setAddListData(AddListEnum.INCORRECTO_EMPTY);
		
		solo.enterText(namelist, helper.getUser());
		//Utilizado para esperar por el retardo de las vistas en mostrar el error al estar vacias
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			Log.e(AddListTestmal.class.getName(),"Interrupted Exception");
		}
		
		assertEquals(true, !TextUtils.isEmpty(namelist.getError().toString()) && namelist.getError().toString().equalsIgnoreCase("É necesario encher o campo seleccionado"));
		
		
		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
	}

}
