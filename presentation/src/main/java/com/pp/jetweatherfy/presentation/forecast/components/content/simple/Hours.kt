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
package com.pp.jetweatherfy.presentation.forecast.components.content.simple

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.domain.model.HourlyForecast
import com.pp.jetweatherfy.presentation.forecast.components.utils.WeatherAnimation
import com.pp.jetweatherfy.presentation.forecast.components.utils.WeatherTemperature
import com.pp.jetweatherfy.presentation.forecast.state.WeatherUnit
import com.pp.jetweatherfy.presentation.theme.MediumDimension
import com.pp.jetweatherfy.presentation.utils.UnselectedAlpha
import com.pp.jetweatherfy.presentation.utils.getFormattedTime

@Composable
internal fun Hours(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    hourlyForecasts: List<HourlyForecast>,
    surfaceColor: Color?,
    weatherUnit: WeatherUnit
) {
    val backgroundColor =
        (surfaceColor ?: MaterialTheme.colors.primary).copy(alpha = UnselectedAlpha)

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MediumDimension),
        state = scrollState
    ) {
        items(hourlyForecasts) { hourlyForecast ->
            Hour(
                surfaceColor = backgroundColor,
                hourlyForecast = hourlyForecast,
                weatherUnit = weatherUnit
            )
        }
    }
}

@Composable
private fun Hour(
    surfaceColor: Color,
    hourlyForecast: HourlyForecast,
    weatherUnit: WeatherUnit
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = hourlyForecast.getFormattedTime(), style = MaterialTheme.typography.subtitle2)
        WeatherAnimation(weather = hourlyForecast.weather, animationSize = 30.dp)
        WeatherTemperature(
            temperature = hourlyForecast.temperature,
            temperatureStyle = MaterialTheme.typography.subtitle1,
            weatherUnit = weatherUnit
        )
    }
}
