package com.kasteca.activityTest;


import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


import com.kasteca.R;
import com.kasteca.activity.RegistrationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrazioneActivityDocenteTest {

    private String nome="testName";
    private String cognome= "testSurname";
    private String password= "test";
    private String matricola="863965";
    private String emailStudente= "@studenti.unisannio.it" ;
    private String emailDocente= "@unisannio.it";

    //Per la corretta eseguzione del test
    private Date date;
    private Calendar calendar;


    //Dopo il testing della registrazione dovrebbe essere eliminato l'utente
    //per evitare che il test fallisca la email inserita si baserà sulla data di eseguzione del test

    @Rule
    public ActivityTestRule<RegistrationActivity> mActivityTestRule = new ActivityTestRule<>(RegistrationActivity.class);

    @Test
    public void registrazioneDocenteTest() throws InterruptedException{

        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText(calendar.get(Calendar.DATE)+""+emailDocente), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());
    }




    @Test
    public void fallimentoRegistrazioneDocenteTestNoName() throws InterruptedException{
        //Dobbiamo valutare tutte le possibili combinazioni che portano al messaggio di errore

        //Prima combinazione: nome non presente
        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(""), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText(calendar.get(Calendar.MILLISECOND)+""+emailDocente), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());

        //Attendiamo l'apparizione del messaggio di errore
        Thread.sleep(500);
        //Verifichiamo la comparsa del messaggio di errore
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

    }

    @Test
    public void fallimentoRegistrazioneDocenteTestNoSurname() throws InterruptedException{
        //Seconda combinazione: cognome non presente //////////////////////////////////////////
        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(""), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText(calendar.get(Calendar.MILLISECOND)+""+emailDocente), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());

        //Attendiamo l'apparizione del messaggio di errore
        Thread.sleep(500);
        //Verifichiamo la comparsa del messaggio di errore
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void fallimentoRegistrazioneDocenteTestNoPassword() throws InterruptedException{
        //Terza combinazione: password non presente //////////////////////////////////////////
        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText(calendar.get(Calendar.MILLISECOND)+""+emailDocente), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(""), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());

        //Attendiamo l'apparizione del messaggio di errore
        Thread.sleep(500);
        //Verifichiamo la comparsa del messaggio di errore
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void fallimentoRegistrazioneDocenteTestNoEmail() throws InterruptedException{
        //Quarta combinazione: email non presente //////////////////////////////////////////
        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText(""), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());

        //Attendiamo l'apparizione del messaggio di errore
        Thread.sleep(500);
        //Verifichiamo la comparsa del messaggio di errore
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void fallimentoRegistrazioneDocenteTestWrongEmail() throws InterruptedException{
        //Quinta combinazione: email presente ma con dominio non corretto //////////////////////////////////////////
        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome), ViewActions.closeSoftKeyboard());
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome), ViewActions.closeSoftKeyboard());

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(replaceText("testmailFail"), ViewActions.closeSoftKeyboard());

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonCreateAccount)).perform(click());

        //Attendiamo l'apparizione del messaggio di errore
        Thread.sleep(500);
        //Verifichiamo la comparsa del messaggio di errore
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }
}
