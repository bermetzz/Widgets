package com.example.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews


class EntranceAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val remoteViews = RemoteViews(context.packageName, R.layout.entrance_app_widget)

    val number = +99999999
    val url = "https://api.whatsapp.com/send?phone=$number"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

}