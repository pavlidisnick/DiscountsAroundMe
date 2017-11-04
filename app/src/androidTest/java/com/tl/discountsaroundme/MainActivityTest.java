package com.tl.discountsaroundme;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.tl.discountsaroundme.Activities.MainActivity;
import com.tl.discountsaroundme.Entities.Item;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
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
        onView(withId(R.id.menu_discounts)).perform(click()).check(matches(isDisplayed()));
    }

    /**
     * Discounts Fragment Test
     */
    @Test
    public void DiscountsListTest (){
        onView(withId(R.id.menu_discounts)).perform(click());
       onView(withId(R.id.item_grid)).check(matches(isDisplayed()));
    }

    @Test
    public void MapTest() {
        onView(withId(R.id.menu_map)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void ClothesTest() {
        onView(withId(R.id.menu_clothes)).perform(click()).check(matches(isDisplayed()));
    }

}
