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
import org.joda.time.DateTime

data class DailyForecast(
    val timestamp: String,
    val hourlyForecasts: List<HourlyForecast> = listOf(),
    val temperature: Int,
    val precipitationProbability: Int,
    val windSpeed: Int,
    val weather: Weather,
    val backgroundColor: Brush,
    val contentColor: Color
) {

    private val timestampFormat = "E, d MMM"

    val formattedTimestamp: String
        get() = DateTime.parse(timestamp).toString(timestampFormat)
}
