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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.pp.jetweatherfy.data.city.CityRepository
import com.pp.jetweatherfy.data.city.FakeCityDao
import com.pp.jetweatherfy.data.forecast.FakeForecastDao
import com.pp.jetweatherfy.data.forecast.ForecastRepository
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Loading
import com.pp.jetweatherfy.domain.JetWeatherfyState.LocationError
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.domain.WeatherUnit
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.MainActivity
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBar
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.GetLocationButton
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.SearchBar
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.SearchBarClearButton
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.ViewTypeToggle
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.WeatherUnitToggle
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBarTestHelper.getTestTag
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class JetWeatherfyTopBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun startApp(state: JetWeatherfyState) {
        composeTestRule.setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    JetWeatherfyTopBar(
                        viewModel = ForecastViewModel(
                            ForecastRepository(FakeForecastDao()),
                            CityRepository(FakeCityDao())
                        ),
                        state = state,
                        contentState = ContentState.Simple,
                        weatherUnit = WeatherUnit.METRIC,
                        onSetMyLocationClick = { }
                    )
                }
            }
        }
    }

    @Test
    fun actions_hidden_when_loading() {
        startApp(Loading)
        composeTestRule.onNodeWithTag(getTestTag(WeatherUnitToggle)).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(getTestTag(ViewTypeToggle)).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(getTestTag(GetLocationButton)).assertIsNotEnabled()
    }

    @Test
    fun actions_shown_when_running() {
        startApp(Running)
        composeTestRule.onNodeWithTag(getTestTag(WeatherUnitToggle)).assertIsEnabled()
        composeTestRule.onNodeWithTag(getTestTag(ViewTypeToggle)).assertIsEnabled()
        composeTestRule.onNodeWithTag(getTestTag(GetLocationButton)).assertIsEnabled()
    }

    @Test
    fun location_button_shown_when_idle() {
        startApp(Idle)
        composeTestRule.onNodeWithTag(getTestTag(WeatherUnitToggle)).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(getTestTag(ViewTypeToggle)).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(getTestTag(GetLocationButton)).assertIsEnabled()
    }

    @Test
    fun searchbar_enabled_when_idle() {
        startApp(Idle)
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).assertIsEnabled()
    }

    @Test
    fun searchbar_enabled_when_running() {
        startApp(Running)
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).assertIsEnabled()
    }

    @Test
    fun searchbar_disabled_when_loading() {
        startApp(Loading)
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).assertIsNotEnabled()
    }

    @Test
    fun searchbar_disabled_when_location_error() {
        startApp(LocationError)
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).assertIsNotEnabled()
    }

    @Test
    fun searchbar_cleared_when_clicking_on_x() {
        startApp(Running)
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).performTextInput("Teste")
        composeTestRule.onNodeWithTag(getTestTag(SearchBarClearButton)).performClick()
        composeTestRule.onNodeWithTag(getTestTag(SearchBar)).assertTextEquals("")
    }
}
