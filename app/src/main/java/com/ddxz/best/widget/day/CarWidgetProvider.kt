package com.ddxz.best.widget.day

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.RemoteViews
import com.ddxz.best.R
import com.ddxz.best.constant.LOG_FUN_DAY
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Runnable

/**
 * 小组件特定时间动画展示逻辑
 */
class CarWidgetProvider : AppWidgetProvider() {

    companion object {
        const val TAG = "DayWidgetProvider"
        var widgetIds: IntArray = intArrayOf()
    }

    var job: Job? = null
    var jobAnim: Job? = null
    var appWidgetId: Int? = null
    val updateSignal = MutableStateFlow(AnimUpdate())

    // key appWidgetId value （key type value time）
    val aniMap = mutableMapOf<Int, MutableMap<String, Long>>()

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        if (aniMap.containsKey(appWidgetId)) {
            aniMap.remove(appWidgetId)
        }
        if (aniMap.isEmpty()) {
            EventBus.getDefault().unregister(this)
            Log.d("riki", "unregister")
        }
    }

    @OptIn(FlowPreview::class)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(LOG_FUN_DAY, "onUpdate: $appWidgetIds")
        if (EventBus.getDefault().isRegistered(this).not()) {
            EventBus.getDefault().register(this)
        }

        widgetIds = appWidgetIds

        appWidgetIds.forEach { appWidgetId ->
            if (aniMap.containsKey(appWidgetId).not()) {
                aniMap[appWidgetId] = mutableMapOf()
            }

            if (job?.isActive != true) {
                job = MainScope().launch {
                    updateSignal.collect {
                        Log.d("riki", "collect")
                        if (it.isUpdate) {
                            when (it.type) {
                                "car" -> {


                                    if (jobAnim?.isActive == true) {
                                        jobAnim?.cancel()
                                        Log.d("riki", "jobAnim cancel")
                                    } else {
                                        Log.d("riki", "jobAnim not cancel")
                                    }

                                    jobAnim = launch {

                                        val views: RemoteViews = RemoteViews(context.packageName, R.layout.widget_car).apply {}

                                        views.setOnClickPendingIntent(R.id.bg, PendingIntent.getBroadcast(context, appWidgetId, Intent().apply {
                                            this.component = ComponentName(context, CarWidgetProvider::class.java)
                                            this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                            this.putExtra("type", "car")
                                            this.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                                        }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))

                                        views.removeAllViews(R.id.flBrake)
                                        views.addView(R.id.flBrake, RemoteViews(context.packageName, R.layout.ani_car_brake))
                                        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)

                                        delay(3000)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val views: RemoteViews = RemoteViews(context.packageName, R.layout.widget_car).apply {}


            views.setOnClickPendingIntent(R.id.bg, PendingIntent.getBroadcast(context, appWidgetId, Intent().apply {
                this.component = ComponentName(context, CarWidgetProvider::class.java)
                this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                this.putExtra("type", "car")
                this.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        when (val type = intent?.getStringExtra("type")) {
            "car" -> {

                val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)

                EventBus.getDefault().post(AnimUpdate(appWidgetId, true, type, System.currentTimeMillis()))
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAnimUpdate(animUpdate: AnimUpdate) {
        Log.d("riki", "AnimUpdate")
        updateSignal.value = animUpdate
    }
}

data class AnimUpdate(var appWidgetId: Int = -1, val isUpdate: Boolean = false, val type: String? = null, val time: Long = 0L)