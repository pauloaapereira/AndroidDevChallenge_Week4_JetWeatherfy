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
package com.pp.jetweatherfy.ui.components.topbar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.ContentState.Detailed
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.domain.WeatherUnit.IMPERIAL
import com.pp.jetweatherfy.domain.WeatherUnit.METRIC
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.components.content.AnimationDuration
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun JetWeatherfyTopBar(
    viewModel: ForecastViewModel,
    state: JetWeatherfyState,
    contentState: ContentState,
    weatherUnit: WeatherUnit,
    onSetMyLocationClick: () -> Unit
) {
    val cities by viewModel.cities.observeAsState(listOf())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(MediumDimension),
        verticalArrangement = Arrangement.spacedBy(BigDimension),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoBar(
            viewModel = viewModel,
            state = state,
            contentState = contentState,
            weatherUnit = weatherUnit,
            onSetMyLocationClick = onSetMyLocationClick
        )
        JetWeatherfySearchBar(viewModel = viewModel, cities = cities, state = state)
    }
}

@ExperimentalAnimationApi
@Composable
private fun LogoBar(
    viewModel: ForecastViewModel,
    state: JetWeatherfyState,
    contentState: ContentState,
    weatherUnit: WeatherUnit,
    onSetMyLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SmallDimension, end = SmallDimension),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.subtitle1
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(SmallDimension),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherUnitToggle(
                viewModel = viewModel,
                isActive = state == Running,
                weatherUnit = weatherUnit
            )
            ViewTypeToggle(
                viewModel = viewModel,
                isActive = state == Running,
                contentState = contentState
            )
            MyLocationButton(
                isActive = state == Running || state == Idle,
                onSetMyLocationClick = onSetMyLocationClick
            )
        }
    }
}

@Composable
private fun WeatherUnitToggle(
    viewModel: ForecastViewModel,
    isActive: Boolean,
    weatherUnit: WeatherUnit
) {
    val buttonTransition = updateTransition(targetState = isActive)
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconButton(
        modifier = Modifier
            .alpha(buttonAlpha)
            .semantics { testTag = "MyLocationButton" },
        onClick = { viewModel.setWeatherUnit(if (weatherUnit == METRIC) IMPERIAL else METRIC) },
        enabled = isActive
    ) {
        Icon(
            painter = painterResource(id = if (weatherUnit == METRIC) R.drawable.centigrade else R.drawable.fahrenheit),
            contentDescription = stringResource(R.string.get_my_location),
            modifier = Modifier.requiredSize(
                BigDimension
            )
        )
    }
}

@Composable
private fun ViewTypeToggle(
    viewModel: ForecastViewModel,
    isActive: Boolean,
    contentState: ContentState
) {
    val buttonTransition = updateTransition(targetState = isActive)
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconToggleButton(
        modifier = Modifier
            .alpha(buttonAlpha)
            .semantics { testTag = "ViewTypeToggle" },
        checked = contentState == Detailed,
        onCheckedChange = {
            viewModel.setContentState(
                if (contentState == Simple) Detailed else Simple
            )
        },
        enabled = isActive
    ) {
        val icon =
            if (contentState == Detailed) R.drawable.ic_list else R.drawable.detailed_view
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(R.string.toggle_view_type),
            modifier = Modifier.requiredSize(
                BigDimension
            )
        )
    }
}

@Composable
fun MyLocationButton(isActive: Boolean, onSetMyLocationClick: () -> Unit) {
    val buttonTransition = updateTransition(targetState = isActive)
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconButton(
        modifier = Modifier
            .alpha(buttonAlpha)
            .semantics { testTag = "MyLocationButton" },
        onClick = { onSetMyLocationClick() },
        enabled = isActive
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_my_location),
            contentDescription = stringResource(R.string.get_my_location)
        )
    }
}

@Composable
fun topBarButtonTransition(
    transition: Transition<Boolean>
): State<Float> {
    return transition.animateFloat(
        transitionSpec = {
            tween(
                AnimationDuration / 4,
                easing = FastOutSlowInEasing
            )
        }
    ) { isActive ->
        when (isActive) {
            true -> 1f
            false -> 0f
        }
    }
}
