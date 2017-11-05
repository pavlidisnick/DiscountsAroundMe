package com.tl.discountsaroundme;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tl.discountsaroundme.Activities.Login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginInterfaceTest {

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);


    @Test
    public void normalLogin(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("pavlidis.nik95@gmail.com"));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("pavlidis"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void wrongEmail(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("email"));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("pavlidis"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void wrongPassword(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("pavlidis.nik95@gmail.com"));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("password"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void wrongData(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("email"));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("password"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void emptyEmail(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText(""));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("pavlidis"),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void emptyPassword(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText("pavlidis.nik95@gmail.com"));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText(""),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void emptyData(){
        Espresso.onView(withId(R.id.emailText)).perform(typeText(""));
        Espresso.onView(withId(R.id.passwordText)).perform(typeText(""),
                closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }

}
