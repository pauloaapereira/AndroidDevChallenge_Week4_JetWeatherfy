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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.Weather
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

@Composable
fun JetWeatherfyContent(
    viewModel: ForecastViewModel,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?,
    onDailyForecastSelected: (DailyForecast) -> Unit
) {
    val selectedCity by viewModel.selectedCity.observeAsState("")

    val transition = updateTransition(targetState = selectedCity.isNotBlank())

    val forecastDetailsX by transition.animateDp(
        transitionSpec = { tween(1000, easing = FastOutSlowInEasing) }
    ) { isCitySelected ->
        when (isCitySelected) {
            true -> 0.dp
            false -> 400.dp
        }
    }

    val forecastDaysX by transition.animateDp(transitionSpec = {
        tween(
            1000,
            delayMillis = 100,
            easing = FastOutSlowInEasing
        )
    }) { isCitySelected ->
        when (isCitySelected) {
            true -> 0.dp
            false -> 400.dp
        }
    }

    val forecastHoursX by transition.animateDp(transitionSpec = {
        tween(
            1000,
            delayMillis = 200,
            easing = FastOutSlowInEasing
        )
    }) { isCitySelected ->
        when (isCitySelected) {
            true -> 0.dp
            false -> 400.dp
        }
    }

    val cityNotSelectedScale by transition.animateFloat(transitionSpec = { tween(1000) }) { isCitySelected ->
        when (isCitySelected) {
            true -> 0f
            false -> 1f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SmallDimension, start = MediumDimension, end = MediumDimension)
            .navigationBarsPadding(left = false, right = false)
    ) {
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
                selectedDailyForecast = selectedDailyForecast,
                dailyForecasts = forecast?.dailyForecasts ?: listOf(),
                surfaceColor = selectedDailyForecast?.contentColor,
                onDailyForecastSelected = { newSelectedDailyForecast ->
                    onDailyForecastSelected(newSelectedDailyForecast)
                }
            )
            ForecastHours(
                modifier = Modifier.offset(x = forecastHoursX),
                hourlyForecasts = selectedDailyForecast?.hourlyForecasts ?: listOf(),
                surfaceColor = selectedDailyForecast?.contentColor,
            )
        }
        Text(
            modifier = Modifier
                .paddingFromBaseline(top = BigDimension)
                .scale(cityNotSelectedScale),
            text = "Hey... You need to select a city to see the forecast \uD83D\uDE06",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1
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
private fun ForecastDetailsAnimation(weather: Weather) {
    val animationSpec = LottieAnimationSpec.RawRes(weather.animation)
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
}

private const val SelectedAlpha = 0.2f
private const val UnselectedAlpha = 0.08f

@Composable
private fun ForecastDays(
    modifier: Modifier = Modifier,
    selectedDailyForecast: DailyForecast?,
    dailyForecasts: List<DailyForecast>,
    surfaceColor: Color?,
    onDailyForecastSelected: (DailyForecast) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BigDimension)
    ) {
        items(dailyForecasts) { dailyForecast ->
            val isSelectedAlpha =
                if (dailyForecast == selectedDailyForecast) SelectedAlpha else UnselectedAlpha
            ForecastDaysItem(
                surfaceColor = (surfaceColor
                    ?: MaterialTheme.colors.primary).copy(alpha = isSelectedAlpha),
                dailyForecast = dailyForecast,
                onDailyForecastSelected = { onDailyForecastSelected(dailyForecast) }
            )
        }
    }
}

@Composable
private fun ForecastDaysItem(
    surfaceColor: Color,
    dailyForecast: DailyForecast,
    onDailyForecastSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .clickable { onDailyForecastSelected() }
            .padding(SmallDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(text = dailyForecast.formattedTimestamp, style = MaterialTheme.typography.subtitle1)
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
                surfaceColor = (surfaceColor
                    ?: MaterialTheme.colors.primary).copy(alpha = UnselectedAlpha),
                hourlyForecast = hourlyForecast
            )
        }
    }
}

@Composable
private fun ForecastHoursItem(
    surfaceColor: Color,
    hourlyForecast: HourlyForecast
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .padding(MediumDimension),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = hourlyForecast.formattedTimestamp, style = MaterialTheme.typography.subtitle2)
        Text(text = "${hourlyForecast.temperature}º", style = MaterialTheme.typography.h2)
    }
}
