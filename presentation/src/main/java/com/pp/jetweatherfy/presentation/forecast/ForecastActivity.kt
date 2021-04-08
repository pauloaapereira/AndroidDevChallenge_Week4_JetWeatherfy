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
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.pp.jetweatherfy.presentation.base.LocationActivity
import com.pp.jetweatherfy.presentation.forecast.events.LocationViewEvent
import com.pp.jetweatherfy.presentation.navigation.NavigationDirections
import com.pp.jetweatherfy.presentation.navigation.NavigationManager
import com.pp.jetweatherfy.presentation.theme.JetWeatherfyTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForecastActivity : LocationActivity() {

    private val viewModel: ForecastViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    App()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.requestingLocation.collectLatest { isRequestingLocation ->
                if (isRequestingLocation) {
                    getLocation()
                }
            }
        }
    }

    override fun onLocationSuccess(cityName: String) {
        viewModel.onLocationEvent(LocationViewEvent.SetLocation(cityName))
    }

    override fun onLocationFailure() {
        viewModel.onLocationEvent(LocationViewEvent.LocationError)
    }

    override fun onLocationRequestCanceled() {
        viewModel.onLocationEvent(LocationViewEvent.PermissionsError)
    }
}

@ExperimentalAnimationApi
@Composable
fun App() {
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
            val viewModel =
                hiltNavGraphViewModel<ForecastViewModel>(backStackEntry = backStackEntry)
            ForecastScreen(viewModel)
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun AppLightPreview() {
    JetWeatherfyTheme {
        ProvideWindowInsets {
            App()
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun AppDarkPreview() {
    JetWeatherfyTheme(darkTheme = true) {
        ProvideWindowInsets {
            App()
        }
    }
}
