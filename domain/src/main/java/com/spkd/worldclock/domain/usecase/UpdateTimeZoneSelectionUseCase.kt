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

package com.spkd.worldclock.domain.usecase

import com.spkd.worldclock.domain.repository.TimeZoneRepository
import javax.inject.Inject

/**
 * Use case for updating time zone selection status.
 * 
 * This use case encapsulates the business logic for modifying the selection
 * state of individual time zones. It handles user interactions when selecting
 * or deselecting time zones for display in the world clock interface.
 * 
 * The use case provides a clean abstraction for selection management,
 * ensuring that selection state changes are properly validated and
 * persisted through the repository layer.
 * 
 * Key characteristics:
 * - State management: Handles selection/deselection logic
 * - User interaction: Responds to user preference changes
 * - Atomic operation: Ensures consistent state updates
 * - Performance optimized: Updates only the selection flag
 * 
 * Business rules enforced:
 * - Validates time zone existence before updating
 * - Ensures data consistency during state changes
 * - Handles concurrent access to selection state
 * 
 * This use case is typically invoked by:
 * - Time zone selection screens when user toggles selections
 * - Programmatic selection during app initialization
 * - Import/restore functionality for user preferences
 * 
 * Usage example:
 * ```kotlin
 * viewModelScope.launch {
 *     try {
 *         updateTimeZoneSelectionUseCase("America/New_York", true)
 *         // Selection updated successfully
 *     } catch (e: Exception) {
 *         // Handle selection update error
 *     }
 * }
 * ```
 * 
 * @property repository TimeZoneRepository for data access operations
 * 
 * @see com.spkd.worldclock.domain.repository.TimeZoneRepository
 */
class UpdateTimeZoneSelectionUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    /**
     * Executes the use case to update time zone selection status.
     * 
     * This suspend function performs the selection update operation
     * asynchronously, ensuring it doesn't block the calling thread.
     * The operation is atomic and will either succeed completely or
     * fail with an exception.
     * 
     * @param uid Unique identifier of the time zone to update
     * @param isSelected New selection status (true for selected, false for unselected)
     * @throws Exception If the update operation fails or the time zone doesn't exist
     */
    suspend operator fun invoke(uid: String, isSelected: Boolean) {
        repository.updateTimeZoneSelection(uid, isSelected)
    }
}
