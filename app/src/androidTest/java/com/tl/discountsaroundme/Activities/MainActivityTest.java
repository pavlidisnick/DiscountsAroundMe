package com.tl.discountsaroundme.Activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tl.discountsaroundme.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTestMenuMap() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(336);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_map), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.nearbyButton), withText("Nearby"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.shopsButton), withText("Shops"), isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3363);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageView = onView(
                allOf(withContentDescription("Η τοποθεσία μου"), isDisplayed()));
        imageView.perform(click());

    }



    @Test
    public void mainActivityTestSlide() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(336);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_map), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.menu_user_options), isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager3.perform(swipeRight());

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager4.perform(swipeRight());



    }

    @Test
    public void mainActivityTestUserOptions() {

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_user_options), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager3.perform(swipeLeft());

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager4.perform(swipeLeft());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.notifyDiscounts), withText("Notify Me for Discounts "), isDisplayed()));
        switch_.perform(click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.notifyDiscounts), withText("Notify Me for Discounts "), isDisplayed()));
        switch_2.perform(click());
    }



    @Test
    public void FirstScreenAllParameters() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3597);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.clothes_tag), withText("clothes"),
                        withParent(allOf(withId(R.id.linear_layout),
                                withParent(withId(R.id.horizontalScroll))))));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.jewelry_tag), withText("jewelry"),
                        withParent(allOf(withId(R.id.linear_layout),
                                withParent(withId(R.id.horizontalScroll))))));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.a), withText("shoes"),
                        withParent(allOf(withId(R.id.linear_layout),
                                withParent(withId(R.id.horizontalScroll))))));
        appCompatButton3.perform(scrollTo(), click());


        ViewInteraction searchInputView = onView(
                allOf(withId(R.id.search_bar_text),
                        withParent(withId(R.id.search_input_parent)),
                        isDisplayed()));
        searchInputView.perform(click());

        ViewInteraction searchInputView2 = onView(
                allOf(withId(R.id.search_bar_text),
                        withParent(withId(R.id.search_input_parent)),
                        isDisplayed()));
        searchInputView2.perform(replaceText("test"), closeSoftKeyboard());

    }

}
