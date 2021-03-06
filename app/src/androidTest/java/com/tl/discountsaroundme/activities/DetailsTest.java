package com.tl.discountsaroundme.activities;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tl.discountsaroundme.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tl.discountsaroundme.activities.LoginActivityInterfaceTest.waitFor;
import static org.hamcrest.Matchers.allOf;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailsTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void activate() {
        try {
            Intents.init();
            mActivityTestRule.launchActivity(new Intent());
            onView(withId(R.id.emailText)).perform(typeText("busines@gmail.com"));
            onView(withId(R.id.passwordText)).perform(typeText("123456"));
            ViewActions.pressBack();
            onView(withId(R.id.login)).perform(click());
            onView(isRoot()).perform(waitFor(5000));
            intended(hasComponent(MainActivity.class.getName()));
            Intents.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sweaterDetails() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.item_grid),
                        childAtPosition(
                                withId(R.id.quickreturn_coordinator)
                        )));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.description),
                withText("Long cozy nights with this special thing"),
                isDisplayed()));

        onView(allOf(withId(R.id.store), withText("testShop")))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.type), withText("Type: Clothing")))
                .check(matches(isDisplayed()));

        pressBack();
    }

    @Test
    public void jeansDetails() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.item_grid),
                        childAtPosition(
                                withId(R.id.quickreturn_coordinator)
                        )));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.description), withText("Nothing special here.Casual jeans")))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.store), withText("Bershka")))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.type), withText("Type: Clothing")))
                .check(matches(isDisplayed()));

        pressBack();
    }

    @Test
    public void test() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.item_grid),
                        childAtPosition(
                                withId(R.id.quickreturn_coordinator)
                        )));
        recyclerView3.perform(actionOnItemAtPosition(3, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.description), withText("HODDIES ARE SUPER COZY!YEAA")))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.type), withText("Type: Clothing")))
                .check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + 0 + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(0));
            }
        };
    }
}
