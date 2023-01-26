package fr.iut2.saeprojet.api;


import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.CandidatureList;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.EtudiantList;
import fr.iut2.saeprojet.entity.LoginResponse;
import fr.iut2.saeprojet.entity.OffreList;
import fr.iut2.saeprojet.entity.CompteEtudiantList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("api/auth")
    Call<LoginResponse> login(@Body Auth auth);

    @GET("/api/compte_etudiants")
    Call<CompteEtudiantList> doGetCompteEtudiants(@Header("Authorization") String token);

    @GET("/api/compte_etudiants/{id}")
    Call<CompteEtudiant> doGetCompteEtudiant(@Header("Authorization") String token, @Path("id") long id);

    @GET("/api/etudiants")
    Call<EtudiantList> doGetEtudiants(@Header("Authorization") String token);

    @GET("/api/etudiants/{id}")
    Call<Etudiant> doGetEtudiant(@Header("Authorization") String token, @Path("id") long id);

    @GET("/api/offres")
    Call<OffreList> doGetOffres(@Header("Authorization") String token);

    @GET("/api/candidatures")
    Call<CandidatureList> doGetCandidatures(@Header("Authorization") String token);

//    @POST("/api/offres")
//    Call<User> createUser(@Body User user);
//
//    @GET("/api/users?")
//    Call<UserList> doGetUserList(@Query("page") int page);
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}