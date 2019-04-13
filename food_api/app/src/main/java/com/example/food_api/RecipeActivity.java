package com.example.food_api;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_api.ViewModels.RecipeListViewModel;
import com.example.food_api.ViewModels.RecipeViewModel;
import com.example.food_api.adapters.CategoryViewHolder;
import com.example.food_api.models.Recipe;

public class RecipeActivity extends BaseActivity {

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);
        recipeViewModel= ViewModelProviders.of(this).get(RecipeViewModel.class);
        showProgressBar(true);
        getRecipe();
        subsribeObserver();
    }

    private void subsribeObserver() {
        recipeViewModel.getRecipe().observe(RecipeActivity.this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe!=null)
                    if(recipe.getRecipe_id().equals(recipeViewModel.getId()))
                    {   recipeViewModel.setDone(true);
                        showRecipe(recipe);


                    }


            }
        });


        recipeViewModel.getGetRecipeState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean&&!recipeViewModel.isDone()){
                     showProgressBar(false);
                    Toast.makeText(RecipeActivity.this,"no network",Toast.LENGTH_LONG).show();}

            }
        });

    }
    private void showRecipe(Recipe recipe){
        mRecipeTitle.setText(recipe.getTitle());
        mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_launcher_background);

        Uri path = Uri.parse(recipe.getImage_url());
        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(path)
                .into(mRecipeImage);

        mRecipeIngredientsContainer.removeAllViews();
        for(String ingredient: recipe.getIngredients()){
            TextView textView = new TextView(this);
            textView.setText(ingredient);
            textView.setTextSize(15);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            mRecipeIngredientsContainer.addView(textView);
        }
        mScrollView.setVisibility(View.VISIBLE);
        showProgressBar(false);

    }

    private void getRecipe(){
        if(getIntent().hasExtra("recipe")){
            String id= ((Recipe)getIntent().getParcelableExtra("recipe")).getRecipe_id();
            recipeViewModel.searchRecipeByIdApi(id);
        }
    }
}
