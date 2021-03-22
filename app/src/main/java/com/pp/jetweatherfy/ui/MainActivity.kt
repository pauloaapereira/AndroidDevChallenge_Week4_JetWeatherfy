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
package com.pp.jetweatherfy.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState.Loading
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.ui.components.background.JetWeatherfySurface
import com.pp.jetweatherfy.ui.components.content.JetWeatherfyContent
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBar
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class MainActivity : ForecastActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    JetWeatherfy(
                        forecastViewModel = viewModel,
                        onSetMyLocationClick = {
                            getLocation()
                        }
                    )
                }
            }
        }

        getLocation()
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun JetWeatherfy(forecastViewModel: ForecastViewModel, onSetMyLocationClick: () -> Unit) {
    val state by forecastViewModel.state.observeAsState(Loading)
    val contentState by forecastViewModel.contentState.observeAsState(Simple)
    val weatherUnit by forecastViewModel.weatherUnit.observeAsState(WeatherUnit.METRIC)

    JetWeatherfySurface(viewModel = forecastViewModel) {
        JetWeatherfyTopBar(
            viewModel = forecastViewModel,
            state = state,
            contentState = contentState,
            weatherUnit = weatherUnit,
            onSetMyLocationClick = onSetMyLocationClick
        )
        JetWeatherfyContent(
            viewModel = forecastViewModel,
            state = state,
            contentState = contentState,
            weatherUnit = weatherUnit
        )
    }
}
