package fr.iut2.saeprojet.exempleNOTUSE.api;


import fr.iut2.saeprojet.exempleNOTUSE.entity.MultipleResource;
import fr.iut2.saeprojet.exempleNOTUSE.entity.User;
import fr.iut2.saeprojet.exempleNOTUSE.entity.UserList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIUserService {
    @GET("/api/unknown")
    Call<MultipleResource> doGetListResources();

    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") int page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}