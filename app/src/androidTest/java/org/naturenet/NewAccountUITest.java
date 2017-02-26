package org.naturenet;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.ui.MainActivity;

/**
 * Created by rigot on 2/16/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NewAccountUITest {


    static String EMAIL = "rigotre7@gmail.com";
    static String PASSWORD = "666666";
    static String USER_NAME = "rtrejo";
    static String NAME = "Rodrigo";
    static String AFFILIATION = "Reedy Creek";
    static String SUCCESS_STRING = "Logout";


    //specify activity where test will take place
    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void create_new_account() throws InterruptedException {


        //click on join button on the launch screen
        onView(withId(R.id.launch_ib_join)).perform(click());

        //type in all user information
        onView(withId(R.id.join_et_email_address)).perform(typeText(EMAIL), closeSoftKeyboard());
        onView(withId(R.id.join_et_password)).perform(typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.join_et_name)).perform(typeText(NAME),closeSoftKeyboard());
        onView(withId(R.id.join_et_user_name)).perform(typeText(USER_NAME), closeSoftKeyboard());
        onView(withId(R.id.join_et_affiliation)).perform(click());
        onView(withText(AFFILIATION)).perform(click());

        //click join button
        onView(withId(R.id.join_b_join)).perform(click());

        //check to see if sign up was successful
        onView(withId(R.id.nav_logout)).check(matches(withText(SUCCESS_STRING)));



    }
}
