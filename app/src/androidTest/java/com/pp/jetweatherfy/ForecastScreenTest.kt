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
package com.pp.jetweatherfy

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pp.jetweatherfy.domain.usecases.cities.AddCity
import com.pp.jetweatherfy.domain.usecases.cities.FetchCities
import com.pp.jetweatherfy.domain.usecases.forecast.FetchForecast
import com.pp.jetweatherfy.fakes.FakeCityRepository
import com.pp.jetweatherfy.fakes.FakeForecastRepository
import com.pp.jetweatherfy.presentation.forecast.ForecastActivity
import com.pp.jetweatherfy.presentation.forecast.ForecastScreen
import com.pp.jetweatherfy.presentation.forecast.ForecastViewModel
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus
import com.pp.jetweatherfy.presentation.theme.JetWeatherfyTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ForecastScreenTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ForecastActivity>()

    private lateinit var viewModel: ForecastViewModel

    private val fetchForecastUseCase = FetchForecast(
        forecastRepository = FakeForecastRepository(),
        dispatcher = testDispatcher
    )

    private val fetchCitiesUseCase = FetchCities(
        citiesRepository = FakeCityRepository(),
        dispatcher = testDispatcher
    )

    private val addCityUseCase = AddCity(
        citiesRepository = FakeCityRepository(),
        dispatcher = testDispatcher
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    viewModel = ForecastViewModel(
                        fetchForecast = fetchForecastUseCase,
                        fetchCities = fetchCitiesUseCase,
                        addCity = addCityUseCase
                    )
                    ForecastScreen(viewModel = viewModel, onLocationRequested = {})
                }
            }
        }
    }

    
    @Test
    fun forecast_screen_first_state_idle() = testScope.runBlockingTest {
        // Given
        val expectedViewStatus = ViewStatus.Idle

        // When
        val currentStatus = viewModel.forecastViewState.value.viewStatus

        // Then
        assert(currentStatus == expectedViewStatus)
    }
}
