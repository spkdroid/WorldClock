package com.spkd.wordlclock.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import com.spkd.wordlclock.MainActivity
import com.spkd.wordlclock.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality for World Clock
 * Shows selected time zones with analog clock and swipe navigation
 */
class WorldClockWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called with ${appWidgetIds.size} widgets")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        Log.d(TAG, "Widget enabled")
        WorldClockWidgetUpdateService.scheduleUpdates(context)
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        Log.d(TAG, "Widget disabled")
        WorldClockWidgetUpdateService.cancelUpdates(context)
        super.onDisabled(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: ${intent.action}")
        
        when (intent.action) {
            ACTION_NEXT_CITY -> {
                val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    handleNextCity(context, widgetId)
                }
                return
            }
        }
        
        super.onReceive(context, intent)
    }

    private fun handleNextCity(context: Context, widgetId: Int) {
        Log.d(TAG, "Handling next city for widget $widgetId")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dataProvider = WorldClockWidgetDataProvider(context)
                dataProvider.getNextCityForWidget(widgetId)
                
                // Update the widget
                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateAppWidget(context, appWidgetManager, widgetId)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling next city", e)
            }
        }
    }

    companion object {
        private const val TAG = "WorldClockWidget"
        private const val ACTION_NEXT_CITY = "com.spkd.wordlclock.widget.NEXT_CITY"
        
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            Log.d(TAG, "Updating widget with ID: $appWidgetId")
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val dataProvider = WorldClockWidgetDataProvider(context)
                    val displayInfo = dataProvider.getWidgetDisplayInfo(appWidgetId)
                    
                    val views = RemoteViews(context.packageName, R.layout.world_clock_widget_enhanced)
                    
                    if (displayInfo != null) {
                        val timeZoneInfo = displayInfo.currentCity
                        
                        // Generate analog clock bitmap
                        val clockBitmap = AnalogClockBitmapGenerator.generateClockBitmap(
                            context, 
                            timeZoneInfo.calendar, 
                            200
                        )
                        
                        // Update views
                        views.setImageViewBitmap(R.id.widget_analog_clock, clockBitmap)
                        views.setTextViewText(R.id.widget_time, timeZoneInfo.currentTime)
                        views.setTextViewText(R.id.widget_date, timeZoneInfo.currentDate)
                        views.setTextViewText(R.id.widget_timezone, timeZoneInfo.displayName)
                        views.setTextViewText(
                            R.id.widget_city_indicator, 
                            "${displayInfo.currentIndex} / ${displayInfo.totalCities}"
                        )
                        
                        Log.d(TAG, "Displaying: ${timeZoneInfo.displayName} - ${timeZoneInfo.currentTime} (${displayInfo.currentIndex}/${displayInfo.totalCities})")
                        
                    } else {
                        // Fallback to local time
                        val calendar = Calendar.getInstance()
                        val clockBitmap = AnalogClockBitmapGenerator.generateClockBitmap(context, calendar, 200)
                        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                        val currentDate = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(Date())
                        
                        views.setImageViewBitmap(R.id.widget_analog_clock, clockBitmap)
                        views.setTextViewText(R.id.widget_time, currentTime)
                        views.setTextViewText(R.id.widget_date, currentDate)
                        views.setTextViewText(R.id.widget_timezone, "Local Time")
                        views.setTextViewText(R.id.widget_city_indicator, "1 / 1")
                        
                        Log.d(TAG, "No selected time zones, showing local time")
                    }
                    
                    // Set up click intents
                    setupClickIntents(context, views, appWidgetId)
                    
                    // Update widget on main thread
                    CoroutineScope(Dispatchers.Main).launch {
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                        Log.d(TAG, "Widget $appWidgetId updated successfully")
                    }
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating widget $appWidgetId", e)
                    
                    // Fallback update on error
                    CoroutineScope(Dispatchers.Main).launch {
                        updateWidgetWithFallback(context, appWidgetManager, appWidgetId)
                    }
                }
            }
        }
        
        private fun setupClickIntents(context: Context, views: RemoteViews, appWidgetId: Int) {
            // Main widget click - open app
            val mainIntent = Intent(context, MainActivity::class.java)
            val mainPendingIntent = PendingIntent.getActivity(
                context, appWidgetId, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_container, mainPendingIntent)
            
            // Next city button click
            val nextCityIntent = Intent(context, WorldClockWidgetProvider::class.java).apply {
                action = ACTION_NEXT_CITY
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val nextCityPendingIntent = PendingIntent.getBroadcast(
                context, appWidgetId, nextCityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_next, nextCityPendingIntent)
            
            // Analog clock click - also cycles to next city
            views.setOnClickPendingIntent(R.id.widget_analog_clock, nextCityPendingIntent)
        }
        
        private fun updateWidgetWithFallback(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            try {
                val calendar = Calendar.getInstance()
                val clockBitmap = AnalogClockBitmapGenerator.generateClockBitmap(context, calendar, 200)
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val currentDate = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(Date())
                
                val views = RemoteViews(context.packageName, R.layout.world_clock_widget_enhanced)
                views.setImageViewBitmap(R.id.widget_analog_clock, clockBitmap)
                views.setTextViewText(R.id.widget_time, currentTime)
                views.setTextViewText(R.id.widget_date, currentDate)
                views.setTextViewText(R.id.widget_timezone, "Local Time")
                views.setTextViewText(R.id.widget_city_indicator, "1 / 1")
                
                setupClickIntents(context, views, appWidgetId)
                appWidgetManager.updateAppWidget(appWidgetId, views)
                
                Log.d(TAG, "Fallback widget update completed for $appWidgetId")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error in fallback widget update", e)
            }
        }
    }
}
