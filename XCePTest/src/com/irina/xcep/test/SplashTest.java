package com.irina.xcep.test;

import android.test.ActivityInstrumentationTestCase2;

import com.gc.materialdesign.views.ButtonRectangle;
import com.irina.xcep.LoginActivity;
import com.irina.xcep.R;
import com.irina.xcep.SignupActivity;
import com.irina.xcep.SplashActivity;
import com.robotium.solo.Solo;

public class SplashTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	
	private Solo solo;
	private SplashActivity splash;

	public SplashTest(Class<SplashActivity> activityClass) {
		super(activityClass);
	}

	public SplashTest() {
		this(SplashActivity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo= new Solo(getInstrumentation(), getActivity());
		splash = (SplashActivity) solo.getCurrentActivity();
	}
	
	@Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
	
	
	public void testsplashLogin() {
		final ButtonRectangle buttonlogin = (ButtonRectangle) splash.findViewById(R.id.btnLogin);
		solo.clickOnView(buttonlogin);
		solo.assertCurrentActivity("Actividad incorrecta", LoginActivity.class);

	}
	
	
	public void testSplashSingup() {
		final ButtonRectangle buttonsingup = (ButtonRectangle) splash.findViewById(R.id.btnSignUp);
		solo.clickOnView(buttonsingup);
		solo.assertCurrentActivity("Actividad incorrecta", SignupActivity.class);
	}
}
