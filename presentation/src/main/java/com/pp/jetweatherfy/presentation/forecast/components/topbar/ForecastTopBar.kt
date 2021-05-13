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
package com.pp.jetweatherfy.presentation.forecast.components.topbar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.statusBarsPadding
import com.pp.jetweatherfy.presentation.R
import com.pp.jetweatherfy.presentation.forecast.state.ForecastViewState
import com.pp.jetweatherfy.presentation.forecast.state.LocationViewState
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus
import com.pp.jetweatherfy.presentation.forecast.state.ViewType
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Detailed
import com.pp.jetweatherfy.presentation.forecast.state.ViewType.Simple
import com.pp.jetweatherfy.presentation.forecast.state.WeatherUnit
import com.pp.jetweatherfy.presentation.forecast.state.WeatherUnit.Imperial
import com.pp.jetweatherfy.presentation.forecast.state.WeatherUnit.Metric
import com.pp.jetweatherfy.presentation.theme.BigDimension
import com.pp.jetweatherfy.presentation.theme.MediumDimension
import com.pp.jetweatherfy.presentation.theme.SmallDimension
import com.pp.jetweatherfy.presentation.utils.AnimationDuration

@Composable
fun ForecastTopBar(
    forecastState: ForecastViewState,
    locationState: LocationViewState,
    onWeatherUnitToggled: (WeatherUnit) -> Unit,
    onViewTypeToggled: (ViewType) -> Unit,
    onSetLocationClick: () -> Unit,
    onQueryTyping: (String) -> Unit,
    onCitySelected: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(MediumDimension),
        verticalArrangement = Arrangement.spacedBy(BigDimension),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionsAndLogo(
            viewType = forecastState.viewType,
            viewStatus = forecastState.viewStatus,
            weatherUnit = forecastState.weatherUnit,
            onWeatherUnitToggled = onWeatherUnitToggled,
            onViewTypeToggled = onViewTypeToggled,
            onSetLocationClick = onSetLocationClick
        )
        ForecastSearchBar(
            query = locationState.query,
            cities = locationState.cities,
            viewStatus = forecastState.viewStatus,
            onQueryTyping = { onQueryTyping(it) },
            onItemSelected = { onCitySelected(it) }
        )
    }
}

@Composable
private fun ActionsAndLogo(
    viewType: ViewType,
    viewStatus: ViewStatus,
    weatherUnit: WeatherUnit,
    onWeatherUnitToggled: (WeatherUnit) -> Unit,
    onViewTypeToggled: (ViewType) -> Unit,
    onSetLocationClick: () -> Unit
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
                isActive = viewStatus == ViewStatus.Running,
                weatherUnit = weatherUnit,
                onWeatherUnitToggled = { onWeatherUnitToggled(it) }
            )
            ViewTypeToggle(
                isActive = viewStatus == ViewStatus.Running,
                viewType = viewType,
                onViewTypeToggled = { onViewTypeToggled(it) }
            )
            GetLocationButton(
                isActive = viewStatus == ViewStatus.Running || viewStatus == ViewStatus.Idle,
                onSetLocationClick = onSetLocationClick
            )
        }
    }
}

@Composable
private fun WeatherUnitToggle(
    isActive: Boolean,
    weatherUnit: WeatherUnit,
    onWeatherUnitToggled: (WeatherUnit) -> Unit
) {
    val buttonTransition =
        updateTransition(targetState = isActive, label = "WeatherUnitToggleTransition")
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconButton(
        modifier = Modifier
            .alpha(buttonAlpha),
        onClick = { onWeatherUnitToggled(if (weatherUnit == Metric) Imperial else Metric) },
        enabled = isActive
    ) {
        Icon(
            painter = painterResource(id = if (weatherUnit == Metric) R.drawable.centigrade else R.drawable.fahrenheit),
            contentDescription = stringResource(R.string.get_my_location),
            modifier = Modifier.requiredSize(
                BigDimension
            )
        )
    }
}

@Composable
private fun ViewTypeToggle(
    isActive: Boolean,
    viewType: ViewType,
    onViewTypeToggled: (ViewType) -> Unit
) {
    val buttonTransition =
        updateTransition(targetState = isActive, label = "ViewTypeToggleTransition")
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconToggleButton(
        modifier = Modifier
            .alpha(buttonAlpha),
        checked = viewType == Detailed,
        onCheckedChange = {
            onViewTypeToggled(
                if (viewType == Simple) Detailed else Simple
            )
        },
        enabled = isActive
    ) {
        val icon =
            if (viewType == Detailed) R.drawable.ic_list else R.drawable.detailed_view
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
fun GetLocationButton(isActive: Boolean, onSetLocationClick: () -> Unit) {
    val buttonTransition =
        updateTransition(targetState = isActive, label = "GetLocationButtonTransition")
    val buttonAlpha by topBarButtonTransition(transition = buttonTransition)

    IconButton(
        modifier = Modifier.alpha(buttonAlpha),
        onClick = { onSetLocationClick() },
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
        },
        label = "TopBarButtonTransition"
    ) { isActive ->
        when (isActive) {
            true -> 1f
            false -> 0f
        }
    }
}
