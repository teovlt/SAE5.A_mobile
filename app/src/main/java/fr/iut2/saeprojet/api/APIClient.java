package fr.iut2.saeprojet.api;

import android.util.Log;

import fr.iut2.saeprojet.StageAppActivity;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.CompteEtudiantRequest;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.EntreprisesResponse;
import fr.iut2.saeprojet.entity.EtatCandidaturesResponse;
import fr.iut2.saeprojet.entity.EtatOffresResponse;
import fr.iut2.saeprojet.entity.EtatRecherchesResponse;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreConsultee;
import fr.iut2.saeprojet.entity.OffreConsulteeRequest;
import fr.iut2.saeprojet.entity.OffreRetenue;
import fr.iut2.saeprojet.entity.OffreRetenueRequest;
import fr.iut2.saeprojet.entity.OffresConsulteesResponse;
import fr.iut2.saeprojet.entity.OffresResponse;
import fr.iut2.saeprojet.entity.OffresRetenuesResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //https://square.github.io/retrofit/?utm_source=developer.android.com&utm_medium=referral
    //https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial

    private static final String BASE_URL = "http://192.168.141.112:8000/";
    //
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    // Instance retrofit
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static APIService getAPIService() {
        return retrofit.create(APIService.class);
    }

    public static void getCompteEtudiant(StageAppActivity activity, long id, ResultatAppel<CompteEtudiant> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CompteEtudiant> call = apiInterface.doGetCompteEtudiant(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void getEtudiant(StageAppActivity activity, long id, ResultatAppel<Etudiant> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Etudiant> call = apiInterface.doGetEtudiant(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void updateCompteEtudiant(StageAppActivity activity, long id, CompteEtudiantRequest compteEtudiantRequest, ResultatAppel<CompteEtudiant> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CompteEtudiant> call = apiInterface.doUpdateCompteEtudiant(getBearer(activity), id, compteEtudiantRequest);
        APIClient.doCall(call, cllbck);
    }

    public static void getOffre(StageAppActivity activity, long id, ResultatAppel<Offre> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Offre> call = apiInterface.doGetOffre(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void getOffres(StageAppActivity activity, ResultatAppel<OffresResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresResponse> call = apiInterface.doGetOffres(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void getOffresConsultees(StageAppActivity activity, ResultatAppel<OffresConsulteesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresConsulteesResponse> call = apiInterface.doGetOffresConsultees(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void createOffreConsultee(StageAppActivity activity, OffreConsulteeRequest offreConsulteeRequest, ResultatAppel<OffreConsultee> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffreConsultee> call = apiInterface.doCreateOffreConsultee(getBearer(activity), offreConsulteeRequest);
        APIClient.doCall(call, cllbck);
    }

    public static void getOffresRetenues(StageAppActivity activity, ResultatAppel<OffresRetenuesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresRetenuesResponse> call = apiInterface.doGetOffresRetenues(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void createOffreRetenue(StageAppActivity activity, OffreRetenueRequest offreRetenueRequest, ResultatAppel<OffreRetenue> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffreRetenue> call = apiInterface.doCreateOffreRetenue(getBearer(activity), offreRetenueRequest);
        APIClient.doCall(call, cllbck);
    }

    public static void removeOffreRetenue(StageAppActivity activity, long id, ResultatAppel<OffreRetenue> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffreRetenue> call = apiInterface.doRemoveOffreRetenue(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void getCandidatures(StageAppActivity activity, ResultatAppel<CandidaturesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CandidaturesResponse> call = apiInterface.doGetCandidatures(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }


    public static void createCandidature(StageAppActivity activity, CandidatureRequest candidature, ResultatAppel<Candidature> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Candidature> call = apiInterface.doCreateCandidature(getBearer(activity), candidature);
        APIClient.doCall(call, cllbck);
    }

    public static void updateCandidature(StageAppActivity activity, long id, CandidatureRequest candidature, ResultatAppel<Candidature> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Candidature> call = apiInterface.doUpdateCandidature(getBearer(activity), id, candidature);
        APIClient.doCall(call, cllbck);
    }

    public static void removeCandidature(StageAppActivity activity, long id, ResultatAppel<Candidature> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Candidature> call = apiInterface.doRemoveCandidature(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void getEntreprise(StageAppActivity activity, long id, ResultatAppel<Entreprise> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Entreprise> call = apiInterface.doGetEntreprise(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    public static void getEntreprises(StageAppActivity activity, ResultatAppel<EntreprisesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<EntreprisesResponse> call = apiInterface.doGetEntreprises(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void getEtatRecherches(StageAppActivity activity, ResultatAppel<EtatRecherchesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<EtatRecherchesResponse> call = apiInterface.doGetEtatRecherches(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void getEtatOffres(StageAppActivity activity, ResultatAppel<EtatOffresResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<EtatOffresResponse> call = apiInterface.doGetEtatsOffres(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static void getEtatCandidatures(StageAppActivity activity, ResultatAppel<EtatCandidaturesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<EtatCandidaturesResponse> call = apiInterface.doGetEtatCandidatures(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    public static long getCandidatureId(String _id) {
        return Long.parseLong(_id.replaceFirst("^/api/candidatures/", ""));
    }

    public static long getEntrepriseId(String _id) {
        return Long.parseLong(_id.replaceFirst("^/api/entreprises/", ""));
    }

    private static String getBearer(StageAppActivity activity) {
        return "Bearer " + activity.getToken();
    }

    private static <T> void doCall(Call<T> call, ResultatAppel<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (callback != null) {
                    callback.traiterResultat(response.body());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                call.cancel();
                Log.e("TAG", t.getMessage());
                if (callback != null) {
                    callback.traiterErreur();
                }
            }
        });
    }
}
