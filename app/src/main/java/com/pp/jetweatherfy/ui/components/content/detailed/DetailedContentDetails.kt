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
package com.pp.jetweatherfy.ui.components.content.detailed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.ui.components.content.UnselectedAlpha
import com.pp.jetweatherfy.ui.components.utils.WeatherAnimation
import com.pp.jetweatherfy.ui.components.utils.WeatherTemperature
import com.pp.jetweatherfy.ui.components.utils.WeatherWindAndPrecipitation
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension

@Composable
fun DetailedContentDetails(
    modifier: Modifier = Modifier,
    hourlyForecastsScrollState: LazyListState,
    selectedDailyForecast: DailyForecast?,
    weatherUnit: WeatherUnit
) {
    val backgroundColor =
        (selectedDailyForecast?.generateWeatherColorFeel() ?: MaterialTheme.colors.primary).copy(
            alpha = UnselectedAlpha
        )
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(MediumDimension),
        contentAlignment = Alignment.Center
    ) {
        selectedDailyForecast?.let { dailyForecast ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MediumDimension)
            ) {
                Details(dailyForecast, weatherUnit = weatherUnit)
                Hours(
                    scrollState = hourlyForecastsScrollState,
                    hourlyForecasts = dailyForecast.hourlyForecasts,
                    weatherUnit = weatherUnit
                )
            }
        }
    }
}

@Composable
private fun Details(dailyForecast: DailyForecast, weatherUnit: WeatherUnit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DetailsDescription(dailyForecast = dailyForecast, weatherUnit = weatherUnit)
        DetailsExtraInformation(dailyForecast = dailyForecast)
    }
}

@Composable
private fun DetailsDescription(dailyForecast: DailyForecast, weatherUnit: WeatherUnit) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WeatherAnimation(
            weather = dailyForecast.weather,
            animationSize = BigDimension * 3
        )

        WeatherTemperature(
            temperature = dailyForecast.temperature,
            minTemperature = dailyForecast.minTemperature,
            maxTemperature = dailyForecast.maxTemperature,
            temperatureStyle = MaterialTheme.typography.h2,
            maxAndMinStyle = MaterialTheme.typography.body2,
            alignment = Alignment.Top,
            weatherUnit = weatherUnit
        )
    }
}

@Composable
private fun DetailsExtraInformation(dailyForecast: DailyForecast) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(SmallDimension)
    ) {
        Text(
            text = stringResource(id = dailyForecast.weather.description),
            style = MaterialTheme.typography.subtitle1
        )
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

@Composable
private fun Hours(
    scrollState: LazyListState,
    hourlyForecasts: List<HourlyForecast>,
    weatherUnit: WeatherUnit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SmallDimension),
        state = scrollState
    ) {
        items(hourlyForecasts) { hourlyForecast ->
            Hour(
                hourlyForecast = hourlyForecast,
                weatherUnit = weatherUnit
            )
        }
    }
}

@Composable
private fun Hour(
    hourlyForecast: HourlyForecast,
    weatherUnit: WeatherUnit
) {
    Column(
        modifier = Modifier.padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SmallDimension / 2)
    ) {
        Text(text = hourlyForecast.getFormattedTime(), style = MaterialTheme.typography.subtitle2)
        WeatherAnimation(weather = hourlyForecast.weather, animationSize = 30.dp)
        WeatherTemperature(
            temperature = hourlyForecast.temperature,
            temperatureStyle = MaterialTheme.typography.subtitle1,
            weatherUnit = weatherUnit,
            compressUnitOnAverageTemp = false
        )
    }
}
