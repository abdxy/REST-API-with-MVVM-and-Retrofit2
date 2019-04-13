package com.example.food_api.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_api.R;

public class REcipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title, publisher, socialscore;
    ImageView imageView;
    OnRecipeListener onRecipeListener;


    public REcipesViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialscore = itemView.findViewById(R.id.recipe_social_score);
        imageView = itemView.findViewById(R.id.recipe_image);
        this.onRecipeListener = onRecipeListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.OnRecipeClick(getAdapterPosition());
    }
}
