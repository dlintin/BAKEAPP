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
import io.dianto.baking.app.R;
import io.dianto.baking.app.model.Recipes;
import io.dianto.baking.app.view.callback.Recipe_OnClickListener;

import static io.dianto.baking.app.util.Constant.Function.setImageResource;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private List<Recipes> mRecipes;
    private Recipe_OnClickListener mCallback;

    public RecipesAdapter(Recipe_OnClickListener callback) {
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
        Recipes recipes = mRecipes.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        holder.recipeTitle.setText(recipes.getName());

        if (recipes.getIngredientses().size() == 1) {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ingredient_singular, recipes.getIngredientses().size()));
        } else {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ingredient_plural, recipes.getIngredientses().size()));
        }
        if (recipes.getStepses().size() == 1) {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_singular, recipes.getStepses().size()));
        } else {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_plural, recipes.getStepses().size()));
        }
        if (recipes.getServings() == 1) {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_singular, recipes.getServings()));
        } else {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_plural, recipes.getServings()));
        }

        if (!TextUtils.isEmpty(recipes.getImage())) {
            setImageResource(context, recipes.getImage(), holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setDataAdapter(List<Recipes> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public List<Recipes> getDataAdapter() {
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

    private void showDetail(Recipes recipes) {
        mCallback.onRecipeSelected(recipes);
    }
}