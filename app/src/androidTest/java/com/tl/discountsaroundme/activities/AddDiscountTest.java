package com.tl.discountsaroundme.activities;


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
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddDiscountTest {

    @Rule
    public ActivityTestRule<AddDiscountsActivity> mActivityTestRule = new ActivityTestRule<>(AddDiscountsActivity.class);

    @Test
    public void withNoImage() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please add image")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void discountOutOfRange1() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("-5"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Discount must be number 1 to 100")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void discountOutOfRange2() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("150"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Discount must be number 1 to 100")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void emptyFields1() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void emptyFields2() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("50"), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void emptyFields3() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("50"), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void emptyFields4() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextName),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("50"), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void emptyFields5() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextCategory),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPrice),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("50"), closeSoftKeyboard());


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextDiscount),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("50"), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }

    @Test
    public void allEmpty() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAddItem),
                        isDisplayed()));
        appCompatButton3.perform(click());

        AddDiscountsActivity activity = mActivityTestRule.getActivity();
        onView(withText("Please fill the fields")).
                inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }
}
