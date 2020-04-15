package com.kasteca.activityTest;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.activity.RichiestaIscrizioneActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static org.hamcrest.Matchers.is;

@LargeTest
public class RichiestaIscrizioneActivityTestCodiceCorsoNonEsistente {

    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE";

    @Rule
    public ActivityTestRule<RichiestaIscrizioneActivity> richiestaIscrizioneActivityActivityTestRule = new ActivityTestRule<>(RichiestaIscrizioneActivity.class);

    @Before()
    public void singIn() throws InterruptedException {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //EspressoIdlingResource.increment();
        mAuth.signInWithEmailAndPassword("studenteprova@studenti.unisannio.it", "passwordProva").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Login eseguito con successo");
                    //EspressoIdlingResource.decrement();
                }else{
                    Log.d(TAG, "Login fallito");
                    //EspressoIdlingResource.decrement();
                }
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(1000);

    }

    @After()
    public void logOut() throws InterruptedException {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Log.d(TAG, "LogOut eseguito con successo");
    }

    // Test iscrizione corso con inserimento codice di un corso non esistente
    @Test()
    public void RichiestaIscrizioneActivityTestCodiceCorsoNonEsistente() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("codiceSbagliatoTest"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("codiceSbagliatoTest"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button2), withText("RICHIEDI ISCRIZIONE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        //Thread.sleep(5000);

        ViewInteraction textView = onView(withText("Il codice inserito non appartiene a nessun corso esistente"));
        textView.check(matches(isDisplayed()));


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button3), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatButton3.perform(scrollTo(), click());
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