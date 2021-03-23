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

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.AnimationDuration
import com.pp.jetweatherfy.ui.components.content.contentOffsetTransition
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.utils.scrollToBegin
import kotlinx.coroutines.launch

@Composable
fun JetWeatherfyDetailedContent(
    modifier: Modifier = Modifier,
    viewModel: ForecastViewModel,
    isActive: Boolean,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?,
    weatherUnit: WeatherUnit
) {
    val coroutineScope = rememberCoroutineScope()
    val transition = updateTransition(targetState = isActive)
    val dailyForecastsScrollState = rememberLazyListState()
    val hourlyForecastsScrollState = rememberLazyListState()

    val firstTileValue by contentOffsetTransition(transition = transition, inverseStart = true)
    val secondTileValue by contentOffsetTransition(transition = transition, delay = 100, inverseStart = true)
    val alphaValue by transition.animateFloat(transitionSpec = { tween(AnimationDuration) }) { active -> if (active) 1f else 0f }

    if (!isActive) {
        dailyForecastsScrollState.scrollToBegin(coroutineScope)
        hourlyForecastsScrollState.scrollToBegin(coroutineScope)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        if (isActive || alphaValue > .15f) {
            DetailedContentDetails(
                modifier = modifier
                    .offset(x = firstTileValue)
                    .alpha(alphaValue),
                selectedDailyForecast = selectedDailyForecast,
                hourlyForecastsScrollState = hourlyForecastsScrollState,
                weatherUnit = weatherUnit
            )
            DetailedContentDays(
                modifier = Modifier
                    .offset(x = secondTileValue)
                    .alpha(alphaValue),
                dailyForecasts = forecast?.dailyForecasts ?: listOf(),
                selectedDailyForecast = selectedDailyForecast,
                onDailyForecastSelected = { index, newSelectedDailyForecast ->
                    viewModel.selectDailyForecast(newSelectedDailyForecast)
                    coroutineScope.launch {
                        dailyForecastsScrollState.animateScrollToItem(index)
                        hourlyForecastsScrollState.animateScrollToItem(0)
                    }
                },
                weatherUnit = weatherUnit,
                dailyForecastsScrollState = dailyForecastsScrollState
            )
        }
    }
}
