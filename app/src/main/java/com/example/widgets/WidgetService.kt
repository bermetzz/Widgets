package com.example.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RemoteViewsFactory(applicationContext, intent)
    }


    inner class RemoteViewsFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsService.RemoteViewsFactory {

        private val appWidgetId: Int = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        private  var dataList: ArrayList<Task> = TaskDataList.getDataList()


        override fun onCreate() {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val jsonData = sharedPreferences.getString("listViewData", null)
            if (jsonData != null) {
                val gson = Gson()
                val type: Type = object : TypeToken<List<Task?>?>() {}.type
                dataList.addAll(gson.fromJson(jsonData, type))
            }
        }



            override fun onDataSetChanged() {
                // Update the dataList with data from SharedPreferences
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val jsonData = sharedPreferences.getString("listViewData", null)
                if (jsonData != null) {
                    val gson = Gson()
                    val type: Type = object : TypeToken<List<Task?>?>() {}.type
                    dataList.clear()
                    dataList.addAll(gson.fromJson(jsonData, type))
                }
            }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
            val task = dataList[position]
            remoteViews.setTextViewText(R.id.task_tv, task.task.toString())
            remoteViews.setTextViewText(R.id.description_tv, task.description.toString())
            return remoteViews
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}