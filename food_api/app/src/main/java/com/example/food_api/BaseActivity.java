package com.example.food_api;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.food_api.requests.RecipeApi;
import com.example.food_api.requests.ServerGenerator;
import com.example.food_api.requests.responses.RecipeSearchResponse;
import com.example.food_api.util.HorizontalDottedProgress;
import com.example.food_api.util.constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {
    private RelativeLayout progressBar;
    @Override
    public void setContentView(int layoutResID) {
        ConstraintLayout constraintLayout= (ConstraintLayout) getLayoutInflater().inflate(R.layout.base_activity,null);
        progressBar=(RelativeLayout) constraintLayout.findViewById(R.id.progress_horizontal);
        FrameLayout frameLayout=constraintLayout.findViewById(R.id.frameLayout);
        getLayoutInflater().inflate(layoutResID,frameLayout,true);
        super.setContentView(constraintLayout);

    }
    public void showProgressBar(boolean visible) {

        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

}
