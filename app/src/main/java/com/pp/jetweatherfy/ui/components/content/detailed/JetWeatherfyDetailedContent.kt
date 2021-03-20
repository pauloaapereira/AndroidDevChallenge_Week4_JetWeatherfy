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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.AnimationDuration
import com.pp.jetweatherfy.ui.components.content.ForecastDetailsAnimation
import com.pp.jetweatherfy.ui.components.content.UnselectedAlpha
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension

private val AnimationStartOffset = (-400).dp
private val AnimationEndOffset = 0.dp

@Composable
fun JetWeatherfyDetailedContent(
    viewModel: ForecastViewModel,
    isActive: Boolean,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?
) {
    val transition = updateTransition(targetState = isActive)

    val forecastDetailsX by transition.animateDp(
        transitionSpec = {
            tween(
                AnimationDuration,
                easing = FastOutSlowInEasing
            )
        }
    ) { isCitySelected ->
        when (isCitySelected) {
            true -> AnimationEndOffset
            false -> AnimationStartOffset
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        ForecastDetails(
            modifier = Modifier.offset(x = forecastDetailsX),
            selectedDailyForecast = selectedDailyForecast,
            surfaceColor = selectedDailyForecast?.generateWeatherColorFeel(),
        )
    }
}

@Composable
private fun ForecastDetails(
    modifier: Modifier = Modifier,
    selectedDailyForecast: DailyForecast?,
    surfaceColor: Color?
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                (surfaceColor ?: MaterialTheme.colors.primary).copy(alpha = UnselectedAlpha)
            )
            .fillMaxWidth()
            .padding(MediumDimension),
        contentAlignment = Alignment.Center
    ) {
        selectedDailyForecast?.let { dailyForecast ->
            ForecastDetailsHeader(dailyForecast)
        }
    }
}

@Composable
private fun ForecastDetailsHeader(dailyForecast: DailyForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
            ForecastDetailsAnimation(
                weather = dailyForecast.weather,
                animationSize = BigDimension * 3
            )
            Text(
                text = "${dailyForecast.temperature}º",
                style = MaterialTheme.typography.h2
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(SmallDimension)
        ) {
            Text(
                text = stringResource(id = dailyForecast.weather.description),
                style = MaterialTheme.typography.subtitle1
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(SmallDimension),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind),
                    contentDescription = stringResource(R.string.wind),
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
                    contentDescription = stringResource(R.string.precipitation_probability),
                    modifier = Modifier.requiredSize(BigDimension)
                )
                Text(
                    text = "${dailyForecast.precipitationProbability} %",
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}
