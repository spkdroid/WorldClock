package com.spkd.wordlclock.widget

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.spkd.worldclock.core.database.TimeZoneDatabase
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.repository.TimeZoneRepositoryImpl
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides time zone data for the widget.
 * Since widgets cannot use Hilt injection, we manually create the database and repository.
 */
class WorldClockWidgetDataProvider(private val context: Context) {
    
    private val database by lazy {
        Room.databaseBuilder(
            context,
            TimeZoneDatabase::class.java,
            TimeZoneDatabase.DATABASE_NAME
        ).build()
    }
    
    private val repository by lazy {
        TimeZoneRepositoryImpl(database.timeZoneDao())
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
    }
    
    /**
     * Get current times for all selected time zones
     */
    suspend fun getSelectedTimeZoneInfos(): List<TimeZoneInfo> {
        return try {
            val selectedTimeZones = repository.getSelectedTimeZones().first()
            selectedTimeZones.map { timeZone ->
                val timeZoneObj = java.util.TimeZone.getTimeZone(timeZone.timeZoneName)
                val calendar = Calendar.getInstance(timeZoneObj)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                
                timeFormat.timeZone = timeZoneObj
                dateFormat.timeZone = timeZoneObj
                
                TimeZoneInfo(
                    displayName = timeZone.displayName,
                    timeZoneName = timeZone.timeZoneName,
                    currentTime = timeFormat.format(calendar.time),
                    currentDate = dateFormat.format(calendar.time),
                    calendar = calendar
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("WidgetDataProvider", "Error getting time zone data", e)
            emptyList()
        }
    }
    
    /**
     * Get current city index for a specific widget
     */
    fun getCurrentCityIndex(widgetId: Int): Int {
        return prefs.getInt("city_index_$widgetId", 0)
    }
    
    /**
     * Set current city index for a specific widget
     */
    fun setCurrentCityIndex(widgetId: Int, index: Int) {
        prefs.edit().putInt("city_index_$widgetId", index).apply()
    }
    
    /**
     * Get the next city for a specific widget
     */
    suspend fun getNextCityForWidget(widgetId: Int): TimeZoneInfo? {
        val timeZoneInfos = getSelectedTimeZoneInfos()
        if (timeZoneInfos.isEmpty()) return null
        
        val currentIndex = getCurrentCityIndex(widgetId)
        val nextIndex = (currentIndex + 1) % timeZoneInfos.size
        setCurrentCityIndex(widgetId, nextIndex)
        
        return timeZoneInfos[nextIndex]
    }
    
    /**
     * Get current city for a specific widget
     */
    suspend fun getCurrentCityForWidget(widgetId: Int): TimeZoneInfo? {
        val timeZoneInfos = getSelectedTimeZoneInfos()
        if (timeZoneInfos.isEmpty()) return null
        
        val currentIndex = getCurrentCityIndex(widgetId)
        val validIndex = currentIndex.coerceIn(0, timeZoneInfos.size - 1)
        
        // Update index if it was out of bounds
        if (validIndex != currentIndex) {
            setCurrentCityIndex(widgetId, validIndex)
        }
        
        return timeZoneInfos[validIndex]
    }
    
    /**
     * Get widget display info including pagination
     */
    suspend fun getWidgetDisplayInfo(widgetId: Int): WidgetDisplayInfo? {
        val timeZoneInfos = getSelectedTimeZoneInfos()
        if (timeZoneInfos.isEmpty()) return null
        
        val currentIndex = getCurrentCityIndex(widgetId)
        val validIndex = currentIndex.coerceIn(0, timeZoneInfos.size - 1)
        
        return WidgetDisplayInfo(
            currentCity = timeZoneInfos[validIndex],
            currentIndex = validIndex + 1,
            totalCities = timeZoneInfos.size
        )
    }
    
    /**
     * Get a limited number of time zones for widget display
     */
    suspend fun getTimeZoneInfosForWidget(maxCount: Int = 3): List<TimeZoneInfo> {
        return getSelectedTimeZoneInfos().take(maxCount)
    }
    
    data class TimeZoneInfo(
        val displayName: String,
        val timeZoneName: String,
        val currentTime: String,
        val currentDate: String,
        val calendar: Calendar
    )

    data class WidgetDisplayInfo(
        val currentCity: TimeZoneInfo,
        val currentIndex: Int,
        val totalCities: Int
    )
}
