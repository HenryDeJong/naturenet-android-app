package org.naturenet;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.ui.MainActivity;

/**
 * Created by rigot on 1/30/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    static final String USERNAME = "rtrejo@uncc.edu";
    static final String PASSWORD = "666666";
    static final String SUCCESS_STRING = "rtrejo";

    //specify activity where test will take place
    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void login_test_not_logged_in() throws InterruptedException {

        //on the MainActivity w/ Launch Fragment displayed (first screen that is displayed when app is launched)
        onView(withId(R.id.launch_tv_sign_in)).perform(click());

        //type in username/email
        onView(withId(R.id.login_et_email_address)).perform(typeText(USERNAME), closeSoftKeyboard());

        //type in password
        onView(withId(R.id.login_et_password)).perform(typeText(PASSWORD), closeSoftKeyboard());

        //click login button
        onView(withId(R.id.login_b_sign_in)).perform(click());

        //verify success
        onView(withId(R.id.nav_tv_display_name)).check(matches(withText(SUCCESS_STRING)));
    }

    @Test
    public void login_test_already_logged_in() throws InterruptedException {

        //give the screen some time to open the drawer
        Thread.sleep(2000);

        //on the MainActivity w/ Launch Fragment displayed (first screen that is displayed when app is launched)
        onView(withText("Logout")).perform(click());

        onView(withId(R.id.nav_b_sign_in)).perform(click());

        //type in username/email
        onView(withId(R.id.login_et_email_address)).perform(typeText(USERNAME), closeSoftKeyboard());

        //type in password
        onView(withId(R.id.login_et_password)).perform(typeText(PASSWORD), closeSoftKeyboard());

        //click login button
        onView(withId(R.id.login_b_sign_in)).perform(click());

        //verify success
        onView(withId(R.id.nav_tv_display_name)).check(matches(withText(SUCCESS_STRING)));
    }

}
