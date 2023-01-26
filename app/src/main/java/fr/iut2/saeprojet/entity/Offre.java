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
    @SerializedName("dateDepot")
    public String dateDepot;
    @SerializedName("parcours")
    public String parcours;
    @SerializedName("motsCles")
    public String motsCles;
    @SerializedName("urlPieceJointe")
    @Nullable
    public String urlPieceJointe;

}
