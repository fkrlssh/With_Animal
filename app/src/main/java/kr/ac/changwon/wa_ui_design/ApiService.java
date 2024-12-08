package kr.ac.changwon.wa_ui_design;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/join") // Flask 서버의 엔드포인트 URL
    Call<Void> registerUser(@Body User user);

    @POST("/login") // Flask 서버의 '/login' 엔드포인트와 매핑
    Call<ResponseBody> loginUser(@Body User user);
}
