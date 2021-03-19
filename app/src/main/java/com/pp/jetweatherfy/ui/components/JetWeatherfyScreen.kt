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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.utils.setForecastColor

@ExperimentalAnimationApi
@Composable
fun JetWeatherfyScreen(
    forecast: Forecast?,
    cities: List<String>,
    onCitySelected: (String) -> Unit
) {
    var selectedHourlyForecast = forecast?.getFirstHourlyForecast()
    val defaultBackgroundColor = MaterialTheme.colors.background
    val defaultContentColor = contentColorFor(defaultBackgroundColor)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .setForecastColor(selectedHourlyForecast, defaultBackgroundColor),
        contentColor = selectedHourlyForecast?.contentColor ?: defaultContentColor
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            JetWeatherfyTopBar(
                city = forecast?.city ?: "",
                onCitySelected = { city ->
                    onCitySelected(city)
                }
            )
            JetWeatherfyContent(
                forecast,
                selectedHourlyForecast,
                onHourlyForecastSelected = { selectedHourlyForecast = it }
            )
        }
    }
}
