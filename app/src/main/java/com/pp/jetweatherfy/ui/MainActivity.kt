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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.pp.jetweatherfy.ui.components.JetWeatherfyScreen
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ForecastViewModel>()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    JetWeatherfy(viewModel)
                }
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun JetWeatherfy(forecastViewModel: ForecastViewModel) {
    val cities by forecastViewModel.cities.observeAsState(listOf())
    val forecast by forecastViewModel.forecast.observeAsState()

    JetWeatherfyScreen(forecast = forecast, cities = cities) { city ->
        forecastViewModel.selectCity(city)
    }
}
