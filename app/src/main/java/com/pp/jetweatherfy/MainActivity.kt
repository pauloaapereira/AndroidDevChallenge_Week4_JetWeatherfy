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
package com.pp.jetweatherfy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.pp.jetweatherfy.domain.City
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ForecastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetWeatherfyTheme {
                JetWeatherfy(viewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun JetWeatherfy(forecastViewModel: ForecastViewModel) {
    val forecast by forecastViewModel.forecast.observeAsState(null)

    if (forecast == null)
        forecastViewModel.getForecast(City.Lisbon)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                forecast?.dailyForecasts?.firstOrNull()?.hourlyForecasts?.firstOrNull()?.description?.backgroundColor
                    ?: Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.background,
                            MaterialTheme.colors.background
                        )
                    )
            ).clickable { forecastViewModel.getForecast(City.London) }
    ) {
        Text(text = "Ready... Set... GO!")
    }
}
