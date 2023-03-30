package fr.iut2.saeprojet

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import fr.iut2.saeprojet.StageAppActivity.getContext
import fr.iut2.saeprojet.api.APIClient
import fr.iut2.saeprojet.api.ResultatAppel
import fr.iut2.saeprojet.entity.OffresResponse
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.TestCase.*
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.not
import java.net.HttpURLConnection
import java.net.URL

@RunWith(AndroidJUnit4ClassRunner::class)
internal class MainActivityTest {

    /**
     * Il se peut que les tests ne passent pas si la connexion est mauvaise, augmenter ou réduire le sleep_time si nécessaire
     */
    companion object {
        const val SLEEP_TIME = 5000L // secondes
    }
    fun connect(){
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.username)).perform(typeText("khonnual"));
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.login)).perform(click());
        sleep(SLEEP_TIME);
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
        connect();
        onView(withId(R.id.prenom)).check(matches(withText("Bienvenue\nAlain")));
    }
    /**
     * Vérifie que la dernière connexion affichée sur la page principale correspond bien
     */
    @Test
    fun test_derniereConnexion() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        val date = Calendar.getInstance().time
        val dateFormatee = SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);

        onView(withId(R.id.username)).perform(typeText("khonnual"));
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.login)).perform(click());
        sleep(SLEEP_TIME);
        pressBack();
        onView(withId(R.id.username)).perform(typeText("khonnual"));
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.login)).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.derniere_connexion)).check(matches(withText("Dernière connexion le $dateFormatee")));
    }

    /**
     * Vérifie que l'indicateur du nombre de page est correct dans la liste des offres
     * Ne passera pas si pas assez d'offre disponible
     */
    @Test
    fun test_nbPagesListeOffres() {
        connect();
        onView(withId(R.id.details_offres)).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.indic_page)).check(matches(withSubstring("1 / ")));
        onView(withId(R.id.buttonNext)).perform(click());
        sleep(1000);
        onView(withId(R.id.indic_page)).check(matches(withSubstring("2 / ")));
        onView(withId(R.id.buttonPrec)).perform(click());
        sleep(1000);
        onView(withId(R.id.indic_page)).check(matches(withSubstring("1 / ")));
    }
    /**
     * Test de la navigation entre la liste des offres et la page de l'offre cliqué
     */
    @Test
    fun test_navigationFromListeOffresToOffre(){
        connect();
        onView(withId(R.id.details_offres)).perform(click());
        sleep(SLEEP_TIME);
        onData(anything()).inAdapterView(withId(R.id.offres_list)).atPosition(0).perform(click());
        sleep(1000);
        onView(withId(R.id.liste_offres)).check(matches(isDisplayed()));
    }

    /**
     *  Test de la navigation entre une offre et la page de candidature de l'offre cliqué
     */
    @Test
    fun test_navigationFromOffreToCandidature(){
        connect();
        onView(withId(R.id.details_offres)).perform(click());
        sleep(SLEEP_TIME);
        onData(anything()).inAdapterView(withId(R.id.offres_list)).atPosition(0).perform(click());
        sleep(1000);
        onView(withId(R.id.buttonCandidature)).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.intitule)).check(matches(isDisplayed()));

    }
    /**
     *  Test de la navigation entre une candidature et sa page de mise à jour
     */
    @Test
    fun test_navigationFromCandidatureToCandidatureEdit(){
        connect();
        onView(withId(R.id.details_candidatures)).perform(click());
        sleep(SLEEP_TIME);
        onData(anything()).inAdapterView(withId(R.id.candidatures_en_cours_list)).atPosition(0).perform(click());
        sleep(1000);
        onView(withId(R.id.mettreajour)).perform(click());
        sleep(1000);
        onView(withId(R.id.intitule)).check(matches(isDisplayed()));

    }
}