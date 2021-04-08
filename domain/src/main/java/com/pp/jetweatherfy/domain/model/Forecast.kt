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
package com.pp.jetweatherfy.domain.model

data class Forecast(
    var city: String = "",
    val dailyForecasts: List<DailyForecast> = listOf()
)

data class DailyForecast(
    val timestamp: String = "",
    val hourlyForecasts: List<HourlyForecast> = listOf(),
    val temperature: Int = 0,
    val minTemperature: Int = 0,
    val maxTemperature: Int = 0,
    val precipitationProbability: Int = 0,
    val windSpeed: Int = 0,
    val weather: Weather = Weather.Sunny
)

data class HourlyForecast(
    val timestamp: String,
    val temperature: Int,
    val weather: Weather
)

enum class Weather {
    Sunny,
    Cloudy,
    Rainy,
    Thunderstorm,
    Windy
}
