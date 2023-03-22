package com.example.widgets


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * Implementation of App Widget functionality.
 */


class TaskAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,

        ) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val jsonData = sharedPreferences.getString("listViewData", null)
        val dataList = TaskDataList.getDataList()
        if (jsonData != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Task?>?>() {}.type
            dataList.clear() // clear the dataList before adding the updated data
            dataList.addAll(gson.fromJson(jsonData, type))
        }

        // Create the RemoteViews object
        for (appWidgetId in appWidgetIds) {
            super.onUpdate(context, appWidgetManager, appWidgetIds)
            updateAppWidgets(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == "ADD_TASK") {
            val task = Task()
            task.task = intent.getStringExtra("task")
            task.description = intent.getStringExtra("description")
            TaskDataList.addTask(task)

            // Save the updated dataList to SharedPreferences
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putString("listViewData", Gson().toJson(TaskDataList.getDataList()))
            editor?.apply()

            // Update the AppWidget
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(context?.let {
                ComponentName(
                    it,
                    TaskAppWidget::class.java
                )
            })
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_task)
        }
    }
}

object TaskDataList {
    private val dataList: ArrayList<Task> = ArrayList()

    fun getDataList(): ArrayList<Task> {
        return dataList
    }

    fun addTask(task: Task) {
        dataList.add(task)
    }
}


internal fun updateAppWidgets(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.task_app_widget)

    //set list adapter
    val serviceIntent = Intent(context, WidgetService::class.java)
    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

    //set onclick listener
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    views.setOnClickPendingIntent(R.id.add_btn, pendingIntent)
    views.setRemoteAdapter(R.id.list_task, serviceIntent)
    views.setEmptyView(R.id.list_task, R.id.widgetEmptyViewText)
    appWidgetManager.updateAppWidget(appWidgetId, views)

}


