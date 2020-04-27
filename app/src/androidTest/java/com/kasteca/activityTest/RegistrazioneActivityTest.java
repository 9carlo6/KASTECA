package com.kasteca.activityTest;


import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrazioneActivityTest {

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

        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome));
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome));

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(typeText(calendar.get(Calendar.DATE)+""+emailDocente));

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password));

        onView(withId(R.id.buttonCreateAccount)).perform(click());
    }


    @Test
    public void registrazioneStudenteTest() throws InterruptedException{

        ViewInteraction textNome= onView(withId(R.id.editTextNome)).perform(typeText(nome));
        ViewInteraction textCognome= onView(withId(R.id.editTextCognome)).perform(typeText(cognome));

        //Impostiamo la data odierna per la creazione della email
        date=new Date();
        calendar= Calendar.getInstance();
        calendar.setTime(date);
        ViewInteraction textEmail= onView(withId(R.id.editTextEmail)).perform(typeText(calendar.get(Calendar.DATE)+""+emailStudente));

        ViewInteraction textPassword= onView(withId(R.id.editTextPassword)).perform(typeText(password));

        onView(withId(R.id.switchDocenteStudente)).perform(click());
        Thread.sleep(500);

        onView(withId(R.id.editTextMatricola)).perform(typeText(matricola));

        onView(withId(R.id.buttonCreateAccount)).perform(click());
    }



}
