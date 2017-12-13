package com.tl.discountsaroundme;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.activities.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityInterfaceTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tl.discountsaroundme", appContext.getPackageName());
    }

    @Test
    public void EmailTypeTest() {
        onView(withId(R.id.email)).perform(typeText("Email@mail.com")).check(matches(isDisplayed()));
    }

    @Test
    public void PasswordTypeTest() {
        onView(withId(R.id.password)).perform(typeText("Pass")).check(matches(isDisplayed()));
    }

    @Test
    public void RegisterButtonClickTest() {
        onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void LoginButtonClickTest() {
        onView(withId(R.id.login_button)).perform(click());
    }
    /**
     * ADDED more tests, because of business account  addition changes
     */
    @Test
    public void EmailTypeTest2(){
        onView(withId(R.id.email)).perform(typeText("EMAIL@EMAIL.COM")).check(matches(withText("EMAIL@EMAIL.COM")));
    }
    @Test
    public void PassTypeTest2(){
        onView(withId(R.id.password)).perform(typeText("pass")).check(matches(withText("pass")));
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
        // TODO: In case location is added test the location using mocks
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
    }
    @Test
    public void ShopTypeTest() {
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).check(matches(isDisplayed()));
    }

    @Test
    public void SpinnerChoiceTest1(){
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Choose a type"))).perform(click());
        onView(withId(R.id.tvSpinnerItem)).check(matches(withText("Choose a type")));
    }
    @Test
    public void SpinnerChoiceTest2(){
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Clothing"))).perform(click());
        onView(withId(R.id.tvSpinnerItem)).check(matches(withText("Clothing")));
    }
    @Test
    public void SpinnerChoiceTest3(){
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Technology"))).perform(click());
        onView(withId(R.id.tvSpinnerItem)).check(matches(withText("Technology")));
    }
    @Test
    public void SpinnerChoiceTest4(){
        onView(withId(R.id.cbBusinessAccount)).perform(click()).check(matches( isChecked()));
        onView(withId(R.id.sShopType)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Sport"))).perform(click());
        onView(withId(R.id.tvSpinnerItem)).check(matches(withText("Sport")));
    }

    /**
     * TODO: ADD some intent Tests
     */

    @Test
    public void LoginActivityIntentTest() throws Exception{
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.login_button)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }

}