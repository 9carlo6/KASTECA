package com.kasteca.activityTest;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.kasteca.activity.LogStudenteActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogStudenteActivityTest {

    private Bundle studente;

    @Rule
    //il terzo parametro è settato a false per non far partire l'activity automaticamente
    public ActivityTestRule<LogStudenteActivity> logStudenteActivityActivityTestRule = new ActivityTestRule<>(LogStudenteActivity.class, false, false);

    //bisogna ancora aggiungere i test
    @Test
    public void logStudenteActivityActivityTest() {
        Intent intent = new Intent();
        studente = new Bundle();
        studente.putString("id", "dCnmTMhVJzUx7sPDsQS28CHx2aC2");
        studente.putString("nome", "Gregorio");
        studente.putString("cognome", "Dalia");
        studente.putString("email", "gregorio.dalia@studenti.unisannio.it");
        intent.putExtras(studente);
        logStudenteActivityActivityTestRule.launchActivity(intent);
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
