package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OffreList {

    @SerializedName("@context")
    public String context;
    @SerializedName("@id")
    public String id;
    @SerializedName("@type")
    public String type;
    @SerializedName("hydra:totalItems")
    public Integer totalItems;
    @SerializedName("hydra:member")
    public List<Offre> offres = new ArrayList();
}
