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
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterInterfaceTest {
    @Rule
    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(Register.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }

    @Test
    public void EmailTypeTest() {
        Espresso.onView(withId(R.id.email)).perform(typeText("Email@mail.com")).check(matches(isDisplayed()));
    }

    @Test
    public void PasswordTypeTest() {
        Espresso.onView(withId(R.id.password)).perform(typeText("Pass")).check(matches(isDisplayed()));
    }

    @Test
    public void RegisterButtonClickTest() {
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void LoginButtonClickTest() {
        Espresso.onView(withId(R.id.login_button)).perform(click());
    }
}
