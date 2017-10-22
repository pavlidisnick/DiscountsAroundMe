package com.tl.discountsaroundme;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Created by rezu on 22/10/2017.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginInterfaceTest {

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule(Login.class);

    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }
    @Test
    public void EmailTypeTest(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("Email@mail.com")).check(matches(isDisplayed()));
    }
    @Test
    public void PasswordTypeTest(){
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("Pass")).check(matches(isDisplayed()));
    }
    @Test
    public void LoginButtonClickTest(){
        Espresso.onView(withId(R.id.login)).perform(click());
    }
}
