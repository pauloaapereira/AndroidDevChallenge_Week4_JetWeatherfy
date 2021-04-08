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

import com.pp.jetweatherfy.domain.fakes.FakeCityRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class AddCityUseCaseTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)

    private val cityRepository = FakeCityRepository()
    private val addCityUseCase = AddCity(
        citiesRepository = cityRepository,
        dispatcher = coroutineDispatcher
    )

    @Test
    fun add_city_returns_new_cities() = coroutineScope.runBlockingTest {
        // Given
        val cityToAdd = "San Francisco"
        val expectedNewCities = listOf(
            cityToAdd
        )

        // When
        addCityUseCase(cityToAdd)
        val newCities = cityRepository.getCities(cityToAdd)

        // Then
        assert(newCities.size == 1)
        assert(newCities == expectedNewCities)
    }

    @Test
    fun add_city_twice_returns_same_cities() = coroutineScope.runBlockingTest {
        // Given
        val cityToAdd = "San Francisco"
        val expectedNewCities = listOf(
            cityToAdd
        )

        // When
        addCityUseCase(cityToAdd)
        val newCities = cityRepository.getCities(cityToAdd)

        // Then
        assert(newCities.size == 1)
        assert(newCities == expectedNewCities)

        addCityUseCase(cityToAdd)
        val citiesAfterSecondAdd = cityRepository.getCities(cityToAdd)

        // Then
        assert(citiesAfterSecondAdd.size == 1)
        assert(citiesAfterSecondAdd == expectedNewCities)
    }
}
