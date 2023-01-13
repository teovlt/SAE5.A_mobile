package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class Offre {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("intitule")
    public String intitule;
    @SerializedName("urlPieceJointe")
    @Nullable
    public String urlPieceJointe;

    public Offre(String intitule, String urlPieceJointe) {
        this.intitule = intitule;
        this.urlPieceJointe = urlPieceJointe;
    }

}
