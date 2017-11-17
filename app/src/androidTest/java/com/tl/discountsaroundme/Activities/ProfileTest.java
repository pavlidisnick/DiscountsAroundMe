package com.tl.discountsaroundme.Activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.tl.discountsaroundme.Activities.Login;
import com.tl.discountsaroundme.Activities.Register;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

import com.tl.discountsaroundme.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by rezu on 17/11/2017.
 */
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
        onView(withId(R.id.tvUserProfile)).check(matches(isDisplayed()));
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
        onView(withId(R.id.etChangeEmail)).perform(typeText("Email@email.com")).check(matches(withText("Email@email.com")));
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    @Test
    public void PasswordChangeLayoutTest() {
        getUserOptionView();
        onView(withId(R.id.btPassChange)).perform(click());
        onView(withId(R.id.etChangePass)).perform(typeText("Password")).check(matches(withText("Password")));
        onView(withId(R.id.etPassword)).perform(typeText("123456")).check(matches(withText("123456")));
    }

    @Test
    public void DisplayChangeLayoutTest() {
        getUserOptionView();
        onView(withId(R.id.btDisplayName)).perform(click());
        onView(withId(R.id.etDisplayName)).perform(typeText("DisplayName")).check(matches(withText("DisplayName")));
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
