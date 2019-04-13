package com.example.food_api.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.renderscript.ScriptGroup;
import android.util.Log;

import com.example.food_api.AppExecutors;
import com.example.food_api.models.Recipe;
import com.example.food_api.repositories.RecipeRepostory;
import com.example.food_api.requests.responses.RecipeResopnse;
import com.example.food_api.requests.responses.RecipeSearchResponse;
import com.example.food_api.util.constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {
    private static final String TAG = "RecipeApiClient";
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<Recipe> recipe;
    MutableLiveData<Boolean> getRecipeState ;
    MutableLiveData<Boolean> getRecipesState ;


    public LiveData<Boolean> getGetRecipesState() {
        return getRecipesState;
    }

    public LiveData<Boolean> getGetRecipeState() {
        return getRecipeState;
    }

    public static RecipeApiClient getInstance() {
        if (instance == null)
            instance = new RecipeApiClient();
        return instance;


    }

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
        recipe = new MutableLiveData<>();
        getRecipeState = new MutableLiveData<>();
        getRecipesState = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
//RetrieveRecipeByIdrunnable

    public void searchRecipeByIdApi(String id) {

        final Future handler = AppExecutors.getInstance().getmNetworkIo().submit(
                new RetrieveRecipeByIdrunnable(id));
        getRecipeState.setValue(false);
        AppExecutors.getInstance().getmNetworkIo().schedule(new Runnable() {
            @Override
            public void run() {
                getRecipeState.postValue(true);
                handler.cancel(true);

            }
        }, constants.NETWORK_TIME_OUT, TimeUnit.MILLISECONDS);

    }

    public void searchRecipeApi(String query, int pageNo) {

        final Future handler = AppExecutors.getInstance().getmNetworkIo().submit(
                new RetrieveReciperunnable(query, pageNo));

        getRecipesState.setValue(false);
        AppExecutors.getInstance().getmNetworkIo().schedule(new Runnable() {
            @Override
            public void run() {
                getRecipesState.postValue(true);
                handler.cancel(true);


            }
        }, constants.NETWORK_TIME_OUT, TimeUnit.MILLISECONDS);

    }

    private class RetrieveReciperunnable implements Runnable {
        private String query;
        private int pageNo;
        private boolean cancelRequest;

        public RetrieveReciperunnable(String query, int pageNo) {
            this.query = query;
            this.pageNo = pageNo;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNo).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {

                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNo == 1)
                        recipes.postValue(list);
                    else {
                        List<Recipe> curRecipe = recipes.getValue();
                        curRecipe.addAll(list);
                        recipes.postValue(curRecipe);

                    }

                } else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    recipes.postValue(null);

                }
            } catch (IOException e) {
                Log.e(TAG, "run: " + e.getMessage());
                recipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNo) {
            return ServerGenerator.getRecipeApi()
                    .searchRecipe(constants.API_KEY, query, String.valueOf(pageNo));

        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancel search request");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeByIdrunnable implements Runnable {
        private String id;
        private boolean cancelRequest;


        public RetrieveRecipeByIdrunnable(String id) {
            this.id = id;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(id).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {


                    Recipe mrecipe = (((RecipeResopnse) response.body()).getRecipe());
                    recipe.postValue(mrecipe);

                } else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    recipes.postValue(null);
                }
            } catch (IOException e) {
                Log.e(TAG, "run: " + e.getMessage());
                recipes.postValue(null);

            }

        }

        private Call<RecipeResopnse> getRecipe(String id) {
            return ServerGenerator.getRecipeApi()
                    .getRecipe(constants.API_KEY, id);

        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancel search request");
            cancelRequest = true;
        }
    }
}
