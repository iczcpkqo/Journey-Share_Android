package com.journey;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> rActivityTestRule = new ActivityScenarioRule<>(RegisterActivity.class);


    @After
    public void setUp() throws Exception {
        rActivityTestRule = null;
    }

    @Test
    public void signup() throws Exception{
        Espresso.onView(withId(R.id.username)).perform(typeText("test"));
        Espresso.onView(withId(R.id.email)).perform(typeText("test@test.com"));
        Espresso.onView(withId(R.id.password)).perform(typeText("test123"));
//        Espresso.onView(withId(R.id.confirm)).perform(typeText("test123"));
        Espresso.onView(withId(R.id.phone)).perform(typeText("123456789"));
        Espresso.onView(withId(R.id.age)).perform(typeText("20"));
        Espresso.onView(withId(R.id.gender)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Female"))).perform(click());


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.useEmulator("10.0.2.2", 8080);
        firestore.setFirestoreSettings(settings);
    }
}