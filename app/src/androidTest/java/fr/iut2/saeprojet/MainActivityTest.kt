package fr.iut2.saeprojet

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.CoreMatchers.endsWith
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4ClassRunner::class)
internal class MainActivityTest {
    fun connect(username : String="b", password : String="b"){
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.username)).perform(typeText(username));
        onView(withId(R.id.password)).perform(typeText(password));
        onView(withId(R.id.login)).perform(click());
        sleep(5000);
    }
    /**
     * Test de la navigation entre le login et la page principale(main)
     */
    @Test
    fun test_navFromLoginToMainAndBack() {

        connect();
        onView(withId(R.id.prenom)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.username)).check(matches(isDisplayed()));
    }


    /**
     * Vérifie que le message de bienvenue a le prénom de la personne connectée
     */
    @Test
    fun test_messageBienvenue() {
        connect("khonnual","123");
        onView(withId(R.id.prenom)).check(matches(withText("Bienvenue\nAlain")));
    }


}