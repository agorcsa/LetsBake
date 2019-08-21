package com.example.andreeagorcsa.bakingsecrets.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RemoteViews;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.ui.MainActivity;

// class which is called during updates
public class RecipeAppWidgetProvider extends AppWidgetProvider {

    public static final String LOG_TAG = "RecipeAppWidgetProvider";

    /**
     * Initial setup when we create the widget
     * Updates the widget at a given interval
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds     contains all the Ids that the instances of all widgets that the user has created,
     *                         because we can add the widget to the screen more than one time
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // to update all the widgets at once, we loop through all the appWidgetIds
        for (int appWidgetId : appWidgetIds) {
            // The click on the ImageButton opens the MainActivity activity
            Intent intent = new Intent(context, MainActivity.class);
            // Wraps the intent in an pending intent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Intent serviceIntent = new Intent(context, RecipeWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // Creates an object of remote views which takes our widget_recipe layout as parameter
            // Displays our layout in another process(our widget)
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
            // we start the pending intent while clicking this button
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
            views.setRemoteAdapter(R.id.recipe_widget_list_view, serviceIntent);
            views.setEmptyView(R.id.recipe_widget_list_view, R.id.recipe_widget_empty_view);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            resizeWidget(appWidgetOptions, views);
            // update the AppWidget for the given Ids and views
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_list_view);
        }
    }

    /**
     * will be called every time when the size of the widget will change
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // We access the layout through RemoteViews
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);

        resizeWidget(newOptions, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views) {
        // We get the dimensions taking into consideration both portrait and landscape mode
        // Portrait mode: minWidth + maxHeight
        // Landscape mode: maxWidth + minHeight
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        if (minWidth > 100 || minHeight > 20) {
            views.setViewVisibility(R.id.recipe_widget_list_view, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.recipe_widget_list_view, View.GONE);
        }
    }

    /**
     * will be called when the widget will be deleted from the home screen
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    /**
     * will be called when the widget will be put for the first time on the home screen,
     * but not for the any additional one
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
    }

    /**
     * will be called when we delete the last widget from our home screen
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
    }
}
