package kr.ac.changwon.wa_ui_design;


import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static String BASE_URL = Constants.BASE_URL;
    private static String authToken = "";

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + authToken);
                        Request request = requestBuilder.build();

                        Log.d("RetrofitClientInstance", "요청 URL: " + BASE_URL);

                        try {
                            return chain.proceed(request);
                        } catch (Exception e) {
                            Log.e("RetrofitClientInstance", "서버 연결 실패: " + e.getMessage());
                            throw e;
                        }
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    // ngrok URL 직접 확인 (서버 연결 확인)
    public static void validateNgrokUrl() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("RetrofitClientInstance", "ngrok URL 유효하지 않음: " + e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("RetrofitClientInstance", "ngrok URL 연결 성공");
                } else {
                    Log.e("RetrofitClientInstance", "ngrok URL 응답 실패: " + response.code());
                }
            }
        });
    }
}
