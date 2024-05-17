package com.ddxz.best.widget.sina

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.work.*
import com.ddxz.best.R
import com.ddxz.best.constant.LOG_FUN_WIDGET_UPDATE
import com.ddxz.best.net.ApiService
import java.text.SimpleDateFormat
import java.util.*

class WidgetUpdateWorker(val appContext: Context, workerParams: WorkerParameters):
        CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {

        val time = calendarToStr(Calendar.getInstance())
        Log.d(LOG_FUN_WIDGET_UPDATE, "worker更新 ${time}")
//        Toast.makeText(applicationContext, "worker更新 ${calendarToStr(Calendar.getInstance())}", Toast.LENGTH_SHORT).show()
        // Do the work here--in this case, upload the images.

        val response = ApiService.api.weiboHot()
        if (response.code == 200) {

            Log.d(LOG_FUN_WIDGET_UPDATE, "${response.newslist?.size} ${SinaWidgetProvider.list?.get(0)?.hotword}")
            SinaWidgetProvider.list = response.newslist
//            val views: RemoteViews = RemoteViews(
//                applicationContext.packageName, R.layout.widget_sina
//                                                )
            SinaWidgetProvider.widgetIds.forEach { appWidgetId ->
                AppWidgetManager.getInstance(applicationContext).notifyAppWidgetViewDataChanged(appWidgetId, R.id.list)

                val views = RemoteViews(
                    appContext.packageName, R.layout.widget_sina
                                       )
                views.setTextViewText(R.id.tvTime, time)
                AppWidgetManager.getInstance(applicationContext).updateAppWidget(appWidgetId, views)
            }
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    fun calendarToStr(c: Calendar): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault())
        return format.format(c.time)
    }
}