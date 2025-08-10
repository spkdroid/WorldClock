/*
 * Copyright (C) 2025 SPKD World Clock
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Data provider for World Clock widgets.
 * 
 * This class provides time zone data specifically for Android widgets,
 * handling the unique requirements of widget data access. Since widgets
 * cannot use Hilt dependency injection, this provider manually creates
 * the necessary database and repository instances.
 * 
 * Key responsibilities:
 * - Provide time zone data to widget components
 * - Manage widget-specific state (current city index per widget)
 * - Handle widget navigation between cities
 * - Format time data appropriately for widget display
 * - Persist widget preferences across app restarts
 * 
 * @param context Android context for database and preferences access
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
