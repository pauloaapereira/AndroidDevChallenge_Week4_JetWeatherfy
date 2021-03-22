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
package com.pp.jetweatherfy.ui.components.content.simple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.ui.components.utils.WeatherAnimation
import com.pp.jetweatherfy.ui.components.utils.WeatherTemperature
import com.pp.jetweatherfy.ui.components.utils.WeatherWindAndPrecipitation
import com.pp.jetweatherfy.ui.theme.MediumDimension

@Composable
fun SimpleContentDetails(modifier: Modifier = Modifier, selectedDailyForecast: DailyForecast?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedDailyForecast?.let { dailyForecast ->
            Description(dailyForecast = dailyForecast)
            WeatherTemperature(
                temperature = dailyForecast.temperature,
                minTemperature = dailyForecast.minTemperature,
                maxTemperature = dailyForecast.maxTemperature
            )
            ExtraInformation(dailyForecast = dailyForecast)
        }
    }
}

@Composable
private fun Description(dailyForecast: DailyForecast) {
    WeatherAnimation(weather = dailyForecast.weather)

    Text(
        text = stringResource(id = dailyForecast.weather.description),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
private fun ExtraInformation(dailyForecast: DailyForecast) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MediumDimension),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WeatherWindAndPrecipitation(
            text = "${dailyForecast.windSpeed} km/h",
            icon = painterResource(id = R.drawable.ic_wind),
            contentDescription = stringResource(R.string.wind)
        )
        WeatherWindAndPrecipitation(
            text = "${dailyForecast.precipitationProbability} %",
            icon = painterResource(id = R.drawable.ic_drop),
            contentDescription = stringResource(R.string.precipitation_probability)
        )
    }
}
