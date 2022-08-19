package com.ddxz.best.widget.day

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import com.ddxz.best.R
import com.ddxz.best.constant.LOG_FUN_DAY
import kotlinx.coroutines.*

/**
 * 小组件特定时间动画展示逻辑
 */
class DayWidgetProvider: AppWidgetProvider() {

    companion object {
        const val TAG = "DayWidgetProvider"
        var widgetIds: IntArray = intArrayOf()
    }

    var job: Job? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
                         ) {
        Log.d(LOG_FUN_DAY, "onUpdate: $appWidgetIds")
        widgetIds = appWidgetIds

        appWidgetIds.forEach { appWidgetId ->

            val views: RemoteViews = RemoteViews(
                context.packageName, R.layout.widget_day_night
                                                ).apply {
            }


            if (job == null) {
                job = MainScope().launch {

                    withContext(Dispatchers.Default) {

                        // 刷新动画开始（包括预备时间）
                        views.setTextColor(R.id.textClock, Color.parseColor("#9C794D"))
                        views.removeAllViews(R.id.fl)
                        views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_anim_day_flipper_start))
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                        delay(2000)

                        // 刷新动画结束
                        views.removeAllViews(R.id.fl)
                        views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_day))
                        appWidgetManager.updateAppWidget(appWidgetId, views)

                        // 正常时间
                        delay(4000)

                        while (isActive) {
                            // 刷新动画开始（包括预备时间）
                            views.removeAllViews(R.id.fl)
                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_anim_day_flipper))
                            appWidgetManager.updateAppWidget(appWidgetId, views)

                            delay(6000)

                            // 正常时间
                            views.removeAllViews(R.id.fl)
                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_night))
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                            delay(4000)

                            views.setTextColor(R.id.textClock, Color.WHITE)
                            views.removeAllViews(R.id.fl)
                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_anim_day_flipper2))
                            appWidgetManager.updateAppWidget(appWidgetId, views)

                            delay(6000)
                            views.setTextColor(R.id.textClock, Color.parseColor("#9C794D"))

                            // 正常时间
                            views.removeAllViews(R.id.fl)
                            views.addView(R.id.fl, RemoteViews(context.packageName, R.layout.view_day))
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                            delay(4000)
                        }
                    }
                }
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}