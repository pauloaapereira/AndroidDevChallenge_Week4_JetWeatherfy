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
package com.pp.jetweatherfy.presentation.forecast.components.content

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.google.accompanist.insets.navigationBarsPadding
import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.presentation.R
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContentTestHelper.DetailedContent
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContentTestHelper.DetectingLocation
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContentTestHelper.DetectingLocationError
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContentTestHelper.NoCity
import com.pp.jetweatherfy.presentation.forecast.components.content.JetWeatherfyContentTestHelper.SimpleContent
import com.pp.jetweatherfy.presentation.forecast.components.content.detailed.ForecastDetailedView
import com.pp.jetweatherfy.presentation.forecast.components.content.simple.ForecastSimpleView
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.HandlingErrors
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Idle
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Loading
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Running
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Detailed
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Simple
import com.pp.jetweatherfy.presentation.theme.BigDimension
import com.pp.jetweatherfy.presentation.theme.MediumDimension
import com.pp.jetweatherfy.presentation.theme.SmallDimension
import com.pp.jetweatherfy.presentation.utils.AnimationDuration
import com.pp.jetweatherfy.presentation.utils.AnimationEndOffset
import com.pp.jetweatherfy.presentation.utils.AnimationStartOffset

@Composable
fun ForecastContent(
    forecastState: ForecastViewState,
    onSeeMoreClick: () -> Unit,
    onDailyForecastSelected: (DailyForecast) -> Unit
) {
    val stateTransition = updateTransition(
        targetState = forecastState.viewStatus,
        label = "JetWeatherfyContentTransition"
    )

    val detectingLocationAlpha by stateTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        },
        label = ""
    ) { newState ->
        if (newState == Loading) 1f else 0f
    }

    val noCityAlpha by stateTransition.animateFloat(
        transitionSpec = { tween(AnimationDuration) },
        label = ""
    ) { newState ->
        if (newState == Idle) 1f else 0f
    }

    val locationErrorValue by stateTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        },
        label = ""
    ) { newState ->
        if (newState == HandlingErrors) 1f else 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SmallDimension, start = MediumDimension, end = MediumDimension)
            .navigationBarsPadding(left = false, right = false)
    ) {
        ForecastSimpleView(
            modifier = Modifier.setTestTag(SimpleContent),
            isActive = forecastState.viewStatus == Running && forecastState.viewType == Simple,
            forecast = forecastState.forecast,
            selectedDailyForecast = forecastState.selectedDailyForecast,
            weatherUnit = forecastState.weatherUnit,
            onDailyForecastSelected = onDailyForecastSelected,
            onSeeMoreClick = onSeeMoreClick
        )
        ForecastDetailedView(
            modifier = Modifier.setTestTag(DetailedContent),
            isActive = forecastState.viewStatus == Running && forecastState.viewType == Detailed,
            forecast = forecastState.forecast,
            selectedDailyForecast = forecastState.selectedDailyForecast,
            weatherUnit = forecastState.weatherUnit,
            onDailyForecastSelected = onDailyForecastSelected
        )
        Message(
            modifier = Modifier
                .scale(detectingLocationAlpha)
                .alpha(detectingLocationAlpha)
                .setTestTag(DetectingLocation),
            text = stringResource(R.string.detecting_location)
        )
        Message(
            modifier = Modifier
                .scale(locationErrorValue)
                .alpha(locationErrorValue)
                .setTestTag(DetectingLocationError),
            text = stringResource(R.string.location_error)
        )
        Message(
            modifier = Modifier
                .scale(noCityAlpha)
                .alpha(noCityAlpha)
                .setTestTag(NoCity),
            text = stringResource(R.string.no_city)
        )
    }
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.subtitle1,
    align: TextAlign = TextAlign.Center
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = BigDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = align,
            style = style
        )
    }
}

@Composable
fun contentOffsetTransition(
    transition: Transition<Boolean>,
    delay: Int = 0,
    inverseStart: Boolean = false
): State<Dp> {
    return transition.animateDp(
        transitionSpec = {
            tween(
                AnimationDuration,
                delayMillis = delay,
                easing = FastOutSlowInEasing
            )
        },
        label = ""
    ) { isActive ->
        when (isActive) {
            true -> AnimationEndOffset
            false -> if (inverseStart) AnimationStartOffset * -1 else AnimationStartOffset
        }
    }
}

// Testing

object JetWeatherfyContentTestHelper {
    fun getTestTag(viewTag: String) = "JetWeatherfyContent_$viewTag"

    const val SimpleContent = "SimpleContent"
    const val DetailedContent = "DetailedContent"
    const val DetectingLocation = "DetectingLocation"
    const val DetectingLocationError = "DetectingLocationError"
    const val NoCity = "NoCity"
}

private fun Modifier.setTestTag(tag: String): Modifier = composed {
    semantics {
        testTag = JetWeatherfyContentTestHelper.getTestTag(tag)
    }
}
