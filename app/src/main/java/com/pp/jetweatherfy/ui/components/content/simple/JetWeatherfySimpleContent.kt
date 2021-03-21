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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.ContentState.Detailed
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.Weather
import com.pp.jetweatherfy.domain.models.getFormattedTime
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.AnimationDuration
import com.pp.jetweatherfy.ui.components.content.ForecastDetailsAnimation
import com.pp.jetweatherfy.ui.components.content.SelectedAlpha
import com.pp.jetweatherfy.ui.components.content.Temperature
import com.pp.jetweatherfy.ui.components.content.UnselectedAlpha
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import com.pp.jetweatherfy.utils.scrollToBegin
import kotlinx.coroutines.launch

private val AnimationStartOffset = 400.dp
private val AnimationEndOffset = 0.dp

@Composable
fun JetWeatherfySimpleContent(
    viewModel: ForecastViewModel,
    isActive: Boolean,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?
) {
    val coroutineScope = rememberCoroutineScope()
    val dailyForecastsScrollState = rememberLazyListState()
    val hourlyForecastsScrollState = rememberLazyListState()
    val transition = updateTransition(targetState = isActive)

    if (!isActive) {
        dailyForecastsScrollState.scrollToBegin(coroutineScope)
        hourlyForecastsScrollState.scrollToBegin(coroutineScope)
    }

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

    val forecastDaysX by transition.animateDp(
        transitionSpec = {
            tween(
                AnimationDuration,
                delayMillis = 100,
                easing = FastOutSlowInEasing
            )
        }
    ) { isCitySelected ->
        when (isCitySelected) {
            true -> AnimationEndOffset
            false -> AnimationStartOffset
        }
    }

    val forecastHoursX by transition.animateDp(
        transitionSpec = {
            tween(
                AnimationDuration,
                delayMillis = 200,
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
            selectedDailyForecast = selectedDailyForecast
        )
        ForecastDays(
            modifier = Modifier
                .padding(top = BigDimension)
                .offset(x = forecastDaysX),
            scrollState = dailyForecastsScrollState,
            selectedDailyForecast = selectedDailyForecast,
            dailyForecasts = forecast?.dailyForecasts?.take(2) ?: listOf(),
            surfaceColor = selectedDailyForecast?.generateWeatherColorFeel(),
            onMoreClick = {
                viewModel.setContentState(Detailed)
            },
            onDailyForecastSelected = { index, newSelectedDailyForecast ->
                viewModel.setSelectedDailyForecast(newSelectedDailyForecast)
                coroutineScope.launch {
                    dailyForecastsScrollState.animateScrollToItem(index)
                    hourlyForecastsScrollState.animateScrollToItem(0)
                }
            }
        )
        ForecastHours(
            modifier = Modifier.offset(x = forecastHoursX),
            scrollState = hourlyForecastsScrollState,
            hourlyForecasts = selectedDailyForecast?.hourlyForecasts ?: listOf(),
            surfaceColor = selectedDailyForecast?.generateWeatherColorFeel()
        )
    }
}

@Composable
private fun ForecastDetails(modifier: Modifier = Modifier, selectedDailyForecast: DailyForecast?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedDailyForecast?.let { dailyForecast ->
            ForecastDetailsAnimation(weather = dailyForecast.weather)

            Text(
                text = stringResource(id = dailyForecast.weather.description),
                style = MaterialTheme.typography.subtitle1
            )
            Temperature(
                temperature = dailyForecast.temperature,
                minTemperature = dailyForecast.minTemperature,
                maxTemperature = dailyForecast.maxTemperature
            )
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
}

@Composable
private fun ForecastDays(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    selectedDailyForecast: DailyForecast?,
    dailyForecasts: List<DailyForecast>,
    surfaceColor: Color?,
    onMoreClick: () -> Unit,
    onDailyForecastSelected: (Int, DailyForecast) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BigDimension),
        state = scrollState
    ) {
        itemsIndexed(dailyForecasts) { index, dailyForecast ->
            val isSelectedAlpha =
                if (dailyForecast == selectedDailyForecast) SelectedAlpha else UnselectedAlpha
            ForecastDaysItem(
                surfaceColor = (
                    surfaceColor
                        ?: MaterialTheme.colors.primary
                    ).copy(alpha = isSelectedAlpha),
                text = dailyForecast.getFormattedTime(),
                onClick = { onDailyForecastSelected(index, dailyForecast) }
            )
        }
        item {
            ForecastDaysItem(
                surfaceColor = (
                    surfaceColor
                        ?: MaterialTheme.colors.primary
                    ).copy(alpha = UnselectedAlpha),
                text = stringResource(R.string.more),
                onClick = { onMoreClick() }
            )
        }
    }
}

@Composable
private fun ForecastDaysItem(
    surfaceColor: Color,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .clickable { onClick() }
            .padding(SmallDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
private fun ForecastHours(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    hourlyForecasts: List<HourlyForecast>,
    surfaceColor: Color?
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MediumDimension),
        state = scrollState
    ) {
        items(hourlyForecasts) { hourlyForecast ->
            ForecastHoursItem(
                surfaceColor = (
                    surfaceColor
                        ?: MaterialTheme.colors.primary
                    ).copy(alpha = UnselectedAlpha),
                hourlyForecast = hourlyForecast,
                weather = hourlyForecast.weather
            )
        }
    }
}

@Composable
private fun ForecastHoursItem(
    surfaceColor: Color,
    hourlyForecast: HourlyForecast,
    weather: Weather?
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = hourlyForecast.getFormattedTime(), style = MaterialTheme.typography.subtitle2)
        weather?.let {
            ForecastDetailsAnimation(weather = it, animationSize = 30.dp)
        }
        Text(text = "${hourlyForecast.temperature}º", style = MaterialTheme.typography.h2)
    }
}
