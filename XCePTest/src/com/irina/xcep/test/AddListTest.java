package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.AddShoppingListActivity;
import com.irina.xcep.LoginActivity;
import com.irina.xcep.R;
import com.irina.xcep.test.enums.AddListEnum;
import com.irina.xcep.test.enums.LoginEnum;
import com.irina.xcep.test.enums.SignUpEnum;
import com.robotium.solo.Solo;

public class AddListTest extends ActivityInstrumentationTestCase2<AddShoppingListActivity> {
	
	private Solo solo;
	private AddShoppingListActivity addlist;
	private TestHelper helper;
	private EditText namelist;
	private GridView gridSupermercados;
	private ButtonRectangle buttonCrear;
	
	static int TIME_OUT_LOGIN = 30000;

	public AddListTest(Class<AddShoppingListActivity> activityClass) {
		super(activityClass);
	}

	public AddListTest() {
		this(AddShoppingListActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		addlist = (AddShoppingListActivity) solo.getCurrentActivity();
		helper = new TestHelper(solo);
		
		namelist  = (EditText) addlist.findViewById(R.id.name_list);
		buttonCrear = (ButtonRectangle) addlist.findViewById(R.id.crear_new_list);
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	
	public void testAddListOk() {
		
		helper.setAddListData(AddListEnum.tesAddListOk);
		
		solo.enterText(namelist, helper.getNameList());
		solo.clickOnView(buttonCrear);
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			Log.e(SignUpTest.class.getName(),"Interrupted Exception");
		}
		

	}
	
	
//	public void testAddListNotName() {
//		
//		helper.setLoginData(LoginEnum.INCORRECTO);
//		
//		solo.enterText(username, helper.getUser());
//		solo.enterText(password, helper.getPass());
//		solo.clickOnView(buttonlogin);
//		solo.searchText("As credenciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
//	}
//	public void testAddListNotMarket() {
//		
//		helper.setLoginData(LoginEnum.INCORRECTO);
//		
//		solo.enterText(username, helper.getUser());
//		solo.enterText(password, helper.getPass());
//		solo.clickOnView(buttonlogin);
//		solo.searchText("As credecnciales son inválidas, se non está auténticado no sistema, por favor rexistrese");
//	}

}
