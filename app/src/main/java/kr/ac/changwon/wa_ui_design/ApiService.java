package kr.ac.changwon.wa_ui_design;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/join") // Flask 서버의 엔드포인트 URL
    Call<Void> registerUser(@Body User user);

    @POST("/login") // Flask 서버의 '/login' 엔드포인트와 매핑
    Call<ResponseBody> loginUser(@Body User user);

    @POST("/pets")
    Call<Void> registerPet(@Body Pet pet);

    @GET("/pets")
    Call<List<Pet>> getPets(@Query("user_id") String userId);

    // 게시물 등록 엔드포인트
    @POST("/board/posts") // Flask 서버의 '/board/posts' 엔드포인트와 매핑
    Call<Void> createBoardPost(@Body BoardPost boardPost);

    // 게시물 조회 엔드포인트 (옵션)
    @GET("/board/posts")
    Call<List<BoardPost>> getPosts();
}
