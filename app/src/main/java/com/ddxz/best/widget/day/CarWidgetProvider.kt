package com.ddxz.best.widget.day

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import android.widget.ViewFlipper
import com.ddxz.best.R
import com.ddxz.best.constant.LOG_FUN_DAY
import kotlinx.coroutines.*

/**
 * 小组件特定时间动画展示逻辑
 */
class CarWidgetProvider: AppWidgetProvider() {

    companion object {
        const val TAG = "DayWidgetProvider"
        var widgetIds: IntArray = intArrayOf()
    }

    var job: Job? = null
    var appWidgetId: Int? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
                         ) {
        Log.d(LOG_FUN_DAY, "onUpdate: $appWidgetIds")
        widgetIds = appWidgetIds

        appWidgetIds.forEach { appWidgetId ->

            val views: RemoteViews = RemoteViews(
                context.packageName, R.layout.widget_car
                                                ).apply {
            }


            views.setOnClickPendingIntent(
                R.id.bg,
                PendingIntent.getBroadcast(context, appWidgetId, Intent().apply {
                    this.component = ComponentName(context, CarWidgetProvider::class.java)
                    this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    this.putExtra("type", "car")
                    this.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                                               )

//            if (job == null) {
//                job = MainScope().launch {
//
//                    withContext(Dispatchers.Default) {
//
//                        while (isActive) {
//                            views.removeAllViews(R.id.fl)
//                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.vf_bear_animation))
//                            appWidgetManager.updateAppWidget(appWidgetId, views)
//
//                            delay(4800)
//
//                            views.removeAllViews(R.id.fl)
//                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_day))
//                            appWidgetManager.updateAppWidget(appWidgetId, views)
//
//                            delay(2000)
//                        }
//                    }
//                }
//            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        when (intent?.getStringExtra("type")) {
            "car" -> {

                val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)

                if (context == null || appWidgetId == -1) {
                    return
                }

                MainScope().launch {

                    val views: RemoteViews = RemoteViews(
                        context.packageName, R.layout.widget_car
                    ).apply {
                    }


                    views.setOnClickPendingIntent(
                        R.id.bg,
                        PendingIntent.getBroadcast(context, appWidgetId, Intent().apply {
                            this.component = ComponentName(context, CarWidgetProvider::class.java)
                            this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                            this.putExtra("type", "car")
                            this.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                        }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    )

                    views.removeAllViews(R.id.flBrake)
                    views.addView(R.id.flBrake, RemoteViews(context.packageName, R.layout.ani_car_brake))
                    AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}