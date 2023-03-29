package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class OffreRetenueRequest {
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
}
