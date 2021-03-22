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
package com.pp.jetweatherfy.ui.components.content

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.ContentState.Detailed
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Loading
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.Weather
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.detailed.JetWeatherfyDetailedContent
import com.pp.jetweatherfy.ui.components.content.simple.JetWeatherfySimpleContent
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

const val SelectedAlpha = 0.25f
const val UnselectedAlpha = 0.1f
const val AnimationDuration = 1000

@ExperimentalAnimationApi
@Composable
fun JetWeatherfyContent(
    viewModel: ForecastViewModel,
    state: JetWeatherfyState
) {
    val forecast by viewModel.forecast.observeAsState()
    val selectedDailyForecast by viewModel.selectedDailyForecast.observeAsState()

    val contentTransition = updateTransition(targetState = state)

    val idleValue by contentTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        }
    ) { appState ->
        if (appState is Idle) 1f else 0f
    }

    val detectingLocationValue by contentTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        }
    ) { appState ->
        if (appState is Loading) 1f else 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SmallDimension, start = MediumDimension, end = MediumDimension)
            .navigationBarsPadding(left = false, right = false)
    ) {
        Content(
            viewModel = viewModel,
            state = state,
            contentTransition = contentTransition,
            forecast = forecast,
            selectedDailyForecast = selectedDailyForecast
        )
        ContentMessage(
            modifier = Modifier
                .scale(idleValue)
                .alpha(idleValue),
            text = stringResource(R.string.choose_city)
        )
        ContentMessage(
            modifier = Modifier
                .scale(detectingLocationValue)
                .alpha(detectingLocationValue),
            text = stringResource(R.string.detecting_location)
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun Content(
    viewModel: ForecastViewModel,
    contentTransition: Transition<JetWeatherfyState>,
    state: JetWeatherfyState,
    forecast: Forecast?,
    selectedDailyForecast: DailyForecast?
) {

    Box(modifier = Modifier.contentTransition(contentTransition, Simple)) {
        JetWeatherfySimpleContent(
            viewModel = viewModel,
            isActive = state is Running && state.isOnSimpleView(),
            forecast = forecast,
            selectedDailyForecast = selectedDailyForecast
        )
    }

    Box(modifier = Modifier.contentTransition(contentTransition, Detailed)) {
        JetWeatherfyDetailedContent(
            viewModel = viewModel,
            isActive = state is Running && state.isOnDetailedView(),
            forecast = forecast,
            selectedDailyForecast = selectedDailyForecast
        )
    }
}

private fun Modifier.contentTransition(
    contentStateTransition: Transition<JetWeatherfyState>,
    ownState: ContentState
): Modifier = composed {
    val transitionValue by contentStateTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        }
    ) { state ->
        if (state is Running && state.contentState == ownState) 1f else 0f
    }

    graphicsLayer {
        alpha = transitionValue
    }
}

@Composable
private fun ContentMessage(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = BigDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
fun ForecastDetailsAnimation(weather: Weather, animationSize: Dp? = null) {
    val animationSpec = LottieAnimationSpec.RawRes(weather.animation)
    val animationState =
        rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)

    Box(modifier = Modifier.weatherAnimation(animationSize)) {
        LottieAnimation(
            animationSpec,
            modifier = Modifier.fillMaxSize(),
            animationState
        )
    }
}

private fun Modifier.weatherAnimation(size: Dp? = null): Modifier = composed {
    size?.let {
        requiredSize(it)
    } ?: run {
        fillMaxHeight(.25f)
    }
}

@Composable
fun Temperature(
    temperature: Int,
    maxTemperature: Int? = null,
    minTemperature: Int? = null,
    celsius: Boolean = true,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    temperatureStyle: TextStyle = MaterialTheme.typography.h1,
    maxAndMinStyle: TextStyle = MaterialTheme.typography.subtitle2
) {
    val type = if (celsius) "º" else "ºF"
    Row(verticalAlignment = alignment) {
        Text(
            text = "$temperature$type",
            style = temperatureStyle
        )
        Column(modifier = Modifier.padding(top = 1.dp)) {
            if (maxTemperature != null && minTemperature != null) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "Max Temperature"
                    )
                    Text(
                        text = "$maxTemperature$type",
                        style = maxAndMinStyle
                    )
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Min Temperature"
                    )
                    Text(
                        text = "$minTemperature$type",
                        style = maxAndMinStyle
                    )
                }
            }
        }
    }
}
