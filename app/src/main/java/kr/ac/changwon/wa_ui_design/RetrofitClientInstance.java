package kr.ac.changwon.wa_ui_design;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = Constants.BASE_URL;
    private static String authToken = ""; // 초기화된 authToken

    public static void setAuthToken(String token) {
        authToken = token; // authToken 값을 설정
    }

    public static String getAuthToken() {
        Log.d("RetrofitClientInstance", "현재 authToken 값: " + authToken);
        return authToken;
    }


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + authToken); // authToken 사용
                        Request request = requestBuilder.build();
                        Log.d("RetrofitClientInstance", "Authorization 헤더 추가: " + request.headers());
                        return chain.proceed(request);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
