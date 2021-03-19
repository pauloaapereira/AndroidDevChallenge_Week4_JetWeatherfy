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
package com.pp.jetweatherfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension

@Composable
fun JetWeatherfyContent(
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?,
    selectedHourlyForecast: HourlyForecast?,
    onDailyForecastSelected: (DailyForecast) -> Unit,
    onHourlyForecastSelected: (HourlyForecast) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        WeatherDetails(selectedDailyForecast)
        ForecastDays(
            modifier = Modifier.padding(top = BigDimension),
            dailyForecasts = forecast?.dailyForecasts ?: listOf()
        )
        ForecastHours(
            hourlyForecasts = selectedDailyForecast?.hourlyForecasts ?: listOf(),
            surfaceColor = selectedDailyForecast?.contentColor
        )
    }
}

@Composable
private fun WeatherDetails(selectedDailyForecast: DailyForecast?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedDailyForecast?.let { dailyForecast ->
            val animationSpec = LottieAnimationSpec.RawRes(dailyForecast.weather.animation)
            val animationState =
                rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)
            val animationSize = 200.dp

            Box(modifier = Modifier.requiredSize(animationSize)) {
                LottieAnimation(
                    animationSpec,
                    modifier = Modifier.requiredSize(animationSize),
                    animationState
                )
            }

            Text(
                text = stringResource(id = dailyForecast.weather.description),
                style = MaterialTheme.typography.subtitle1
            )
            Text(text = "${dailyForecast.temperature}º", style = MaterialTheme.typography.h1)
            Row(
                horizontalArrangement = Arrangement.spacedBy(MediumDimension),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SmallDimension),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = "Wind",
                        modifier = Modifier.requiredSize(BigDimension)
                    )
                    Text(
                        text = "${dailyForecast.windSpeed} km/h",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SmallDimension),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_drop),
                        contentDescription = "drop",
                        modifier = Modifier.requiredSize(BigDimension)
                    )
                    Text(
                        text = "${dailyForecast.precipitationProbability} %",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        } ?: run {
            Text(text = "No data!")
        }
    }
}

@Composable
private fun ForecastDays(modifier: Modifier = Modifier, dailyForecasts: List<DailyForecast>) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        items(dailyForecasts) { dailyForecast ->
            Text(text = dailyForecast.formattedTimestamp)
        }
    }
}

@Composable
private fun ForecastHours(
    modifier: Modifier = Modifier,
    hourlyForecasts: List<HourlyForecast>,
    surfaceColor: Color?
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        items(hourlyForecasts) { hourlyForecast ->
            ForecastHoursItem(
                surfaceColor = (surfaceColor ?: MaterialTheme.colors.primary).copy(alpha = 0.08f),
                hourlyForecast = hourlyForecast
            )
        }
    }
}

@Composable
private fun ForecastHoursItem(surfaceColor: Color, hourlyForecast: HourlyForecast) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = hourlyForecast.formattedTimestamp, style = MaterialTheme.typography.h2)
    }
}