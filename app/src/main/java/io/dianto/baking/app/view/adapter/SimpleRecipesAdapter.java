package io.dianto.baking.app.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.model.Recipe;
import io.dianto.baking.app.view.callback.SimpleRecipeOnClickListener;
import io.dianto.baking.app.R;

public class SimpleRecipesAdapter extends RecyclerView.Adapter<SimpleRecipesAdapter.SimpleRecipesViewHolder> {

    private SimpleRecipeOnClickListener mCallback;
    private List<Recipe> mRecipes;

    public SimpleRecipesAdapter(SimpleRecipeOnClickListener callback) {
        mRecipes = new ArrayList<>();
        this.mCallback = callback;
    }


    @Override
    public SimpleRecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_simple_recipes, parent, false);
        return new SimpleRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleRecipesViewHolder holder, int position) {
        holder.simpleRecipesTitle.setText(mRecipes.get(holder.getAdapterPosition()).getName());
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setDataAdapter(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public List<Recipe> getDataAdapter() {
        return mRecipes;
    }

    public class SimpleRecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.simple_recipes_title)
        TextView simpleRecipesTitle;

        public SimpleRecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            simpleRecipesTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onRecipeSelected(mRecipes.get(getAdapterPosition()));
        }
    }
}
