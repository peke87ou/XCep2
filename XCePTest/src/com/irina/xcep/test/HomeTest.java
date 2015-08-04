package com.irina.xcep.test;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Suppress;

import com.irina.xcep.MenuActivity;
import com.irina.xcep.R;
import com.robotium.solo.Solo;

public class HomeTest extends ActivityInstrumentationTestCase2<MenuActivity> {
	
	private Solo solo;
//	private HomeFragment home;
	static int TIME_OUT_LOGIN = 30000;
	Fragment fragment;

	public HomeTest(Class<MenuActivity> activityClass) {
		super(activityClass);
	}

	public HomeTest() {
		this(MenuActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		//home = (HomeFragment) solo.getCurrentActivity();
		//fragment = solo.getCurrentActivity().getFragmentManager();
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	@Suppress
	public void testDesconectar() {
		//Fragment 
		fragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.username);
		

	}
	
	
	
}
