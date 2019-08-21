package com.example.andreeagorcsa.bakingsecrets.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @SerializedName("id")
    private Integer recipeId;
    @SerializedName("name")
    private String recipeName;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;
    private List<Recipe> mRecipeList;

    // recipe constructor
    public Recipe(Integer recipeId, String recipeName, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    // reads from parcel
    protected Recipe(Parcel in) {
        if (in.readByte() == 0) {
            recipeId = null;
        } else {
            recipeId = in.readInt();
        }
        recipeName = in.readString();
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        in.readTypedList(ingredients, Ingredient.CREATOR);

        if (steps == null) {
            steps = new ArrayList<>();
        }
        in.readTypedList(steps, Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    // getter and setter methods
    public static Creator<Recipe> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // writes to parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (recipeId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(recipeId);
        }
        parcel.writeString(recipeName);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Recipe> getRecipeList() {
        return mRecipeList;
    }

    public void setRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
    }
}