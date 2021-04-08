/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pp.jetweatherfy.domain.usecases.cities

import com.pp.jetweatherfy.domain.base.Result
import com.pp.jetweatherfy.domain.base.data
import com.pp.jetweatherfy.domain.fakes.FakeCityRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchCitiesUseCaseTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)

    private val fetchCitiesUseCase = FetchCities(
        citiesRepository = FakeCityRepository(),
        dispatcher = coroutineDispatcher
    )

    @Test
    fun fetch_cities_returns_all_cities() = coroutineScope.runBlockingTest {
        // Given
        val query = ""
        val expectedCities = listOf(
            "San Francisco",
            "London",
            "New York",
            "Paris",
            "Moscow",
            "Tokyo",
            "Dubai",
            "Toronto"
        )

        // When
        val result = fetchCitiesUseCase(query).toList()
        assert(result.size == 2)
        assert(result[0] == Result.Loading)
        val cities = result[1].data ?: listOf()

        // Then
        assert(cities == expectedCities)
    }

    @Test
    fun fetch_cities_returns_starts_with() = coroutineScope.runBlockingTest {
        // Given
        val query = "To"
        val expectedCities = listOf(
            "Tokyo",
            "Toronto"
        )

        // When
        val result = fetchCitiesUseCase(query).toList()
        assert(result.size == 2)
        assert(result[0] == Result.Loading)
        val cities = result[1].data ?: listOf()

        // Then
        assert(cities == expectedCities)
    }
}
