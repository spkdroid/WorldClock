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

data class TimeZoneUiState(
    val allTimeZones: List<TimeZone> = emptyList(),
    val selectedTimeZones: List<TimeZone> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CurrentTimeInfo(
    val timeZone: TimeZone,
    val currentTime: String,
    val timeFormat: String = "HH:mm",
    val dateFormat: String = "MMM dd, yyyy"
)

@HiltViewModel
class WorldClockViewModel @Inject constructor(
    private val getAllTimeZonesUseCase: GetAllTimeZonesUseCase,
    private val getSelectedTimeZonesUseCase: GetSelectedTimeZonesUseCase,
    private val updateTimeZoneSelectionUseCase: UpdateTimeZoneSelectionUseCase,
    private val initializeDefaultTimeZonesUseCase: InitializeDefaultTimeZonesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimeZoneUiState())
    val uiState: StateFlow<TimeZoneUiState> = _uiState.asStateFlow()

    private val _currentTimes = MutableStateFlow<List<CurrentTimeInfo>>(emptyList())
    val currentTimes: StateFlow<List<CurrentTimeInfo>> = _currentTimes.asStateFlow()

    init {
        initializeData()
        observeTimeZones()
        startTimeUpdates()
    }

    private fun initializeData() {
        viewModelScope.launch {
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
    }

    private fun observeTimeZones() {
        viewModelScope.launch {
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
    }

    private fun startTimeUpdates() {
        viewModelScope.launch {
            while (true) {
                updateCurrentTimes(_uiState.value.selectedTimeZones)
                kotlinx.coroutines.delay(1000) // Update every second
            }
        }
    }

    private fun updateCurrentTimes(timeZones: List<TimeZone>) {
        val currentTimeInfos = timeZones.map { timeZone ->
            val timeZoneObj = java.util.TimeZone.getTimeZone(timeZone.timeZoneName)
            val calendar = Calendar.getInstance(timeZoneObj)
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            timeFormat.timeZone = timeZoneObj
            dateFormat.timeZone = timeZoneObj
            
            CurrentTimeInfo(
                timeZone = timeZone,
                currentTime = timeFormat.format(calendar.time),
                timeFormat = timeFormat.format(calendar.time),
                dateFormat = dateFormat.format(calendar.time)
            )
        }
        _currentTimes.value = currentTimeInfos
    }

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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
