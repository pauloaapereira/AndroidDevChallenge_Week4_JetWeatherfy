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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pp.jetweatherfy.domain.base.successOr
import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.domain.model.Forecast
import com.pp.jetweatherfy.domain.usecases.cities.AddCity
import com.pp.jetweatherfy.domain.usecases.cities.FetchCities
import com.pp.jetweatherfy.domain.usecases.forecast.FetchForecast
import com.pp.jetweatherfy.presentation.forecast.components.content.AnimationDuration
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.GetForecast
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetSelectedDailyForecast
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetViewStatus
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetViewType
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent.SetWeatherUnit
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.LocationError
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.PermissionsError
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.SearchCities
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent.SetLocation
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.LocationViewState
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.HandlingErrors
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Idle
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Running
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val fetchForecast: FetchForecast,
    private val fetchCities: FetchCities,
    private val addCity: AddCity
) : ViewModel() {

    private val _forecastViewState = MutableStateFlow(ForecastViewState())
    val forecastViewState: StateFlow<ForecastViewState> = _forecastViewState

    private val _locationViewState = MutableStateFlow(LocationViewState())
    val locationViewState: StateFlow<LocationViewState> = _locationViewState

    fun onForecastEvent(event: ForecastViewEvent) = viewModelScope.launch {
        when (event) {
            is GetForecast -> {
                fetchForecast(event.city).collectLatest { result ->
                    val forecast = result.successOr(Forecast())
                    _forecastViewState.emit(
                        forecastViewState.value.copy(
                            forecast = forecast,
                            selectedDailyForecast = forecast.dailyForecasts.firstOrNull()
                                ?: DailyForecast(),
                            viewStatus = Running
                        )
                    )
                }
            }
            is SetSelectedDailyForecast -> {
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        selectedDailyForecast = event.selectedDailyForecast
                    )
                )
            }
            is SetViewStatus -> {
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        viewStatus = event.viewStatus
                    )
                )
                // If the status was set as handling errors, maintain the error state for 2 seconds and then move to idle
                if (event.viewStatus == HandlingErrors) {
                    delay(AnimationDuration * 2L)
                    _forecastViewState.emit(
                        forecastViewState.value.copy(
                            viewStatus = Idle
                        )
                    )
                }
            }
            is SetViewType -> {
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        viewType = event.viewType
                    )
                )
            }
            is SetWeatherUnit ->
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        weatherUnit = event.weatherUnit
                    )
                )
        }
    }

    fun onLocationEvent(event: LocationViewEvent) = viewModelScope.launch {
        when (event) {
            is SetLocation -> {
                onForecastEvent(GetForecast(event.location))

                addCity(event.location)
                fetchCities(event.location).collectLatest { cities ->
                    _locationViewState.emit(
                        locationViewState.value.copy(
                            query = event.location,
                            cities = cities.successOr(emptyList()),
                            errorGettingLocation = false,
                            errorGettingPermissions = false
                        )
                    )
                }
            }
            is SearchCities -> {
                if (event.query.isEmpty()) {
                    onForecastEvent(SetViewStatus(Idle))
                }

                fetchCities(event.query).collectLatest { cities ->
                    _locationViewState.emit(
                        locationViewState.value.copy(
                            query = event.query,
                            cities = cities.successOr(emptyList()),
                            errorGettingLocation = false,
                            errorGettingPermissions = false
                        )
                    )
                }
            }
            LocationError -> {
                onForecastEvent(SetViewStatus(HandlingErrors))
                _locationViewState.emit(
                    locationViewState.value.copy(
                        errorGettingLocation = true
                    )
                )
            }
            PermissionsError -> {
                onForecastEvent(SetViewStatus(HandlingErrors))
                _locationViewState.emit(
                    locationViewState.value.copy(
                        errorGettingPermissions = true
                    )
                )
            }
        }
    }
}
