package com.example.findrestaurants

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.findrestaurants.placesAPI.Common
import java.lang.StringBuilder
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
class RandomWidget : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    Log.d("TEST", "yaaaaa")
    val cursor = MainActivity.contentResolverVar.query(
        Uri.parse("content://com.example.findrestaurants.db.providers.DatabaseProvider"),
        null,
        null,
        null,
        null
    )
    val builder = StringBuilder()
    builder.append("Favorite restaurants list:")
    builder.append(System.lineSeparator())
    builder.append(System.lineSeparator())
    while(cursor!!.moveToNext()) {
        builder.append(
            cursor.getString(cursor.getColumnIndex("name")) + " Rating: " +
            cursor.getDouble(cursor.getColumnIndex("rating")).toString()
        )

        builder.append(System.lineSeparator())
    }
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_widget)
    views.setTextViewText(R.id.appwidget_text, builder.toString())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}