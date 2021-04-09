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
package com.pp.jetweatherfy.presentation.utils

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.pp.jetweatherfy.domain.MaxPrecipitation
import com.pp.jetweatherfy.domain.MaxTemperature
import com.pp.jetweatherfy.domain.MaxWindSpeed
import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.domain.model.HourlyForecast
import com.pp.jetweatherfy.domain.model.Weather
import com.pp.jetweatherfy.presentation.R
import org.joda.time.LocalDateTime
import java.util.Locale

@Composable
fun DailyForecast.getFormattedTime(): String {
    val timestampTime = LocalDateTime.parse(timestamp)
    val today = LocalDateTime.now()
    return when {
        timestampTime.dayOfYear == today.dayOfYear && timestampTime.year == today.year -> stringResource(
            R.string.today
        )
        timestampTime.dayOfYear == today.plusDays(1).dayOfYear && timestampTime.year == today.plusDays(
            1
        ).year -> stringResource(
            R.string.tomorrow
        )
        else -> timestampTime.toString(DailyTimestampFormat)
    }
}

fun HourlyForecast.getFormattedTime() =
    LocalDateTime.parse(timestamp).toString(HourlyTimestampFormat)
        .toUpperCase(Locale.getDefault())

fun DailyForecast.generateColorBasedOnForecast(): Color {
    return Color(
        red = (temperature * 255 / MaxTemperature.toFloat()) / 255f,
        green = (windSpeed * 255 / MaxWindSpeed.toFloat()) / 255f,
        blue = (precipitationProbability * 255 / MaxPrecipitation.toFloat()) / 255f
    )
}

data class WeatherResources(
    @StringRes val description: Int,
    @RawRes val icon: Int
)

fun Weather.getWeatherResources(): WeatherResources {
    return when (this) {
        Weather.Sunny -> WeatherResources(
            description = R.string.sunny,
            icon = R.raw.sunny
        )
        Weather.Cloudy -> WeatherResources(
            description = R.string.cloudy,
            icon = R.raw.cloudy
        )
        Weather.Rainy -> WeatherResources(
            description = R.string.rainy,
            icon = R.raw.rainy
        )
        Weather.Thunderstorm -> WeatherResources(
            description = R.string.thunderstorm,
            icon = R.raw.thunderstorm
        )
        Weather.Windy -> WeatherResources(
            description = R.string.windy,
            icon = R.raw.windy
        )
    }
}
