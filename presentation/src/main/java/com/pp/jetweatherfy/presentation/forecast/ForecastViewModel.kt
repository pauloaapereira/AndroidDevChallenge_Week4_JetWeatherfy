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
import com.pp.jetweatherfy.domain.model.Forecast
import com.pp.jetweatherfy.domain.usecases.cities.AddCity
import com.pp.jetweatherfy.domain.usecases.cities.FetchCities
import com.pp.jetweatherfy.domain.usecases.forecast.FetchForecast
import com.pp.jetweatherfy.presentation.forecast.events.ForecastViewEvent
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.LocationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _requestingLocation = MutableStateFlow(false)
    val requestingLocation: StateFlow<Boolean> = _requestingLocation

    fun onForecastEvent(event: ForecastViewEvent) = viewModelScope.launch {
        when (event) {
            is ForecastViewEvent.GetForecast -> {
                fetchForecast(event.city).collectLatest { forecast ->
                    _forecastViewState.emit(
                        forecastViewState.value.copy(
                            forecast = forecast.successOr(Forecast())
                        )
                    )
                }
            }
            is ForecastViewEvent.SetSelectedDailyForecast -> {
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        selectedDailyForecast = event.selectedDailyForecast
                    )
                )
            }
            is ForecastViewEvent.SetViewType -> {
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        viewType = event.viewType
                    )
                )
            }
            is ForecastViewEvent.SetWeatherUnit ->
                _forecastViewState.emit(
                    forecastViewState.value.copy(
                        weatherUnit = event.weatherUnit
                    )
                )
        }
    }

    fun onLocationEvent(event: LocationViewEvent) = viewModelScope.launch {
        when (event) {
            is LocationViewEvent.SetLocation -> {
                _requestingLocation.emit(false)
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
            is LocationViewEvent.SearchCities -> {
                fetchCities(event.query).collectLatest { cities ->
                    _locationViewState.emit(
                        locationViewState.value.copy(
                            cities = cities.successOr(emptyList()),
                            errorGettingLocation = false,
                            errorGettingPermissions = false
                        )
                    )
                }
            }
            LocationViewEvent.LocationError -> {
                _requestingLocation.emit(false)
                _locationViewState.emit(
                    locationViewState.value.copy(
                        errorGettingLocation = true
                    )
                )
            }
            LocationViewEvent.PermissionsError -> {
                _requestingLocation.emit(false)
                _locationViewState.emit(
                    locationViewState.value.copy(
                        errorGettingPermissions = true
                    )
                )
            }
            LocationViewEvent.RequestLocation -> {
                _requestingLocation.emit(true)
            }
        }
    }
}
