package com.tl.discountsaroundme;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.tl.discountsaroundme.Activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginInterfaceTest {

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);

    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }

    @Test
    public void EmailTypeTest() {
        Espresso.onView(withId(R.id.emailText)).perform(typeText("Email@mail.com")).check(matches(isDisplayed()));
    }

    @Test
    public void PasswordTypeTest() {
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("Pass")).check(matches(isDisplayed()));
    }

    @Test
    public void LoginButtonClickTest() {
        Espresso.onView(withId(R.id.login)).perform(click());

    }

    @Test
    public void PasswordTypeTest2() {
        Espresso.onView(withId(R.id.passwordText)).perform(typeText("Pass")).check(matches(withText("Pass")));
    }

    @Test
    public void EmailTypeTest2() {
        Espresso.onView(withId(R.id.emailText)).perform(typeText("Email@mail.com")).check(matches(withText("Email@mail.com")));
    }
    /**
     * TODO:Intent Tests
     */
    @Test
    public void LoginActivityIntentTest() throws Exception{
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.emailText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.passwordText)).perform(typeText("123456"));
        onView(withId(R.id.login)).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    /**
     * Method to  make espresso wait up
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
