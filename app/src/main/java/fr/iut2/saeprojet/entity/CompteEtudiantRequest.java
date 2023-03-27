package fr.iut2.saeprojet.entity;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompteEtudiantRequest {
    @SerializedName("login")
    public String login;
    @SerializedName("roles")
    public List<String> roles;
    @SerializedName("password")
    public String password;
    @SerializedName("parcours")
    public String parcours;
    @SerializedName("derniereConnexion")
    @Nullable
    public String derniereConnexion;
    @SerializedName("etatRecherche")
    public String etatRecherche;
    @SerializedName("etudiant")
    public String etudiant;
    @SerializedName("offreConsultees")
    public List<String> offreConsultees;
    @SerializedName("offreRetenues")
    public List<String> offreRetenues;
    @SerializedName("candidatures")
    public List<String> candidatures;

}
