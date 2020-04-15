package com.kasteca.activityTest;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.kasteca.R;
import com.kasteca.activity.LogDocenteActivity;
import com.kasteca.activity.MainActivity;
import com.kasteca.util.EspressoIdlingResource;

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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginDocenteTest {



    private Bundle docente;

    @Rule
    //il terzo parametro è settato a false per non far partire l'activity automaticamente
    public ActivityTestRule<MainActivity> logDocenteActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    public ActivityTestRule<LogDocenteActivity> logDocenteF= new ActivityTestRule<>(LogDocenteActivity.class);

    @Before
    public void setUp(){

        //Registro l'idling resource per il test
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    //bisogna ancora aggiungere i test
    @Test
    public void logDocenteActivityActivityTest() {

        //Controllo se tutto è presente nel layout di login
        onView(withId(R.id.Email_Edit_Text)).check(matches(isDisplayed()));
        onView(withId(R.id.Password_Edit_Text)).check(matches(isDisplayed()));
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        //onView(withId(R.id.imageViewLogoKasteca)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.New_Account_Text)).check(matches(isDisplayed()));


        //Eseguiamo il login con il docente
        onView(withId(R.id.Email_Edit_Text)).perform(replaceText("studenteProva@studenti.unisannio.it"), closeSoftKeyboard());
        onView(withId(R.id.Password_Edit_Text)).perform(replaceText("passwordProva"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        //Controlliamo che l'ambiente grafico sia stato caricato
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        //onView(withId(R.id.swipeLayout_lista_corsi_docente)).check(matches(isDisplayed()));

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
