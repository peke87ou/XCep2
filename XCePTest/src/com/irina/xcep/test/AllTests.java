package com.irina.xcep.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class AllTests extends ActivityInstrumentationTestCase2<Activity> {
	
	public AllTests(Class<Activity> activityClass) {
		super(activityClass);
	}

	public AllTests() {
		this(Activity.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        suite.addTestSuite(SplashTest.class);
        suite.addTestSuite(SignUpTest.class);
        suite.addTestSuite(LoginTest.class);
        suite.addTestSuite(HomeTest.class);
        suite.addTestSuite(AddListTestmal.class);
        return suite;
    }
}
