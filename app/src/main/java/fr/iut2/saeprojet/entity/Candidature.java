package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class Candidature {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
    @SerializedName("typeAction")
    public String typeAction;
    @SerializedName("dateAction")
    public String dateAction;
    @SerializedName("etatCandidature")
    public String etatCandidature;

}
