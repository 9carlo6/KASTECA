package com.kasteca.activityTest;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.kasteca.R;
import com.kasteca.activity.NewPostActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class NewPostActivityTestWithoutUpload {
    @Rule
    public IntentsTestRule<NewPostActivity> intentsTestRule = new IntentsTestRule<>(NewPostActivity.class);


    // Verifica che Activity visibile, con tutti gli elementi grafici previsti
    @Test
    public void test_isActivityOnView(){
        onView(withId(R.id.new_post_layout)).check(matches(isDisplayed()));
        closeSoftKeyboard();

        onView(withId(R.id.text_post_Edit_Text)).check(matches(isDisplayed()));
        onView(withId(R.id.link_Edit_Text)).check(matches(isDisplayed()));
        onView(withId(R.id.pdfButton)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadButton)).check(matches(isDisplayed()));
        onView(withId(R.id.uri_pdf)).check(matches(isDisplayed()));
        onView(withId(R.id.tags_spinner)).check(matches(isDisplayed()));
    }


    // Verifica che non viene permesso l'upload di un post privo di testo
    @Test
    public void test_isUploadDenied_WithEmptyText(){
        closeSoftKeyboard();
        onView(withId(R.id.uploadButton)).perform(click());
        onView(withText(R.string.missing_data)).check(matches(isDisplayed()));
        onView(withText(R.string.Dialog_neutral_button_login_failed)).perform(click());
        onView(withText(R.string.missing_data)).check(doesNotExist());
    }


    // Verifica che caricamento del pdf dalla memoria all'Activity
    @Test
    public void test_validateFileIntent(){
        closeSoftKeyboard();
        Matcher<Intent> expectedIntent = allOf(
                hasAction(Intent.ACTION_GET_CONTENT),
                hasType("application/pdf")
        );

        Instrumentation.ActivityResult activityResult = createFilePickActivityResultStub();
        intending(expectedIntent).respondWith(activityResult);

        onView(withId(R.id.pdfButton)).perform(click());
        intended(expectedIntent);

        onView(withId(R.id.uri_pdf)).check(matches(withText((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + "/mypdf/test.pdf")));

        File fdelete = new File((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + "/mypdf/test.pdf");
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e(TAG, "file Deleted");
            } else {
                Log.e(TAG,"file not Deleted");
            }
        }
    }

    // Metodo per simulare la selezione del file pdf
    private Instrumentation.ActivityResult createFilePickActivityResultStub(){


        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Testo.", 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
        // write the document content
        String directory_path = (getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
        }
        // close the document
        document.close();



        //Context context = intentsTestRule.getActivity().getBaseContext();
        Log.e(TAG, "pdf uri: " + targetPdf);

        Intent resultIntent = new Intent();
        resultIntent.setData(Uri.parse(targetPdf));
        return new Instrumentation.ActivityResult(RESULT_OK, resultIntent);
    }
}
