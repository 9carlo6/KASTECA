package com.kasteca.activity;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.kasteca.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoadDocenteCorsiTest {



    @Rule
    public ActivityTestRule<LogDocenteActivity> mActivityTestRule = new ActivityTestRule<>(LogDocenteActivity.class);

    @Before
    public void setUpTest(){
        Bundle bundleTest= new Bundle();
        bundleTest.putString("id","BbrK7iSha8RcYNdnkiOLm0H9XAk1\n" );
        bundleTest.putString("nome", "Eugenio");
        bundleTest.putString("cognome","Zimeo" );
        bundleTest.putString("email","zimeo@unisannio.it" );
        mActivityTestRule.getActivity().onCreate(bundleTest);

    }

    @Test
    public void loadDocenteCorsiTest() {

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.reclycleview_docente_corsi),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_docente),
                                        0),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
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
