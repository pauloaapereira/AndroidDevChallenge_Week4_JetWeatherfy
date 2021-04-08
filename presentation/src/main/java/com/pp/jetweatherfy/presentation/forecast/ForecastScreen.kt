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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContent
import com.pp.jetweatherfy.presentation.forecast.components.surface.JetWeatherfySurface
import com.pp.jetweatherfy.presentation.forecast.components.topbar.JetWeatherfyTopBar
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetSelectedDailyForecast
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetViewType
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetWeatherUnit
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.SearchCities
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.SetLocation
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.LocationViewState
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Detailed

@Composable
fun ForecastScreen(viewModel: ForecastViewModel, onLocationRequested: () -> Unit) {
    val forecastViewState by viewModel.forecastViewState.collectAsState(ForecastViewState())
    val locationViewState by viewModel.locationViewState.collectAsState(LocationViewState())

    JetWeatherfySurface(forecastViewState.selectedDailyForecast) {
        JetWeatherfyTopBar(
            forecastState = forecastViewState,
            locationState = locationViewState,
            onWeatherUnitToggled = { weatherUnit ->
                viewModel.onForecastEvent(SetWeatherUnit(weatherUnit))
            },
            onViewTypeToggled = { viewType ->
                viewModel.onForecastEvent(SetViewType(viewType))
            },
            onSetLocationClick = {
                onLocationRequested()
            },
            onQueryTyping = { query ->
                viewModel.onLocationEvent(SearchCities(query))
            },
            onCitySelected = { city ->
                viewModel.onLocationEvent(SetLocation(city))
            }
        )
        JetWeatherfyContent(
            forecastState = forecastViewState,
            onDailyForecastSelected = { dailyForecast ->
                viewModel.onForecastEvent(SetSelectedDailyForecast(dailyForecast))
            },
            onSeeMoreClick = {
                viewModel.onForecastEvent(SetViewType(Detailed))
            }
        )
    }
}
