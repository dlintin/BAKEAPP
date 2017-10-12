package io.dianto.baking.app.events;

import java.util.List;

import io.dianto.baking.app.model.Recipes;


public class Recipe_Event {
    private String message;
    private boolean success;
    private List<Recipes> recipes;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
    }

    public String getMessage() {
        return message;
    }

    public List<Recipes> getRecipes() {
        return recipes;
    }

    public boolean isSuccess() {
        return success;
    }
}