package com.example.food_api.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.food_api.models.Recipe;
import com.example.food_api.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepostory {
    private static RecipeRepostory instance;
    private RecipeApiClient recipeApiClient;
    private MutableLiveData<Boolean> isQueryExh=new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> recipes =new MediatorLiveData<>();
    public static RecipeRepostory getInstance() {
        if (instance == null)
            instance = new RecipeRepostory();
        return instance;


    }

    private RecipeRepostory() {
        recipeApiClient = RecipeApiClient.getInstance();
        initMediator();
    }

    private void initMediator() {
        recipes.addSource(recipeApiClient.getRecipes(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> mrecipes) {
                if(mrecipes!=null){
                recipes.setValue(mrecipes);
                doneQuery(mrecipes);
                }else{
                    //chaching
                    doneQuery(null);
                }

            }
        });
    }

    public LiveData<Boolean> IsQueryExh() {
        return isQueryExh;
    }

    private void doneQuery(List<Recipe> recipes){
        if(recipes!=null){
        if(recipes.size()%30!=0||recipes.size()==0){
            isQueryExh.setValue(true);

        }}else isQueryExh.setValue(true);
    }

    public  LiveData<Boolean> getGetRecipesState() {
        return recipeApiClient.getGetRecipesState();
    }

    public  LiveData<Boolean> getGetRecipeState() {
        return recipeApiClient.getGetRecipeState();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void searchRecipeApi(String query, int pageNo) {
        isQueryExh.setValue(false);
        recipeApiClient.searchRecipeApi(query, pageNo);
    }
    public LiveData<Recipe> getRecipe() {
        return recipeApiClient.getRecipe();
    }

    public void searchRecipeByIdApi(String id) {

       recipeApiClient.searchRecipeByIdApi(id);
    }
}
