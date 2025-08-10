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

package com.spkd.worldclock.di

import com.spkd.worldclock.data.repository.TimeZoneRepositoryImpl
import com.spkd.worldclock.domain.repository.TimeZoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt dependency injection module for repository bindings.
 * 
 * This module handles the binding of repository interfaces to their concrete
 * implementations, following the dependency inversion principle of Clean
 * Architecture. It ensures that the domain layer depends on abstractions
 * (interfaces) rather than concrete implementations.
 * 
 * The module uses @Binds instead of @Provides for interface bindings, which
 * is more efficient as it generates less code and provides better compile-time
 * validation.
 * 
 * Key characteristics:
 * - Abstract module for interface binding
 * - Singleton scope for repository instances
 * - Clean Architecture compliance
 * - Efficient binding using @Binds annotation
 * 
 * Benefits of this approach:
 * - Enables easy testing through interface mocking
 * - Supports multiple implementations of the same interface
 * - Provides clear separation between domain and data layers
 * - Enables runtime implementation switching if needed
 * 
 * @see dagger.hilt.InstallIn
 * @see dagger.hilt.components.SingletonComponent
 * @see com.spkd.worldclock.domain.repository.TimeZoneRepository
 * @see com.spkd.worldclock.data.repository.TimeZoneRepositoryImpl
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the TimeZoneRepository interface to its implementation.
     * 
     * This binding tells Hilt to provide TimeZoneRepositoryImpl instances
     * whenever TimeZoneRepository is requested as a dependency. The binding
     * is scoped as Singleton to ensure a single instance throughout the
     * application lifecycle.
     * 
     * The @Binds annotation is used instead of @Provides because:
     * - It's more efficient (generates less code)
     * - It provides better compile-time validation
     * - It clearly expresses the intent of interface binding
     * 
     * @param timeZoneRepositoryImpl The concrete implementation to bind
     * @return TimeZoneRepository interface bound to the implementation
     * 
     * @see dagger.Binds
     * @see javax.inject.Singleton
     */
    @Binds
    @Singleton
    abstract fun bindTimeZoneRepository(
        timeZoneRepositoryImpl: TimeZoneRepositoryImpl
    ): TimeZoneRepository
}
