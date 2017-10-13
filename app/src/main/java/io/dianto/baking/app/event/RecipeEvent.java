package io.dianto.baking.app.event;

import java.util.List;

import io.dianto.baking.app.model.Recipe;

public class RecipeEvent {
    private String message;
    private boolean success;
    private List<Recipe> recipes;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getMessage() {
        return message;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public boolean isSuccess() {
        return success;
    }
}