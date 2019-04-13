package com.example.food_api.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_api.R;
import com.example.food_api.models.Recipe;
import com.example.food_api.util.constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerApapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Recipe> recipes;
    private OnRecipeListener monRecipeListener;
    private static final int RECIPE_TYPE = 1;
    public static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    public static final int EXHAUSTED_TYPE=4;

    public RecipeRecyclerApapter(OnRecipeListener monRecipeListener) {

        this.monRecipeListener = monRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;

        switch (i) { // i is the view type constant
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, viewGroup, false);
                return new REcipesViewHolder(view, monRecipeListener);
            }

            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, monRecipeListener);
            }

            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_progress_item, viewGroup, false);
                return new ProgressViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_exh, viewGroup, false);
                return new ExhViewHolder(view);
            }

            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, viewGroup, false);
                return new REcipesViewHolder(view, monRecipeListener);
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == RECIPE_TYPE) {

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_launcher_background))
                    .load(recipes.get(i).getImage_url())
                    .into(((REcipesViewHolder) viewHolder).imageView)
            ;


            ((REcipesViewHolder) viewHolder).title.setText(recipes.get(i).getTitle());
            ((REcipesViewHolder) viewHolder).publisher.setText(recipes.get(i).getPublisher());
            ((REcipesViewHolder) viewHolder).socialscore.setText(String.valueOf(Math.round(recipes.get(i).getSocial_rank())));
        } else if (viewHolder.getItemViewType() == CATEGORY_TYPE) {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://com.example.food_api/drawable/" + recipes.get(i).getImage_url());
            Glide.with(((CategoryViewHolder) viewHolder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(path)
                    .into(((CategoryViewHolder) viewHolder).categoryImage);

            ((CategoryViewHolder) viewHolder).categoryTitle.setText(recipes.get(i).getTitle());

        }

    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {

         if (recipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (recipes.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        }else if(recipes.get(position).getTitle().equals("EXHAUSTED...")){
             return EXHAUSTED_TYPE;
         }
                else if (recipes.size() - 1 == position
                && position != 0
                && !recipes.get(position).getTitle().equals("EXHAUSTED...")
        )return LOADING_TYPE;
        else{
            return RECIPE_TYPE;
        }
    }


    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if(recipes!=null)
        if (recipes.size() > 0) {
            if (recipes.get(recipes.size() - 1).getTitle().equals("LOADING...")) {
                return true;
            }
        }
        return false;
    }
    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        recipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    private void hideLoading(){
        if(isLoading()){
            for(Recipe recipe: recipes){
                if(recipe.getTitle().equals("LOADING...")){
                    recipes.remove(recipe);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        recipes = categories;
        notifyDataSetChanged();
    }
}
