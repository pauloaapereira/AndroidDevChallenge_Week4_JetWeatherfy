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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.pp.jetweatherfy.data.city.CityRepository
import com.pp.jetweatherfy.data.city.FakeCityDao
import com.pp.jetweatherfy.data.forecast.FakeForecastDao
import com.pp.jetweatherfy.data.forecast.ForecastRepository
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.MainActivity
import com.pp.jetweatherfy.ui.components.topbar.JetWeatherfyTopBar
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import org.junit.Rule
import org.junit.Test

class TopBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Test
    fun actions_hidden_when_loading() {
        composeTestRule.setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    JetWeatherfyTopBar(
                        viewModel = ForecastViewModel(
                            ForecastRepository(FakeForecastDao()),
                            CityRepository(FakeCityDao())
                        ),
                        state = JetWeatherfyState.Loading,
                        contentState = ContentState.Simple,
                        onSetMyLocationClick = { }
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag("AppLogo").assertExists()
    }
}
