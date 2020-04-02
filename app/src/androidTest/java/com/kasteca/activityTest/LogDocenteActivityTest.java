package com.kasteca.activityTest;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.kasteca.R;
import com.kasteca.activity.LogDocenteActivity;
import com.kasteca.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogDocenteActivityTest {

    private Bundle docente;

    @Rule
    //il terzo parametro è settato a false per non far partire l'activity automaticamente
    public ActivityTestRule<LogDocenteActivity> logDocenteActivityActivityTestRule = new ActivityTestRule<>(LogDocenteActivity.class, false, false);

    //bisogna ancora aggiungere i test
    @Test
    public void logDocenteActivityActivityTest() {
        Intent intent = new Intent();
        docente = new Bundle();
        docente.putString("id", "BbrK7iSha8RcYNdnkiOLm0H9XAk1");
        docente.putString("nome", "Eugenio");
        docente.putString("cognome", "Zimeo");
        docente.putString("email", "zimeo@unisannio.it");
        intent.putExtras(docente);
        logDocenteActivityActivityTestRule.launchActivity(intent);

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
