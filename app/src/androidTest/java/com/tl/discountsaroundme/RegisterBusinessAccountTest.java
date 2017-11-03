package com.tl.discountsaroundme;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterBusinessAccountTest {
    @Rule
    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(Register.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }

    @Test
    public void CheckBoxClickTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
    }

    @Test
    public void CheckBoxIsUncheckedTest() {
        onView(withId(R.id.cbBusinessAccount)).check(matches(isNotChecked()));
    }

    @Test
    public void CheckBoxClickAndDisplayShopNameTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.etShopName)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckBoxClickAndDisplayShopLocationTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.tvShopLocation)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckBoxClickAndDisplayShopTypeTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.sShopType)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckBoxClickAndDisplayAllTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.etShopName)).check(matches(isDisplayed()));
        onView(withId(R.id.tvShopLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.sShopType)).check(matches(isDisplayed()));
    }
    @Test
    public void CheckBoxNotCheckedDisplayShopNameTest() {
        onView(withId(R.id.cbBusinessAccount)).check(matches( isNotChecked()));
        onView(withId(R.id.etShopName)).check(matches(not(isDisplayed())));
    }
    @Test
    public void CheckBoxNotCheckedDisplayShopLocationTest() {
        onView(withId(R.id.cbBusinessAccount)).check(matches( isNotChecked()));
        onView(withId(R.id.tvShopLocation)).check(matches(not(isDisplayed())));
    }
    @Test
    public void CheckBoxNotCheckedDisplayShopTypeTest() {
        onView(withId(R.id.cbBusinessAccount)).check(matches(isNotChecked()));
        onView(withId(R.id.sShopType)).check(matches(not(isDisplayed())));
    }
    @Test
    public void CheckBoxNotCheckedDisplayAllTest() {
        onView(withId(R.id.cbBusinessAccount)).check(matches(isNotChecked()));
        onView(withId(R.id.etShopName)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tvShopLocation)).check(matches(not(isDisplayed())));
        onView(withId(R.id.sShopType)).check(matches(not(isDisplayed())));
    }
    @Test
    public void ShopNameTypeTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.etShopName)).check(matches(isDisplayed()));
        onView(withId(R.id.etShopName)).perform(typeText("SHOPNAME")).check(matches(withText("SHOPNAME")));
    }

    @Test
    public void ShopLocationTextViewTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.tvShopLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.tvShopLocation)).check(matches(withText("LATLANG")));
    }
    @Test
    public void ShopTypeTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).check(matches(isDisplayed()));
    }
    /**
    TODO: TEST THE SPINNER!
     */
}
