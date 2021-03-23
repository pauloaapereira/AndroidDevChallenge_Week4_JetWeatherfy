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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.ContentState.Detailed
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Loading
import com.pp.jetweatherfy.domain.JetWeatherfyState.LocationError
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContentTestHelper.DetailedContent
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContentTestHelper.DetectingLocation
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContentTestHelper.DetectingLocationError
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContentTestHelper.NoCity
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContentTestHelper.SimpleContent
import com.pp.jetweatherfy.ui.components.content.detailed.JetWeatherfyDetailedContent
import com.pp.jetweatherfy.ui.components.content.simple.JetWeatherfySimpleContent
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

const val SelectedAlpha = 0.25f
const val UnselectedAlpha = 0.1f
const val AnimationDuration = 1000
val AnimationStartOffset = 400.dp
val AnimationEndOffset = 0.dp

@ExperimentalAnimationApi
@Composable
fun JetWeatherfyContent(
    viewModel: ForecastViewModel,
    state: JetWeatherfyState,
    contentState: ContentState,
    weatherUnit: WeatherUnit
) {
    val forecast by viewModel.forecast.observeAsState()
    val selectedDailyForecast by viewModel.selectedDailyForecast.observeAsState()

    val stateTransition = updateTransition(targetState = state)

    val loadingValue by stateTransition.animateFloat(transitionSpec = { tween(AnimationDuration) }) { newState ->
        if (newState == Loading) 1f else 0f
    }

    val idleValue by stateTransition.animateFloat(transitionSpec = { tween(AnimationDuration) }) { newState ->
        if (newState == Idle) 1f else 0f
    }

    val locationErrorValue by stateTransition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration
            )
        }
    ) { newState ->
        if (newState == LocationError) 1f else 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SmallDimension, start = MediumDimension, end = MediumDimension)
            .navigationBarsPadding(left = false, right = false)
    ) {
        JetWeatherfySimpleContent(
            modifier = Modifier.setTestTag(SimpleContent),
            viewModel = viewModel,
            isActive = state == Running && contentState == Simple,
            forecast = forecast,
            selectedDailyForecast = selectedDailyForecast,
            weatherUnit = weatherUnit
        )
        JetWeatherfyDetailedContent(
            modifier = Modifier.setTestTag(DetailedContent),
            viewModel = viewModel,
            isActive = state == Running && contentState == Detailed,
            forecast = forecast,
            selectedDailyForecast = selectedDailyForecast,
            weatherUnit = weatherUnit
        )
        Message(
            modifier = Modifier
                .scale(loadingValue)
                .alpha(loadingValue)
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
                .scale(idleValue)
                .alpha(idleValue)
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
        }
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
