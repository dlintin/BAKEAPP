package io.dianto.baking.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class Recipes {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredientses")
    private List<Ingredients> ingredientses;
    @SerializedName("stepses")
    private List<Steps> stepses;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredientses() {
        return ingredientses;
    }

    public List<Steps> getStepses() {
        return stepses;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}