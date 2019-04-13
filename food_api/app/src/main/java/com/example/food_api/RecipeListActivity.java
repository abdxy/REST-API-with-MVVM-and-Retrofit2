package com.example.food_api;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.food_api.ViewModels.RecipeListViewModel;
import com.example.food_api.adapters.OnRecipeListener;
import com.example.food_api.adapters.RecipeRecyclerApapter;
import com.example.food_api.models.Recipe;
import com.example.food_api.requests.RecipeApi;
import com.example.food_api.requests.ServerGenerator;
import com.example.food_api.requests.responses.RecipeSearchResponse;
import com.example.food_api.util.constants;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity   implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView mrecyclerView;
    private RecipeRecyclerApapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipelistactivity);
        recipeListViewModel= ViewModelProviders.of(this).get(RecipeListViewModel.class);


        mrecyclerView=(RecyclerView) findViewById(R.id.Recipe_list);
        intiRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!recipeListViewModel.isViewingRecipes()){
            displaySearchCategories();
        }


    }




    private void initSearchView() {
        final SearchView searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayLoading();
                searchView.clearFocus();
                searchRecipeApi(s,1);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void intiRecyclerView() {
        mAdapter=new RecipeRecyclerApapter(this);
        mrecyclerView.setAdapter(mAdapter);
        mrecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=30;
            }
        });
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1))
                {   if(recipeListViewModel.isDone())
                {recipeListViewModel.queryNextPage();
                 Log.d("searchRecipeApi:", "searchRecipeApi:next page");
                }
                }
            }
        });

    }

    private void searchRecipeApi(String query, int pageNo){
        Log.d("searchRecipeApi:", "searchRecipeApi:page");
        recipeListViewModel.setDone(false);
        recipeListViewModel.searchRecipeApi(query,pageNo);
    }


    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
              if(recipes!=null)
              if(recipeListViewModel.isViewingRecipes())
              {mAdapter.setRecipes(recipes);
               recipeListViewModel.setIsperformeQuery(false);
               recipeListViewModel.setDone(true);
              }
            }
        });
        recipeListViewModel.getGetRecipesState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean&&!recipeListViewModel.isDone()){
                    Toast.makeText(RecipeListActivity.this,"no network",Toast.LENGTH_LONG).show();

                }
            }
        });

        recipeListViewModel.isQueryExh().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean)
                mAdapter.setQueryExhausted();
            }
        });
    }


    @Override
    public void OnRecipeClick(int postion) {
        Intent intent=new Intent(this,RecipeActivity.class);
        if(recipeListViewModel.getRecipes().getValue()!=null)
        intent.putExtra("recipe",recipeListViewModel.getRecipes().getValue().get(postion));
        startActivity(intent);
    }

    @Override
    public void OnCategoryClick(String category) {
        mAdapter.displayLoading();
        Log.d(TAG, "OnCategoryClick: "+category);
        searchRecipeApi(category, 1);
    }
    private void displaySearchCategories(){
        Log.d(TAG, "displaySearchCategories: called.");
        recipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if(recipeListViewModel.isViewingRecipes())
        {
            displaySearchCategories();
            return;
        }
        super.onBackPressed();
    }
}
