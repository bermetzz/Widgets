package com.example.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService


class WidgetService : RemoteViewsService() {
    companion object {
        const val ACTION_CHECK_TASK = "com.example.widgets.CHECK_TASK"
    }

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

        private lateinit var tasks: List<Task>


        override fun onCreate() {}


        override fun onDataSetChanged() {
            tasks = TaskDataList.getDataList()
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val task = tasks[position]
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
            remoteViews.setTextViewText(R.id.task_tv, task.task)
            remoteViews.setTextViewText(R.id.description_tv, task.description)
            remoteViews.setImageViewResource(
                R.id.my_checkbox,
                if (task.isChecked) R.drawable.selected else R.drawable.not_selected
            )
            val checkIntent = Intent(context, TaskAppWidget::class.java).apply {
                action = ACTION_CHECK_TASK
                putExtra("CHECK", position)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            remoteViews.setOnClickFillInIntent(R.id.my_checkbox, checkIntent)
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