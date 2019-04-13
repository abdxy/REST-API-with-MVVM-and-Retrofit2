package com.example.food_api.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.food_api.adapters.RecipeRecyclerApapter;
import com.example.food_api.models.Recipe;
import com.example.food_api.repositories.RecipeRepostory;

import java.util.List;



public class RecipeListViewModel extends ViewModel {

    RecipeRepostory recipeRepostory;
    private boolean mIsViewingRecipes;
    String mquery;
    int mpageno;
    boolean isperformeQuery;
    boolean isDone;


    public RecipeListViewModel() {
        recipeRepostory=RecipeRepostory.getInstance();
        isDone=false;

    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepostory.getRecipes();
    }

    public void setIsperformeQuery(boolean isperformeQuery) {
        this.isperformeQuery = isperformeQuery;
    }

    public void searchRecipeApi(String query, int pageNo){
        isperformeQuery=true;
        mquery=query;
        mpageno=pageNo;
        mIsViewingRecipes = true;
        recipeRepostory.searchRecipeApi(query,pageNo);

    }
    public  LiveData<Boolean> getGetRecipesState() {
        return recipeRepostory.getGetRecipesState();
    }

    public void queryNextPage(){
        setDone(false);
        if(mIsViewingRecipes&&!isperformeQuery&&!isQueryExh().getValue()){
        searchRecipeApi(mquery,++mpageno);}
    }
    public LiveData<Boolean> isQueryExh(){
        return recipeRepostory.IsQueryExh();
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

}
