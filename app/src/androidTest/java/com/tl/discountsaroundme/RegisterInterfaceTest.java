package com.tl.discountsaroundme;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterInterfaceTest {
    @Rule
    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(Register.class);


    @Test
    public void dataTaken(){
        Espresso.onView(withId(R.id.email)).perform(typeText("pavlidis.nik95@gmail.com"));
        Espresso.onView(withId(R.id.password)).perform(typeText("pavlidis"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void wrongEmail(){
        Espresso.onView(withId(R.id.email)).perform(typeText("Email"));
        Espresso.onView(withId(R.id.password)).perform(typeText("Password"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void emptyEmail(){
        Espresso.onView(withId(R.id.email)).perform(typeText(""));
        Espresso.onView(withId(R.id.password)).perform(typeText("Password"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void emptyPassword(){
        Espresso.onView(withId(R.id.email)).perform(typeText("pavlidis.nik95@gmail.com"));
        Espresso.onView(withId(R.id.password)).perform(typeText("Password"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void emptyData(){
        Espresso.onView(withId(R.id.email)).perform(typeText(""));
        Espresso.onView(withId(R.id.password)).perform(typeText(""),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void LoginButtonClickTest(){
        Espresso.onView(withId(R.id.login_button)).perform(click());
    }
}
