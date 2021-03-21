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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.getFormattedTime
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.AnimationDuration
import com.pp.jetweatherfy.ui.components.content.ForecastDetailsAnimation
import com.pp.jetweatherfy.ui.components.content.UnselectedAlpha
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import kotlinx.coroutines.launch

private val AnimationStartOffset = (-400).dp
private val AnimationEndOffset = 0.dp

@Composable
fun JetWeatherfyDetailedContent(
    viewModel: ForecastViewModel,
    isActive: Boolean,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?
) {
    val coroutineScope = rememberCoroutineScope()
    val transition = updateTransition(targetState = isActive)
    val dailyForecastsScrollState = rememberLazyListState()
    val hourlyForecastsScrollState = rememberLazyListState()

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MediumDimension)
    ) {
        ForecastDetails(
            modifier = Modifier.offset(x = forecastDetailsX),
            selectedDailyForecast = selectedDailyForecast,
            surfaceColor = selectedDailyForecast?.generateWeatherColorFeel(),
            hourlyForecastsScrollState = hourlyForecastsScrollState
        )
        ForecastDays(
            modifier = Modifier.offset(x = forecastDaysX),
            dailyForecasts = forecast?.dailyForecasts ?: listOf(),
            surfaceColor = selectedDailyForecast?.generateWeatherColorFeel(),
            selectedDailyForecast = selectedDailyForecast,
            onDailyForecastSelected = { index, newSelectedDailyForecast ->
                viewModel.setSelectedDailyForecast(newSelectedDailyForecast)
                coroutineScope.launch {
                    dailyForecastsScrollState.animateScrollToItem(index)
                    hourlyForecastsScrollState.animateScrollToItem(0)
                }
            },
            dailyForecastsScrollState = dailyForecastsScrollState
        )
    }
}

@Composable
private fun ForecastDays(
    modifier: Modifier = Modifier,
    dailyForecastsScrollState: LazyListState,
    selectedDailyForecast: DailyForecast?,
    dailyForecasts: List<DailyForecast>,
    surfaceColor: Color?,
    onDailyForecastSelected: (Int, DailyForecast) -> Unit
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
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MediumDimension),
            state = dailyForecastsScrollState
        ) {
            itemsIndexed(dailyForecasts) { index, dailyForecast ->
                ForecastDaysItem(
                    isSelected = dailyForecast == selectedDailyForecast,
                    dailyForecast = dailyForecast,
                    surfaceColor = surfaceColor,
                    onDailyForecastSelected = { onDailyForecastSelected(index, dailyForecast) }
                )
            }
        }
    }
}

@Composable
private fun ForecastDaysItem(
    isSelected: Boolean,
    dailyForecast: DailyForecast,
    surfaceColor: Color?,
    onDailyForecastSelected: () -> Unit
) {
    val backgroundColor = (
        surfaceColor
            ?: MaterialTheme.colors.primary
        ).copy(alpha = if (isSelected) UnselectedAlpha else 0f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .clickable { onDailyForecastSelected() }
            .padding(SmallDimension),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SmallDimension)) {
            ForecastDetailsAnimation(weather = dailyForecast.weather, animationSize = BigDimension)
            Text(
                text = dailyForecast.getFormattedTime(),
                style = MaterialTheme.typography.subtitle2
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(SmallDimension)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SmallDimension),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind),
                    contentDescription = stringResource(R.string.wind),
                    modifier = Modifier.requiredSize(MediumDimension)
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
                    modifier = Modifier.requiredSize(MediumDimension)
                )
                Text(
                    text = "${dailyForecast.precipitationProbability} %",
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
        Text(
            text = "${dailyForecast.temperature}º",
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun ForecastDetails(
    modifier: Modifier = Modifier,
    hourlyForecastsScrollState: LazyListState,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MediumDimension)
            ) {
                ForecastDetailsDaily(dailyForecast)
                ForecastDetailsHourly(hourlyForecastsScrollState, dailyForecast.hourlyForecasts)
            }
        }
    }
}

@Composable
private fun ForecastDetailsDaily(dailyForecast: DailyForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

@Composable
private fun ForecastDetailsHourly(
    scrollState: LazyListState,
    hourlyForecasts: List<HourlyForecast>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(SmallDimension),
        state = scrollState
    ) {
        items(hourlyForecasts) { hourlyForecast ->
            ForecastDetailsHourlyItem(hourlyForecast = hourlyForecast)
        }
    }
}

@Composable
private fun ForecastDetailsHourlyItem(
    hourlyForecast: HourlyForecast
) {
    Column(
        modifier = Modifier.padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SmallDimension / 2)
    ) {
        Text(text = hourlyForecast.getFormattedTime(), style = MaterialTheme.typography.subtitle2)
        ForecastDetailsAnimation(weather = hourlyForecast.weather, animationSize = 30.dp)
        Text(text = "${hourlyForecast.temperature}º", style = MaterialTheme.typography.subtitle1)
    }
}
