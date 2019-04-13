package com.example.food_api.requests;

import com.example.food_api.requests.responses.RecipeResopnse;
import com.example.food_api.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page

    );
    @GET("api/get")
    Call<RecipeResopnse> getRecipe(
            @Query("key") String key,
            @Query("rId") String id
    );

}
