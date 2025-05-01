package com.example.studentcv;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiApiService {

    @Headers({"Content-Type: application/json"})
    @POST("v1/models/gemini-2.0-flash-exp-image-generation:generateText")
    Call<GeminiResponse> generateCV(@Body GeminiRequest request);
}