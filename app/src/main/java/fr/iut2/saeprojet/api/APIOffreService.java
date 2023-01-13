package fr.iut2.saeprojet.api;


import fr.iut2.saeprojet.entity.OffreList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIOffreService {
    @GET("/api/offres")
    Call<OffreList> doGetOffreList();

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