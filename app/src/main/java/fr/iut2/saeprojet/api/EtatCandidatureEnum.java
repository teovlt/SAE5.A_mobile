package fr.iut2.saeprojet.api;

public enum EtatCandidatureEnum {
    OFFRE_RETENUE("/api/etat_candidatures/1", "Candidature à envoyer"),
    ATTENTE_REPONSE("/api/etat_candidatures/2", "En attente de réponse"),
    ATTENTE_RDV("/api/etat_candidatures/4", "En attente du rendez-vous"),
    ATTENTE_ENTRETIEN("/api/etat_candidatures/5", "En atttente de l'entretien"),
    ACCEPTEE("/api/etat_candidatures/6", "Candidature acceptée"),
    REFUSEE("/api/etat_candidatures/3", "Candidature refusée");



    private final String _id;
    private final String _nom;

    EtatCandidatureEnum(String _id, String nom) {
        this._id = _id;
        this._nom = nom;
    }

    public String get_id() {
        return this._id;
    }

    public String get_nom() {
        return _nom;
    }

    public String toString() {
        return this._nom;
    }
}
