package com.tl.discountsaroundme.Activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tl.discountsaroundme.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }

    @Test
    public void LayoutDisplayTest() {
        getUserOptionView();
        onView(withId(R.id.btMailChange)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDisplayName)).check(matches(isDisplayed()));
        onView(withId(R.id.Image)).check(matches(isDisplayed()));
        onView(withId(R.id.btPassChange)).check(matches(isDisplayed()));
        onView(withId(R.id.btImageChange)).check(matches(isDisplayed()));
        onView(withId(R.id.btDisplayName)).check(matches(isDisplayed()));
        onView(withId(R.id.btDeleteAccount)).check(matches(isDisplayed()));
    }

    @Test
    public void EmailButtonClickLayoutDisplayTest() {
        getUserOptionView();
        onView(withId(R.id.btMailChange)).perform(click());
        onView(withId(R.id.btOk)).check(matches(isDisplayed()));
        onView(withId(R.id.etChangeEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void PasswordButtonClickLayoutDisplayTest() {
        getUserOptionView();
        onView(withId(R.id.btPassChange)).perform(click());
        onView(withId(R.id.btOk)).check(matches(isDisplayed()));
        onView(withId(R.id.etChangePass)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void DisplayNameButtonClickLayoutDisplayTest() {
        getUserOptionView();
        onView(withId(R.id.btDisplayName)).perform(click());
        onView(withId(R.id.btOk)).check(matches(isDisplayed()));
        onView(withId(R.id.etDisplayName)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void DeleteAccountButtonClickLayoutDisplayTest() {
        getUserOptionView();
        onView(withId(R.id.btDeleteAccount)).perform(click());
        onView(withId(R.id.btYes)).check(matches(isDisplayed()));
        onView(withId(R.id.btNo)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void EmailChangeLayoutTest() {
        getUserOptionView();
        onView(withId(R.id.btMailChange)).perform(click());
        onView(withId(R.id.etChangeEmail)).perform(typeText("Email@email.com"), closeSoftKeyboard()).check(matches(withText("Email@email.com")));
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    @Test
    public void PasswordChangeLayoutTest() {
        getUserOptionView();
        onView(withId(R.id.btPassChange)).perform(click());
        onView(withId(R.id.etChangePass)).perform(typeText("Password"), closeSoftKeyboard()).check(matches(withText("Password")));
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    @Test
    public void DisplayChangeLayoutTest() {
        getUserOptionView();
        onView(withId(R.id.btDisplayName)).perform(click());
        onView(withId(R.id.etDisplayName)).perform(typeText("DisplayName"), closeSoftKeyboard()).check(matches(withText("DisplayName")));
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    @Test
    public void DeleteAccountLayoutTest() {
        getUserOptionView();
        onView((withId(R.id.btDeleteAccount))).perform(click());
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    public void getUserOptionView() {
        try {
            Thread.sleep(336);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction bottomNavigationItemView = onView(
                Matchers.allOf(withId(R.id.menu_user_options), isDisplayed()));
        bottomNavigationItemView.perform(click());
        onView(withId(R.id.btEditUser)).perform(click());
    }
}
