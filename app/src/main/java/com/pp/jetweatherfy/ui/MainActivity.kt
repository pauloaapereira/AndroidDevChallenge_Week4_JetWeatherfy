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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.pp.jetweatherfy.ui.components.JetWeatherfyContent
import com.pp.jetweatherfy.ui.components.JetWeatherfySurface
import com.pp.jetweatherfy.ui.components.JetWeatherfyTopBar
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ForecastViewModel>()

    @ExperimentalComposeUiApi
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
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun JetWeatherfy(forecastViewModel: ForecastViewModel) {
    JetWeatherfySurface(viewModel = forecastViewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            JetWeatherfyTopBar(viewModel = forecastViewModel)
            JetWeatherfyContent(viewModel = forecastViewModel)
        }
    }
}
