package com.ddxz.best.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.ddxz.best.R
import com.ddxz.best.widget.sina.SinaWidgetProvider

class WidgetRemoteViewsService : RemoteViewsService() {

    companion object {
        const val TAG = "WidgetRemoteViewsService"
        const val TYPE_SINA_LIST = "TYPE_SINA_LIST"

        fun getSinListAdapterIntent(context: Context, appWidgetId: Int): Intent {
            val intent = Intent(context, WidgetRemoteViewsService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putExtra("type", TYPE_SINA_LIST)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            return intent
        }
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return when (intent.getStringExtra("type")) {
            else -> SinaListFactory(this.applicationContext)
        }
    }
}

class SinaListFactory(context: Context): BaseRemoteViewsFactory(context) {

    override fun getCount(): Int {
        return SinaWidgetProvider.list?.size ?: 0
    }

    override fun getViewAt(position: Int): RemoteViews {
//        Log.d(LOG_FUN_SINA, "getViewAt $position ${SinaWidgetProvider.list?.get(position)?.hotword}")
        val remoteViews = RemoteViews(context.packageName, R.layout.item_sina)
        remoteViews.setTextViewText(R.id.tvTitle, SinaWidgetProvider.list?.get(position)?.hotword)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }
}

abstract class BaseRemoteViewsFactory(val context: Context) :
        RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    abstract override fun getCount(): Int

    abstract override fun getViewAt(position: Int): RemoteViews

    override fun getLoadingView(): RemoteViews? {
        return RemoteViews(context.packageName, R.layout.view_loading)
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