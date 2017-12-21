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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class CategoriesTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

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
    public void clothingTest() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.clothes_tag), withText("Clothing"),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout),
                                        childAtPosition(
                                                withId(R.id.horizontalScroll),
                                                0)),
                                1)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        1),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));
    }

    @Test
    public void shoesCategory() throws Exception {
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.clothes_tag), withText("Shoes")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.item_grid),
                                childAtPosition(
                                        withId(R.id.quickreturn_coordinator),
                                        0)),
                        1),
                        isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));
    }

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
}
