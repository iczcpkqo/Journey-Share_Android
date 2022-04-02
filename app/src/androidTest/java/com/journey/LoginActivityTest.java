package com.journey;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<LoginActivity> mIntentsRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @org.junit.Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void login_button() throws Exception{
        Espresso.onView(withId(R.id.username)).perform(typeText("test@test.com"));
        Espresso.onView(withId(R.id.password)).perform(typeText("test123"));
        Espresso.onView(withId(R.id.login_in)).perform(click());
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.useEmulator("10.0.2.2", 9099);
        mAuth.signInWithEmailAndPassword("test@test.com", "test123")
            .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = authResult.getUser();
                    assertNotNull(user);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                    assertThat(errorCode, CoreMatchers.containsString("ERROR"));
                }
            });
    }

    @Test
    public void register_button() throws  Exception{
        Espresso.onView(withId(R.id.register)).perform(click());
        intended(hasComponent(hasShortClassName(".RegisterActivity")));
    }
}