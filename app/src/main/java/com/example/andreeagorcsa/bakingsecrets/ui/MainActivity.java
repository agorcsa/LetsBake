package com.example.andreeagorcsa.bakingsecrets.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.adapter.RecipeAdapter;

import com.example.andreeagorcsa.bakingsecrets.model.Ingredient;
import com.example.andreeagorcsa.bakingsecrets.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import com.example.andreeagorcsa.bakingsecrets.model.Recipe;
import com.example.andreeagorcsa.bakingsecrets.widget.RecipeAppWidgetProvider;
import com.google.gson.Gson;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.ComponentName;

import androidx.test.espresso.idling.CountingIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    // constant for sending the recipe object through intent
    public static final String RECIPE_KEY = "recipe";
    // constant used to save the ingredients with shared preferences
    public static final String SHARED_PREF = "shared preferences";
    // variable for IdlingResource
    CountingIdlingResource mIdlingRes = new CountingIdlingResource("name");

    @BindView(R.id.recycler_view_recipe)
    RecyclerView mRecipeRecyclerView;

    private RecipeAdapter mRecipeAdapter;
    // GridlayoutManager used for portrait, landscape and tablet layout
    private GridLayoutManager mGridLayoutManager;
    private ArrayList<Recipe> mRecipeList;

    // request queue for Volley
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Let's bake");

        buildRecipeRecyclerView();

        // Network Request with Volley
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }


    /**
     * builds a RecyclerView for displaying the recipes
     * for portrait numberOfColumns = 1
     * for landscape and tablet numberOfColumns = 2
     */
    public void buildRecipeRecyclerView() {
        ButterKnife.bind(this);
        mRecipeRecyclerView.setHasFixedSize(true);

        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.numberOfColumns));
        mRecipeRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecipeList = new ArrayList<>();
    }

    /**
     * parses JSON from the given url, extracts the int and Strings from the objects
     * and creates a recipeObject which will be added to the mRecipeList
     */
    private void parseJSON() {

        String url = "https://go.udacity.com/android-baking-app-json";


        /**
         * we call increment() just before the Network Request
         * increment() = there are active tasks
         */
        mIdlingRes.increment();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject recipe = response.getJSONObject(i);

                                int id = recipe.getInt("id");
                                String name = recipe.getString("name");
                                int servings = recipe.getInt("servings");
                                String image = recipe.getString("image");

                                // loops in the Ingredient List
                                List<Ingredient> ingredientList = new ArrayList<>();
                                JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
                                for (int j = 0; j < ingredientsArray.length(); j++) {
                                    JSONObject ingredients = ingredientsArray.getJSONObject(j);

                                    // extracts the float from quantity
                                    float quantityFloat = BigDecimal.valueOf(ingredients.getDouble("quantity")).floatValue();
                                    // converts the float into a String, in order to create an ingredientObjects made only of Strings
                                    String quantity = String.valueOf(quantityFloat);
                                    String measure = ingredients.getString("measure");
                                    String ingredient = ingredients.getString("ingredient");

                                    // creates an ingredientObject from the extracted Strings
                                    Ingredient ingredientObject = new Ingredient(quantity, measure, ingredient);
                                    ingredientList.add(ingredientObject);
                                }

                                // loops in the Step List
                                List<Step> stepList = new ArrayList<>();
                                JSONArray stepsArray = recipe.getJSONArray("steps");
                                for (int k = 0; k < stepsArray.length(); k++) {
                                    JSONObject steps = stepsArray.getJSONObject(k);

                                    int stepId = steps.getInt("id");
                                    String shortDescription = steps.getString("shortDescription");
                                    String description = steps.getString("description");
                                    String videoURL = steps.getString("videoURL");
                                    String thumbnailURL = steps.getString("thumbnailURL");

                                    // creates a stepObject from the extracted int and Strings
                                    Step stepObject = new Step(stepId, shortDescription, description, videoURL, thumbnailURL);
                                    stepList.add(stepObject);
                                }

                                Recipe recipeObject = new Recipe(id, name, ingredientList, stepList, servings, image);
                                mRecipeList.add(recipeObject);
                            }

                            mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeList);
                            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
                            mRecipeAdapter.setOnItemClickListener(MainActivity.this);
                            mRecipeAdapter.notifyDataSetChanged();

                            /**
                             * when the Network Request is complete, we call decrement()
                             * decrement() = no active tasks
                             */
                            mIdlingRes.decrement();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(LOG_TAG, "ERROR PARSING JSON!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);
        // reference to the menu item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // reference to the SearchView for searching the recipes
        SearchView searchView = (SearchView) searchItem.getActionView();
        // replaces the search button in the keyboard with an action done button
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecipeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItem spinnerItem = menu.findItem(R.id.spinner);

        // spinner declaration
        final Spinner spinner = (Spinner) spinnerItem.getActionView();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.item_spinner,
                getResources().getStringArray(R.array.recipes));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Add to widget")) {
                    // if the spinner selection is the first(default) position, don't do anything
                } else {
                    saveData();
                    // if a recipe is selected, show a toast
                    StyleableToast.makeText(MainActivity.this, spinner.getSelectedItem() + " selected", R.style.toast_style).show();
                    updateWidgets(getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            /**
             * saves the ingredients data into shared preferences
             */
            public void saveData() {
                Log.e(LOG_TAG, "saveData()");
                // we use the spinner only to get the index of the recipe that was clicked
                Recipe selectedRecipe = mRecipeList.get(spinner.getSelectedItemPosition() - 1);
                List<Ingredient> ingredients = selectedRecipe.getIngredients();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(ingredients);
                editor.putString(SHARED_PREF, json);
                editor.apply();
            }

            /**
             * sends broadcast to the app's widget
             * @param context
             */
            public void updateWidgets(Context context) {
                Intent intent = new Intent(context.getApplicationContext(), RecipeAppWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, RecipeAppWidgetProvider.class));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                context.sendBroadcast(intent);
            }
        });
        return true;
    }

    /**
     * sends the recipe object to the TabActivity
     *
     * @param position that has been clicked in the mRecipeList
     */
    @Override
    public void onItemClick(int position) {
        Intent recipeIntent = new Intent(this, TabActivity.class);
        Recipe recipe = mRecipeList.get(position);
        recipeIntent.putExtra(RECIPE_KEY, recipe);
        startActivity(recipeIntent);
    }

    /**
     * method to access our IdlingResource from the test
     *
     * @return mIdlingRes
     */
    public CountingIdlingResource getIdlingResourceInTest() {
        return mIdlingRes;
    }
}