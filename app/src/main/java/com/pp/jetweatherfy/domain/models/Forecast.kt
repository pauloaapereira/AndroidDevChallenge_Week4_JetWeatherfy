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
package com.pp.jetweatherfy.domain.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Forecast(
    val city: City,
    val dailyForecasts: List<DailyForecast> = listOf()
) {
    fun getFirstHourlyForecast(): HourlyForecast? {
        val a = dailyForecasts.firstOrNull()?.hourlyForecasts?.firstOrNull()
        println(a)
        return a
    }
}

data class DailyForecast(
    val timestamp: String,
    val hourlyForecasts: List<HourlyForecast> = listOf()
)

data class HourlyForecast(
    val timestamp: String,
    val temperature: Int,
    val precipitationProbability: Int,
    val windSpeed: Int,
    val weather: Weather,
    val backgroundColor: Brush,
    val contentColor: Color
)
