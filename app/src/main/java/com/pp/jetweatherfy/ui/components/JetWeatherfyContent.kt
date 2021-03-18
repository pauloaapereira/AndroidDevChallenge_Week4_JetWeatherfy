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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension

@Composable
fun JetWeatherfyContent(
    forecast: Forecast?,
    selectedHourlyForecast: HourlyForecast?,
    onHourlyForecastSelected: (HourlyForecast) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        WeatherDetails(selectedHourlyForecast)
    }
}

@Composable
private fun WeatherDetails(selectedHourlyForecast: HourlyForecast?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedHourlyForecast?.let { hourlyForecast ->
            val animationSpec = LottieAnimationSpec.RawRes(hourlyForecast.weather.animation)
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
                text = stringResource(id = hourlyForecast.weather.description),
                style = MaterialTheme.typography.subtitle1
            )
            Text(text = "${hourlyForecast.temperature}º", style = MaterialTheme.typography.h1)
            Row(horizontalArrangement = Arrangement.spacedBy(MediumDimension), verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(SmallDimension), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = "Wind",
                        modifier = Modifier.requiredSize(BigDimension)
                    )
                    Text(
                        text = "${hourlyForecast.windSpeed} km/h",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(SmallDimension), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_drop),
                        contentDescription = "drop",
                        modifier = Modifier.requiredSize(BigDimension)
                    )
                    Text(
                        text = "${hourlyForecast.precipitationProbability} %",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        } ?: run {
            Text(text = "No data!")
        }
    }
}
