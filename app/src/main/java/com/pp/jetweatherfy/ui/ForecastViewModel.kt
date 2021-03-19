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
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _selectedHourlyForecast = MutableLiveData<HourlyForecast>()
    val selectedHourlyForecast: LiveData<HourlyForecast> = _selectedHourlyForecast

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun selectCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        _searchQuery.postValue(city)
        val forecast = forecastRepository.getForecast(city)
        _forecast.postValue(forecast)
        _selectedDailyForecast.postValue(forecast.getFirstDailyForecast())
        _selectedHourlyForecast.postValue(forecast.getFirstHourlyForecast())
    }

    fun setSelectedDailyForecast(dailyForecast: DailyForecast) {
        _selectedDailyForecast.value = dailyForecast
    }

    fun setSelectedHourlyForecast(hourlyForecast: HourlyForecast) {
        _selectedHourlyForecast.value = hourlyForecast
    }

    fun search(query: String) = viewModelScope.launch(Dispatchers.IO) {
        _searchQuery.postValue(query)
        val cities = cityRepository.getCities(query)
        _cities.postValue(cities)
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() = viewModelScope.launch(Dispatchers.IO) {
        val defaultCity = cityRepository.getDefaultCity()
        search(defaultCity)

        val forecast = forecastRepository.getForecast(defaultCity)
        _forecast.postValue(forecast)
        _selectedDailyForecast.postValue(forecast.getFirstDailyForecast())
        _selectedHourlyForecast.postValue(forecast.getFirstHourlyForecast())
    }
}
