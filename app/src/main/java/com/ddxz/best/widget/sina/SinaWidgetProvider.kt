package com.ddxz.best.widget.sina

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ddxz.best.R
import com.ddxz.best.constant.LOG_FUN_SINA
import com.ddxz.best.net.ApiService
import com.ddxz.best.net.Newslist
import com.ddxz.best.widget.WidgetRemoteViewsService
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * 测试WorkManager拉活桌面小组件刷新(唔work)
 */
class SinaWidgetProvider: AppWidgetProvider() {

    companion object {
        const val ACTION_SINA = "ACTION_SINA"
        var list: List<Newslist?>? = null
        var widgetIds: IntArray = intArrayOf()
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
                         ) {
        Log.d(LOG_FUN_SINA, "onUpdate: $appWidgetIds")
        widgetIds = appWidgetIds

        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
//            val pendingIntent: PendingIntent = Intent(context, ExampleActivity::class.java)
//                    .let { intent ->
//                        PendingIntent.getActivity(context, 0, intent, 0)
//                    }
//
//            // Get the layout for the App Widget and attach an on-click listener
//            // to the button
            val views: RemoteViews = RemoteViews(
                context.packageName, R.layout.widget_sina
                                                ).apply {
                setOnClickPendingIntent(R.id.ivBg, PendingIntent.getActivity(
                    context, appWidgetId, getSinaIntent(), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
            }

            CoroutineScope(Job() + Dispatchers.Main.immediate).launch {
                val response = ApiService.api.weiboHot()
                if (response.code == 200) {
                    list = response.newslist
                    Log.d(LOG_FUN_SINA, "list size ${list?.size}")
                    views.setRemoteAdapter(R.id.list, WidgetRemoteViewsService.getSinListAdapterIntent(context, appWidgetId))

                    val toastPendingIntent: PendingIntent = Intent(
                        context,
                        SinaWidgetProvider::class.java
                                                                  ).run {
                        // Set the action for the intent.
                        // When the user touches a particular view, it will have the effect of
                        // broadcasting TOAST_ACTION.
                        action = ACTION_SINA
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                        PendingIntent.getActivity(
                            context, appWidgetId, getSinaIntent(), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    }
                    views.setPendingIntentTemplate(R.id.list, toastPendingIntent)

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WorkManager
                .getInstance(context.applicationContext)
                .enqueue(PeriodicWorkRequestBuilder<WidgetUpdateWorker>(15, TimeUnit.MINUTES)
                        .addTag("SinaWidgetProvider")
//                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // 加急
                        .build())
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WorkManager
                .getInstance(context.applicationContext)
                .cancelAllWorkByTag("SinaWidgetProvider")
    }

    fun getSinaIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory("android.intent.category.DEFAULT")
            data = Uri.parse("sinaweibo://splash")
        }
    }
}