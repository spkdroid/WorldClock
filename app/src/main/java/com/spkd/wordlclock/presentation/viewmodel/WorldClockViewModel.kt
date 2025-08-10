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

package com.spkd.wordlclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.domain.usecase.GetAllTimeZonesUseCase
import com.spkd.worldclock.domain.usecase.GetSelectedTimeZonesUseCase
import com.spkd.worldclock.domain.usecase.InitializeDefaultTimeZonesUseCase
import com.spkd.worldclock.domain.usecase.UpdateTimeZoneSelectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Data class representing the UI state for time zone management.
 * 
 * This immutable data structure encapsulates all the state information needed
 * by the UI layer to render the world clock interface.
 * 
 * @property allTimeZones Complete list of available time zones from the repository
 * @property selectedTimeZones List of time zones that user has selected for display
 * @property isLoading Loading state indicator for UI to show progress indicators
 * @property error Optional error message to display to the user
 */
data class TimeZoneUiState(
    val allTimeZones: List<TimeZone> = emptyList(),
    val selectedTimeZones: List<TimeZone> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Data class representing current time information for a specific time zone.
 * 
 * This class encapsulates formatted time data for display purposes, including
 * the associated time zone and formatted time strings.
 * 
 * @property timeZone The time zone entity this time info belongs to
 * @property currentTime Formatted current time string (HH:mm:ss)
 * @property timeFormat Time format pattern used for formatting
 * @property dateFormat Date format pattern used for formatting
 */
data class CurrentTimeInfo(
    val timeZone: TimeZone,
    val currentTime: String,
    val timeFormat: String = "HH:mm",
    val dateFormat: String = "MMM dd, yyyy"
)

/**
 * ViewModel for managing world clock functionality and state.
 * 
 * This ViewModel follows the MVVM pattern and serves as the presentation layer
 * coordinator between the UI and domain layer. It manages time zone selection,
 * real-time updates, and provides reactive state to the UI components.
 * 
 * Key responsibilities:
 * - Manage time zone selection state
 * - Provide real-time updates every second
 * - Handle user interactions for time zone toggling
 * - Coordinate with domain use cases for business logic
 * - Maintain reactive state flow for UI observations
 * 
 * The ViewModel uses Hilt for dependency injection and maintains state using
 * Kotlin coroutines and StateFlow for reactive programming.
 * 
 * @property getAllTimeZonesUseCase Use case for retrieving all available time zones
 * @property getSelectedTimeZonesUseCase Use case for retrieving user-selected time zones
 * @property updateTimeZoneSelectionUseCase Use case for updating time zone selection state
 * @property initializeDefaultTimeZonesUseCase Use case for setting up default time zones
 */
@HiltViewModel
class WorldClockViewModel @Inject constructor(
    private val getAllTimeZonesUseCase: GetAllTimeZonesUseCase,
    private val getSelectedTimeZonesUseCase: GetSelectedTimeZonesUseCase,
    private val updateTimeZoneSelectionUseCase: UpdateTimeZoneSelectionUseCase,
    private val initializeDefaultTimeZonesUseCase: InitializeDefaultTimeZonesUseCase
) : ViewModel() {

    /**
     * Private mutable state flow for UI state management.
     * 
     * This internal state holder allows the ViewModel to manage state mutations
     * while exposing a read-only interface to external consumers.
     */
    private val _uiState = MutableStateFlow(TimeZoneUiState())
    
    /**
     * Public read-only state flow for UI observation.
     * 
     * External components observe this flow to react to state changes.
     * The asStateFlow() ensures immutability for external consumers.
     */
    val uiState: StateFlow<TimeZoneUiState> = _uiState.asStateFlow()

    /**
     * Private mutable state flow for current time information.
     * 
     * Maintains real-time updates for all selected time zones with
     * formatted time strings for UI display.
     */
    private val _currentTimes = MutableStateFlow<List<CurrentTimeInfo>>(emptyList())
    
    /**
     * Public read-only state flow for current time observations.
     * 
     * UI components observe this to display real-time clock updates.
     */
    val currentTimes: StateFlow<List<CurrentTimeInfo>> = _currentTimes.asStateFlow()

    /**
     * ViewModel initialization block.
     * 
     * Executes when the ViewModel is created, setting up initial data
     * and starting background processes for time updates.
     */
    init {
        viewModelScope.launch {
            initializeData()
            observeTimeZones()
        }
        startTimeUpdates()
    }

    /**
     * Initializes application data by setting up default time zones.
     * 
     * This method ensures the application has a baseline set of time zones
     * available when first launched. It handles loading states and error
     * conditions appropriately.
     * 
     * @throws Exception If initialization fails, error is captured in UI state
     */
    private suspend fun initializeData() {
        try {
            _uiState.value = _uiState.value.copy(isLoading = true)
            initializeDefaultTimeZonesUseCase()
            _uiState.value = _uiState.value.copy(isLoading = false)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message
            )
        }
    }

    /**
     * Establishes reactive observation of time zone data changes.
     * 
     * This method combines multiple data streams (all time zones and selected
     * time zones) into a single UI state. It maintains real-time synchronization
     * between the repository and UI state.
     * 
     * Uses Kotlin coroutines combine operator to merge multiple flows and
     * automatically updates current times when selected time zones change.
     * 
     * @throws Exception Errors are caught and propagated to UI state
     */
    private suspend fun observeTimeZones() {
        combine(
            getAllTimeZonesUseCase(),
            getSelectedTimeZonesUseCase()
        ) { allTimeZones, selectedTimeZones ->
            TimeZoneUiState(
                allTimeZones = allTimeZones,
                selectedTimeZones = selectedTimeZones,
                isLoading = false
            )
        }.catch { throwable ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = throwable.message
            )
        }.collect { state ->
            _uiState.value = state
            updateCurrentTimes(state.selectedTimeZones)
        }
    }

    /**
     * Starts the real-time clock update mechanism.
     * 
     * Launches a coroutine that continuously updates the current time display
     * every second. This ensures all clocks remain synchronized and current.
     * 
     * The infinite loop runs in the viewModelScope, ensuring automatic cleanup
     * when the ViewModel is destroyed.
     */
    private fun startTimeUpdates() {
        viewModelScope.launch {
            while (true) {
                updateCurrentTimes(_uiState.value.selectedTimeZones)
                kotlinx.coroutines.delay(1000) // Update every second
            }
        }
    }

    /**
     * Updates current time information for the provided time zones.
     * 
     * Calculates and formats current time for each time zone, creating
     * CurrentTimeInfo objects with properly formatted time strings.
     * 
     * @param timeZones List of time zones to calculate current times for
     */
    private fun updateCurrentTimes(timeZones: List<TimeZone>) {
        val currentTimeInfos = timeZones.map { timeZone ->
            // Create time zone-specific calendar instance
            val timeZoneObj = java.util.TimeZone.getTimeZone(timeZone.timeZoneName)
            val calendar = Calendar.getInstance(timeZoneObj)
            
            // Configure formatters with appropriate time zone
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            timeFormat.timeZone = timeZoneObj
            dateFormat.timeZone = timeZoneObj
            
            // Create formatted time information
            CurrentTimeInfo(
                timeZone = timeZone,
                currentTime = timeFormat.format(calendar.time),
                timeFormat = timeFormat.format(calendar.time),
                dateFormat = dateFormat.format(calendar.time)
            )
        }
        _currentTimes.value = currentTimeInfos
    }

    /**
     * Toggles the selection state of a time zone.
     * 
     * This method handles user interactions when selecting or deselecting
     * time zones for display. It delegates to the domain layer for business
     * logic execution and handles any resulting errors.
     * 
     * @param timeZone The time zone to toggle selection for
     */
    fun toggleTimeZoneSelection(timeZone: TimeZone) {
        viewModelScope.launch {
            try {
                updateTimeZoneSelectionUseCase(
                    timeZone.uid,
                    !timeZone.isSelected
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Clears any error state in the UI.
     * 
     * This method allows the UI to acknowledge and dismiss error messages
     * after they have been displayed to the user.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
