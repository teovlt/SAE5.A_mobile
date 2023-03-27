package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class EtudiantRequest {
    @SerializedName("numeroINE")
    public String numeroINE;
    @SerializedName("nom")
    public String nom;
    @SerializedName("prenom")
    public String prenom;
    @SerializedName("email")
    public String email;
}
