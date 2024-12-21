package kr.ac.changwon.wa_ui_design;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/join")
    Call<Void> registerUser(@Body User user);

    @POST("/login")
    Call<ResponseBody> loginUser(@Body User user);

    @GET("/pets")
    Call<List<Pet>> getUserPets(@Query("user_id") String userId);

    @GET("/pets")
    Call<List<Pet>> getPets(@Query("user_id") String userId);

    @POST("/pets")
    Call<Void> registerPet(@Body Pet pet);

    @POST("/lost_pets")
    Call<Void> registerLostPet(@Body LostPet lostPet);


    @POST("/board/posts")
    Call<Void> createBoardPost(@Body BoardPost boardPost);

    @GET("/board/posts")
    Call<List<BoardPost>> getPosts();

    @Multipart
    @POST("/upload/photo")
    Call<ResponseBody> uploadPhoto(
            @Part MultipartBody.Part photo,
            @Part("description") RequestBody description
    );

}
