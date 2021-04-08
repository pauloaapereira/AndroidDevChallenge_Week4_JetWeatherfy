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
package com.pp.jetweatherfy.domain.usecases.forecast

import com.pp.jetweatherfy.domain.base.Result
import com.pp.jetweatherfy.domain.base.data
import com.pp.jetweatherfy.domain.fakes.FakeForecastRepository
import com.pp.jetweatherfy.domain.model.Forecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchForecastUseCaseTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)

    private val fetchForecastUseCase = FetchForecast(
        forecastRepository = FakeForecastRepository(),
        dispatcher = coroutineDispatcher
    )

    @Test
    fun fetch_forecast_returns_lisbon() = coroutineScope.runBlockingTest {
        // Given
        val expectedCity = "Lisbon"

        // When
        val results = fetchForecastUseCase(expectedCity).toList()
        assert(results.size == 2)
        assert(results[0] == Result.Loading)
        val forecast = results[1].data ?: Forecast()

        // Then
        assert(forecast.city == expectedCity)
    }

    @Test
    fun fetch_forecast_returns_london() = coroutineScope.runBlockingTest {
        // Given
        val expectedCity = "London"

        // When
        val results = fetchForecastUseCase(expectedCity).toList()
        assert(results.size == 2)
        assert(results[0] == Result.Loading)
        val forecast = results[1].data ?: Forecast()

        // Then
        assert(forecast.city == expectedCity)
    }

    @Test
    fun fetch_forecast_returns_default() = coroutineScope.runBlockingTest {
        // Given
        val expectedForecast = Forecast()

        // When
        val results = fetchForecastUseCase("").toList()
        assert(results.size == 2)
        assert(results[0] == Result.Loading)
        val forecast = results[1].data ?: Forecast()

        // Then
        assert(forecast == expectedForecast)
    }
}
