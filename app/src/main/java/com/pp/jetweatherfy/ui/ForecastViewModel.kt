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
package com.pp.jetweatherfy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pp.jetweatherfy.data.city.ICityRepository
import com.pp.jetweatherfy.data.forecast.IForecastRepository
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastRepository: IForecastRepository,
    private val cityRepository: ICityRepository
) : ViewModel() {

    private val _cities = MutableLiveData<List<String>>(listOf())
    val cities: LiveData<List<String>> = _cities

    private val _forecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast> = _forecast

    private val _selectedDailyForecast = MutableLiveData<DailyForecast>()
    val selectedDailyForecast: LiveData<DailyForecast> = _selectedDailyForecast

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _contentState = MutableLiveData(Simple)
    val contentState: LiveData<ContentState> = _contentState

    private val _state = MutableLiveData(JetWeatherfyState.Loading)
    val state: LiveData<JetWeatherfyState> = _state

    private val _weatherUnit = MutableLiveData(WeatherUnit.METRIC)
    val weatherUnit: LiveData<WeatherUnit> = _weatherUnit

    fun setState(newState: JetWeatherfyState) = viewModelScope.launch {
        _state.postValue(newState)
    }

    fun setContentState(state: ContentState) = viewModelScope.launch {
        _contentState.postValue(state)
    }

    fun setWeatherUnit(unit: WeatherUnit) = viewModelScope.launch {
        _weatherUnit.postValue(unit)
    }

    fun selectCity(city: String, fromLocation: Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
        if (fromLocation) {
            delay(2000L)
            cityRepository.addCity(city)
        }

        _searchQuery.postValue(city)
        delay(150)

        val forecast = forecastRepository.getForecast(city)
        _forecast.postValue(forecast)
        _selectedDailyForecast.postValue(forecast.getFirstDailyForecast())

        setState(Running)
    }

    fun selectDailyForecast(dailyForecast: DailyForecast) = viewModelScope.launch {
        _selectedDailyForecast.postValue(dailyForecast)
    }

    fun searchCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        _searchQuery.postValue(city)

        if (city.isBlank()) {
            setState(Idle)
        }

        getCities(city)
    }

    private fun getCities(city: String) = viewModelScope.launch(Dispatchers.IO) {
        val cities = cityRepository.getCities(city)
        _cities.postValue(cities)
    }

    init {
        getCities("")
    }
}
