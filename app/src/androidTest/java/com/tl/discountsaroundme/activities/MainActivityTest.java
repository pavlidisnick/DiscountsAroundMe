package com.tl.discountsaroundme.activities;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tl.discountsaroundme.R;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static com.tl.discountsaroundme.activities.LoginActivityInterfaceTest.waitFor;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void activate() {
        try {
            Intents.init();
            mActivityTestRule.launchActivity(new Intent());
            onView(withId(R.id.emailText)).perform(typeText("busines@gmail.com"));
            onView(withId(R.id.passwordText)).perform(typeText("123456"));
            pressBack();
            onView(withId(R.id.login)).perform(click());
            onView(isRoot()).perform(waitFor(5000));
            intended(hasComponent(MainActivity.class.getName()));
            Intents.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMenuMap() {
        try {
            Thread.sleep(336);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_map), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());
    }

    @Test
    public void testSlideOnMenu() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(336);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_map), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.menu_user_options), isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager3.perform(swipeRight());

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager4.perform(swipeRight());
    }


    @Test
    public void testTopDiscountsMenu() {
        try {
            Thread.sleep(3597);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction searchInputView = onView(
                allOf(withId(R.id.search_bar_text),
                        withParent(withId(R.id.search_input_parent)),
                        isDisplayed()));
        searchInputView.perform(click());

        ViewInteraction searchInputView2 = onView(
                allOf(withId(R.id.search_bar_text),
                        withParent(withId(R.id.search_input_parent)),
                        isDisplayed()));
        searchInputView2.perform(replaceText("test"), closeSoftKeyboard());
    }

}
