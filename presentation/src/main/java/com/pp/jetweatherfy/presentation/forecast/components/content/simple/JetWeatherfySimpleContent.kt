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

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.domain.model.Forecast
import com.pp.jetweatherfy.presentation.forecast.components.content.AnimationDuration
import com.pp.jetweatherfy.presentation.forecast.components.content.contentOffsetTransition
import com.pp.jetweatherfy.presentation.forecast.state.WeatherUnit
import com.pp.jetweatherfy.presentation.theme.BigDimension
import com.pp.jetweatherfy.presentation.theme.MediumDimension
import com.pp.jetweatherfy.presentation.utils.generateColorBasedOnForecast
import com.pp.jetweatherfy.presentation.utils.scrollToBegin
import kotlinx.coroutines.launch

@Composable
fun JetWeatherfySimpleContent(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    forecast: Forecast,
    selectedDailyForecast: DailyForecast,
    weatherUnit: WeatherUnit,
    onSeeMoreClick: () -> Unit,
    onDailyForecastSelected: (DailyForecast) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val dailyForecastsScrollState = rememberLazyListState()
    val hourlyForecastsScrollState = rememberLazyListState()
    val transition =
        updateTransition(targetState = isActive, label = "JetWeatherfySimpleContentTransition")

    if (!isActive) {
        dailyForecastsScrollState.scrollToBegin(coroutineScope)
        hourlyForecastsScrollState.scrollToBegin(coroutineScope)
    }

    val firstTileValue by contentOffsetTransition(transition = transition)
    val secondTileValue by contentOffsetTransition(transition = transition, delay = 100)
    val thirdTileValue by contentOffsetTransition(transition = transition, delay = 200)
    val alphaValue by transition.animateFloat(
        transitionSpec = { tween(AnimationDuration) },
        label = ""
    ) { active -> if (active) 1f else 0f }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        if (isActive || alphaValue > .15f) {
            SimpleContentDetails(
                modifier = modifier
                    .offset(x = firstTileValue)
                    .alpha(alphaValue),
                selectedDailyForecast = selectedDailyForecast,
                weatherUnit = weatherUnit
            )
            SimpleContentDays(
                modifier = Modifier
                    .padding(top = BigDimension)
                    .offset(x = secondTileValue)
                    .alpha(alphaValue),
                scrollState = dailyForecastsScrollState,
                selectedDailyForecast = selectedDailyForecast,
                dailyForecasts = forecast.dailyForecasts.take(2),
                onMoreClick = { onSeeMoreClick() },
                onDailyForecastSelected = { index, newSelectedDailyForecast ->
                    onDailyForecastSelected(newSelectedDailyForecast)
                    coroutineScope.launch {
                        dailyForecastsScrollState.animateScrollToItem(index)
                        hourlyForecastsScrollState.animateScrollToItem(0)
                    }
                }
            )
            SimpleContentHours(
                modifier = Modifier
                    .offset(x = thirdTileValue)
                    .alpha(alphaValue),
                scrollState = hourlyForecastsScrollState,
                hourlyForecasts = selectedDailyForecast.hourlyForecasts,
                surfaceColor = selectedDailyForecast.generateColorBasedOnForecast(),
                weatherUnit = weatherUnit
            )
        }
    }
}
