package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EtatRecherche {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public long id;
    @SerializedName("etat")
    public String etat;
    @SerializedName("descriptif")
    public String descriptif;
    @SerializedName("comptesEtudiant")
    public List<String> comptesEtudiant;
}
