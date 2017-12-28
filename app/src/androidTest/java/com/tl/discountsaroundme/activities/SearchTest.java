package com.tl.discountsaroundme.activities;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.tl.discountsaroundme.activities.LoginActivityInterfaceTest.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void activate() {
        try {
            Intents.init();
            mActivityTestRule.launchActivity(new Intent());
            onView(withId(R.id.emailText)).perform(typeText("busines@gmail.com"));
            onView(withId(R.id.passwordText)).perform(typeText("123456"));
            pressBack();
            onView(withId(R.id.login)).perform(click());
            onView(isRoot()).perform(waitFor(5000));
            intended(hasComponent(MainActivity.class.getName()));
            Intents.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchASelectSweater() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(childAtPosition(
                allOf(withId(R.id.item_grid),
                        childAtPosition(
                                withId(R.id.quickreturn_coordinator),
                                0)),
                0)))
                .perform(swipeDown());

        ViewInteraction searchInputView = onView(
                allOf(withId(R.id.search_bar_text),
                        childAtPosition(
                                allOf(withId(R.id.search_input_parent),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchInputView.perform(click(), replaceText("a"), closeSoftKeyboard());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.suggestions_list),
                        childAtPosition(
                                withId(R.id.suggestions_list_container),
                                0), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
    }

    @Test
    public void searchJeans() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(childAtPosition(
                allOf(withId(R.id.item_grid),
                        childAtPosition(
                                withId(R.id.quickreturn_coordinator),
                                0)),
                0)))
                .perform(swipeDown());

        ViewInteraction searchInputView = onView(
                allOf(withId(R.id.search_bar_text),
                        childAtPosition(
                                allOf(withId(R.id.search_input_parent),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchInputView.perform(click(), replaceText("jeans"), closeSoftKeyboard());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.suggestions_list),
                        childAtPosition(
                                withId(R.id.suggestions_list_container),
                                0), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
    }
}
