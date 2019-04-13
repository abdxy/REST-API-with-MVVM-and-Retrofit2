package com.example.food_api.requests;

import android.support.constraint.Constraints;

import com.example.food_api.util.constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerGenerator {
    private static Retrofit.Builder retrofitBuilder= new Retrofit.Builder()
            .baseUrl(constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit=retrofitBuilder.build();
    private static RecipeApi recipeApi=retrofit.create(RecipeApi.class);
    public static RecipeApi getRecipeApi(){
        return  recipeApi;
    }



}
