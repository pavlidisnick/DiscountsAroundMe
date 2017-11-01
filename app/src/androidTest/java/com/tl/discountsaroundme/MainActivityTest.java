package com.tl.discountsaroundme;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tl.discountsaroundme.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme.Activities", appContext.getPackageName());
    }

    @Test
    public void DiscountsTest() {
        Espresso.onView(withId(R.id.menu_discounts)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void MapTest() {
        Espresso.onView(withId(R.id.menu_map)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void ClothesTest() {
        Espresso.onView(withId(R.id.menu_user_options)).perform(click()).check(matches(isDisplayed()));
    }
}
