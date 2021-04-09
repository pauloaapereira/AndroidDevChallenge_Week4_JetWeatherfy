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
package com.pp.jetweatherfy.presentation.forecast

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.pp.jetweatherfy.presentation.forecast.base.LocationActivity
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent
import com.pp.jetweatherfy.presentation.forecast.navigation.NavigationDirections
import com.pp.jetweatherfy.presentation.forecast.navigation.NavigationManager
import com.pp.jetweatherfy.presentation.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class ForecastActivity : LocationActivity() {

    private var forecastViewModel: ForecastViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    App(
                        onLocationRequested = { viewModel ->
                            if (forecastViewModel == null)
                                forecastViewModel = viewModel

                            getLocation()
                        }
                    )
                }
            }
        }
    }

    override fun onLocationSuccess(cityName: String) {
        forecastViewModel?.onLocationEvent(LocationViewEvent.SetLocation(cityName))
    }

    override fun onLocationFailure() {
        forecastViewModel?.onLocationEvent(LocationViewEvent.LocationError)
    }

    override fun onLocationRequestCanceled() {
        forecastViewModel?.onLocationEvent(LocationViewEvent.PermissionsError)
    }
}

@Composable
fun App(onLocationRequested: (ForecastViewModel) -> Unit = {}) {
    val navController = rememberNavController()

    NavigationManager.command.collectAsState().value.also { command ->
        if (command.destination.isNotEmpty()) {
            navController.navigate(command.destination)
        }
    }

    NavHost(
        navController,
        startDestination = NavigationDirections.Forecast.destination
    ) {
        composable(NavigationDirections.Forecast.destination) { backStackEntry ->
            val viewModel = hiltNavGraphViewModel<ForecastViewModel>(backStackEntry = backStackEntry)
            ForecastScreen(viewModel, onLocationRequested = { onLocationRequested(viewModel) })
        }
    }
}

@Preview
@Composable
fun AppLightPreview() {
    JetWeatherfyTheme {
        ProvideWindowInsets {
            App()
        }
    }
}

@Preview
@Composable
fun AppDarkPreview() {
    JetWeatherfyTheme(darkTheme = true) {
        ProvideWindowInsets {
            App()
        }
    }
}
