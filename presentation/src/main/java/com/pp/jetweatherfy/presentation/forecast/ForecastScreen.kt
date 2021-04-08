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
package com.pp.jetweatherfy.presentation.forecast

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContent
import com.pp.jetweatherfy.presentation.forecast.components.surface.JetWeatherfySurface
import com.pp.jetweatherfy.presentation.forecast.components.topbar.JetWeatherfyTopBar
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.LocationViewState
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Detailed

@ExperimentalAnimationApi
@Composable
fun ForecastScreen(viewModel: ForecastViewModel) {
    val forecastViewState by viewModel.forecastViewState.collectAsState(ForecastViewState())
    val locationViewState by viewModel.locationViewState.collectAsState(LocationViewState())

    JetWeatherfySurface(forecastViewState.selectedDailyForecast) {
        JetWeatherfyTopBar(
            forecastState = forecastViewState,
            locationState = locationViewState,
            onWeatherUnitToggled = { weatherUnit ->
                viewModel.onForecastEvent(ForecastViewEvent.SetWeatherUnit(weatherUnit))
            },
            onViewTypeToggled = { viewType ->
                viewModel.onForecastEvent(ForecastViewEvent.SetViewType(viewType))
            },
            onSetLocationClick = {
                viewModel.onLocationEvent(LocationViewEvent.RequestLocation)
            },
            onQueryTyping = { query ->
                viewModel.onLocationEvent(LocationViewEvent.SearchCities(query))
            },
            onCitySelected = { city ->
                viewModel.onLocationEvent(LocationViewEvent.SetLocation(city))
            }
        )
        JetWeatherfyContent(
            forecastState = forecastViewState,
            onDailyForecastSelected = { dailyForecast ->
                viewModel.onForecastEvent(ForecastViewEvent.SetSelectedDailyForecast(dailyForecast))
            },
            onSeeMoreClick = {
                viewModel.onForecastEvent(ForecastViewEvent.SetViewType(Detailed))
            }
        )
    }
}
