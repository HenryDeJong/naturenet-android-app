package org.naturenet;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.data.model.Observation;
import org.naturenet.ui.MainActivity;


import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by rigot on 2/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DrawerNavigationTest {

    static String COMMUNITIES = "Communities";
    static String PROJECTS = "Projects";
    static String DESIGN_IDEAS = "Design Ideas";
    static String EXPLORE = "Explore";
    static String EXPLORE_CAPS = "EXPLORE";
    static String TRACKS = "Tracks";
    static String NATURENET = "NatureNet";
    static String MUSHROOMS = "Mushrooms";

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void navigation_test() throws InterruptedException {


        //open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on design ideas link
        onView(withText(DESIGN_IDEAS)).perform(click());

        //make sure we are on the correct screen
        onView(withId(R.id.app_bar_main_tv)).check(matches(withText(DESIGN_IDEAS)));

        //open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on communities link
        onView(withText(COMMUNITIES)).perform(click());

        //make sure we're on the correct screen
        onView(withId(R.id.app_bar_main_tv)).check(matches(withText(COMMUNITIES)));

        //open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on projects link
        onView(withText(PROJECTS)).perform(click());

        //make sure we are indeed in the projects screen
        onView(withId(R.id.app_bar_main_tv)).check(matches(withText(PROJECTS)));

        //open navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on Explore link
        onView(withText(EXPLORE)).perform(click());

        //let map load
        Thread.sleep(2000);

        //make sure we're on the correct screen
        onView(withId(R.id.app_bar_main_tv)).check(matches(withText(NATURENET)));

        //open the nav drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on Gallery link
        onView(allOf(withText(R.string.nav_gallery), withEffectiveVisibility(VISIBLE))).perform(click());

        //make sure we are on the Gallery screen
        onView(withId(R.id.app_bar_explore_tv)).check(matches(withText(EXPLORE_CAPS)));


    }

    @Test
    public void view_project_test(){

        //open navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //click on projects link
        onView(withText(PROJECTS)).perform(click());

        //click on Tracks Project
        onView(withText(TRACKS)).perform(click());

        //make sure we are looking at Tracks
        onView(withId(R.id.project_tv_name)).check(matches(withText(TRACKS)));

        //
        onData(allOf(is(instanceOf(Observation.class)), withName("Snowmass CO"))).perform(click());

        //click the back button in the toolbar
        onView(withId(R.id.project_back)).perform(click());

        //click the back button once again
        onView(withId(R.id.project_back)).perform(click());

        //open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //open the projects tab again
        onView(withText(PROJECTS)).perform(click());

        //open the mushrooms link
        onView(withText(MUSHROOMS)).perform(click());

        onView(withId(R.id.project_tv_name)).check(matches(withText(MUSHROOMS)));

        onData(allOf(is(instanceOf(Observation.class)), withSiteId("rcnc"))).atPosition(0).perform(click());

        onView(withId(R.id.project_back)).perform(click());
        onView(withId(R.id.project_back)).perform(click());
    }

    @Test
    public void view_gallery_test(){

        //open drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        //open gallery link
        onView(allOf(withText(R.string.nav_gallery), withEffectiveVisibility(VISIBLE))).perform(click());

        onData(allOf(is(instanceOf(Observation.class)), whereObservation("Freedom park"))).perform(click());

        onView(withId(R.id.explore_tv_back)).perform(click());

        onData(allOf(is(instanceOf(Observation.class)), whereObservation("Snowmass CO"))).perform(click());

        onView(withId(R.id.explore_tv_back)).perform(click());

        onData(allOf(is(instanceOf(Observation.class)), whereObservation("RCNC trail"))).perform(click());


    }



    /*
    CUSTOM MATCHERS
     */
    public static Matcher withName (final String name) {
        return new BoundedMatcher<Object, Observation>(Observation.class) {
            @Override
            protected boolean matchesSafely(Observation obs) {
                    return name.equals(obs.getWhere());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + name);
            }
        };
    }

    public static Matcher withSiteId (final String name){
        return new BoundedMatcher<Object, Observation>(Observation.class){
            @Override
            protected boolean matchesSafely(Observation item) {
                return name.equals(item.siteId);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    public static Matcher whereObservation (final String name){
        return new BoundedMatcher<Object, Observation>(Observation.class){
            @Override
            protected boolean matchesSafely(Observation item) {
                return name.equals(item.where);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }





    }
