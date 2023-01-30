package fr.iut2.saeprojet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Offre {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("intitule")
    public String intitule;
    @SerializedName("descriptif")
    @Nullable
    public String descriptif;
    @SerializedName("dateDepot")
    public String dateDepot;
    @SerializedName("parcours")
    @Nullable
    public String parcours;
    @SerializedName("motsCles")
    @Nullable
    public String motsCles;
    @SerializedName("urlPieceJointe")
    @Nullable
    public String urlPieceJointe;
    @SerializedName("etatOffre")
    public String etatOffre;
    @SerializedName("entreprise")
    public String entreprise;
    @SerializedName("offreConsultees")
    public List<String> offreConsultees;
    @SerializedName("offreRetenues")
    public List<String> offreRetenues;
    @SerializedName("candidatures")
    public List<String> candidatures;
}
