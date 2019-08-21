package com.example.andreeagorcsa.bakingsecrets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.adapter.IngredientAdapter;
import com.example.andreeagorcsa.bakingsecrets.model.Ingredient;
import com.example.andreeagorcsa.bakingsecrets.model.Recipe;
import com.example.andreeagorcsa.bakingsecrets.ui.MainActivity;

import java.util.List;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    @BindView(R.id.recycler_view_ingredients)
    RecyclerView mIngredientRecyclerView;
    private Context mContext;
    private IngredientAdapter mIngredientAdapter;
    private RecyclerView.LayoutManager mIngredientLayoutManager;

    /**
     * creates a view containing the list of the ingredients objects,
     * displayed using the IngredientAdapter
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view containing the ingredients
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate((R.layout.fragment_ingredient), container, false);
        ButterKnife.bind(this, view);
        mIngredientRecyclerView.setHasFixedSize(true);

        mContext = view.getContext();
        mIngredientLayoutManager = new LinearLayoutManager(mContext);
        mIngredientRecyclerView.setLayoutManager(mIngredientLayoutManager);

        // gets the parcel from the intent, containing the recipe object
        Recipe recipe = getActivity().getIntent().getParcelableExtra(MainActivity.RECIPE_KEY);

        // gets the ingredients from the recipe object and puts them into an ingredients list
        List<Ingredient> ingredients = recipe.getIngredients();

        mIngredientAdapter = new IngredientAdapter((ArrayList<Ingredient>) ingredients);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        return view;
    }
}
