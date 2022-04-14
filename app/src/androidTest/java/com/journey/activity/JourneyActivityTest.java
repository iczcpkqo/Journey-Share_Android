package com.journey.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.journey.R;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class JourneyActivityTest {
    @Rule
    public ActivityScenarioRule<JourneyActivity> jActivityTestRule = new ActivityScenarioRule<>(JourneyActivity.class);

    @Rule
    public ActivityScenarioRule<JourneyActivity> jIntentsRule =
            new ActivityScenarioRule<>(JourneyActivity.class);


    @After
    public void tearDown(){
        jIntentsRule = null;
        jActivityTestRule = null;
    }

    @Test
    public  void testNav(){

        onView(withId(R.id.journeyFragment)).check(matches(isDisplayed()));
    }

}