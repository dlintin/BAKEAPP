package io.dianto.baking.app.view.callback;

import io.dianto.baking.app.model.Recipe;

public interface RecipeOnClickListener {
    void onRecipeSelected(Recipe recipe);
}
