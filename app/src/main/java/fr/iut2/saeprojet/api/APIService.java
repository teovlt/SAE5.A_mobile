package fr.iut2.saeprojet.api;


import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.EtudiantsResponse;
import fr.iut2.saeprojet.entity.LoginResponse;
import fr.iut2.saeprojet.entity.OffresResponse;
import fr.iut2.saeprojet.entity.CompteEtudiantResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @POST("api/auth")
    Call<LoginResponse> login(@Body Auth auth);

    @GET("/api/compte_etudiants")
    Call<CompteEtudiantResponse> doGetCompteEtudiants(@Header("Authorization") String token);

    @GET("/api/compte_etudiants/{id}")
    Call<CompteEtudiant> doGetCompteEtudiant(@Header("Authorization") String token, @Path("id") long id);

    @GET("/api/etudiants")
    Call<EtudiantsResponse> doGetEtudiants(@Header("Authorization") String token);

    @GET("/api/etudiants/{id}")
    Call<Etudiant> doGetEtudiant(@Header("Authorization") String token, @Path("id") long id);

    @GET("/api/offres")
    Call<OffresResponse> doGetOffres(@Header("Authorization") String token);

    @GET("/api/candidatures")
    Call<CandidaturesResponse> doGetCandidatures(@Header("Authorization") String token);

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