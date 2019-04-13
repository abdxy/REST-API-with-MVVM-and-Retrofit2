package com.example.food_api.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.food_api.models.Recipe;
import com.example.food_api.repositories.RecipeRepostory;

public class RecipeViewModel extends ViewModel {
    LiveData<Recipe> recipe;
    String id;
    RecipeRepostory recipeRepostory;
    boolean isDone;


    public RecipeViewModel (){
        recipeRepostory=RecipeRepostory.getInstance();
        isDone=false;

    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public  LiveData<Boolean> getGetRecipeState() {
        return recipeRepostory.getGetRecipeState();
    }
    public String getId() {
        return id;
    }

    public LiveData<Recipe> getRecipe() {
        return recipeRepostory.getRecipe();
    }
    public void searchRecipeByIdApi(String id) {
        this.id=id;
        recipeRepostory.searchRecipeByIdApi(id);
    }
}
