package io.dianto.baking.app.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.model.Recipe;
import io.dianto.baking.app.R;
import io.dianto.baking.app.view.callback.RecipeOnClickListener;

import static io.dianto.baking.app.util.Constant.Function.setImageResource;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipes;
    private RecipeOnClickListener mCallback;

    public RecipesAdapter(RecipeOnClickListener callback) {
        mRecipes = new ArrayList<>();
        mCallback = callback;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recipes, parent, false);
        return new RecipeViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        holder.recipeTitle.setText(recipe.getName());

        if (recipe.getIngredients().size() == 1) {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ingredient_singular, recipe.getIngredients().size()));
        } else {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ingredient_plural, recipe.getIngredients().size()));
        }
        if (recipe.getSteps().size() == 1) {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_singular, recipe.getSteps().size()));
        } else {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_plural, recipe.getSteps().size()));
        }
        if (recipe.getServings() == 1) {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_singular, recipe.getServings()));
        } else {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_plural, recipe.getServings()));
        }

        if (!TextUtils.isEmpty(recipe.getImage())) {
            setImageResource(context, recipe.getImage(), holder.recipeImage);
        }
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

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_recipes_title)
        TextView recipeTitle;

        @BindView(R.id.adapter_recipes_cardview)
        CardView recipeCardView;

        @BindView(R.id.adapter_recipes_ingredient)
        TextView recipeIngredient;

        @BindView(R.id.adapter_recipes_step)
        TextView recipeStep;

        @BindView(R.id.adapter_recipes_serving)
        TextView recipeServing;

        @BindView(R.id.adapter_recipes_image)
        ImageView recipeImage;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(mRecipes.get(getAdapterPosition()));
                }
            });
        }
    }

    private void showDetail(Recipe recipe) {
        mCallback.onRecipeSelected(recipe);
    }
}