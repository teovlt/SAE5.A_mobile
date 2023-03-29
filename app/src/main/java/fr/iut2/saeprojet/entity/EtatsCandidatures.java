package fr.iut2.saeprojet.entity;

import java.util.HashMap;
import java.util.Map;

public class EtatsCandidatures {

/*    public static final Map<String, Long> etatsCandidature = new HashMap<String, Long>() {
        {
            put("Candidature à envoyer", 1L);
            put("En attente de réponse", 2L);
            put("Candidature Refusée", 3L);
            put("En attente du rendez-vous", 4L);
            put("Attente Entretien", 5L);
            put("Acceptée", 6L);
        }
    };*/

    public static final Map<Long, String> etatsCandidatureInverse = new HashMap<Long, String>() {
        {
            put(1L, "Candidature à envoyer");
            put(2L, "En attente de réponse");
            put(3L, "Candidature Refusée");
            put(4L, "En attente du rendez-vous");
            put(5L, "En atttente de l'entretien");
            put(6L, "Candidature Acceptée");
        }
    };

}
