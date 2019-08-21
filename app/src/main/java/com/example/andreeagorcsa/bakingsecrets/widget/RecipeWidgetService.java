package com.example.andreeagorcsa.bakingsecrets.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.model.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.andreeagorcsa.bakingsecrets.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecipeWidgetService extends RemoteViewsService {

    public static final String LOG_TAG = "RecipeWidgetService";

    /**
     * @param intent
     * @return RemoteViewsFactory objects
     * RemoteViewFactory class  = adapter
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetItemFactory(getApplicationContext(), intent);
    }

    /**
     * gets the data from our data source into each item view of the list view
     */
    class RecipeWidgetItemFactory implements RemoteViewsFactory {

        private Context mContext;
        // To distinguish between widget versions
        private int mAppWidgetId;
        //ListView of Ingredient objects
        private List<Ingredient> mIngredients = new ArrayList<>();

        RecipeWidgetItemFactory(Context context, Intent intent) {
            mContext = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        /**
         * will be triggered when we will first instantiate the RecipeWidgetItemFactory
         * connect to data source
         */
        @Override
        public void onCreate() {
        }

        /**
         * updates the widget
         */
        @Override
        public void onDataSetChanged() {
            getData();
        }

        /**
         * closes connection to the data source
         */
        @Override
        public void onDestroy() {

        }

        /**
         * @return the number of items that you want to display in the list
         */
        @Override
        public int getCount() {
            if (mIngredients == null) {
                return 0;
            }
            return mIngredients.size();
        }

        /**
         * loads data from the data source into the item view of the list view
         *
         * @param position
         * @return RemoteViews objects
         */
        @Override
        public RemoteViews getViewAt(int position) {
            // creates a RemoteViews object
            // each entry in a ListView = one RemoteViews object
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_item);
            views.setTextViewText(R.id.recipe_widget_list_item_text, mIngredients.get(position).getIngredient());
            return views;
        }

        /**
         * triggered when the data is loading
         *
         * @return
         */
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        /**
         * @return the number of different types of views of the collection
         */
        @Override
        public int getViewTypeCount() {
            return 1;
        }

        /**
         * what Id to return for each object
         *
         * @param position
         * @return position (the id and the position is the same)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * @return true, if the id for each item stays the same
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        public List<Ingredient> getData(){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPreferences.getString(MainActivity.SHARED_PREF, null);
            Type type = new TypeToken<List<Ingredient>>() {}.getType();
            List<Ingredient> ingredients = gson.fromJson(json,type);
            mIngredients = ingredients;
            return mIngredients;
        }
    }
}
