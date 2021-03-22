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
import androidx.compose.ui.test.onNodeWithText
import com.pp.jetweatherfy.data.city.ICityRepository
import com.pp.jetweatherfy.data.forecast.IForecastRepository
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.JetWeatherfy
import com.pp.jetweatherfy.ui.MainActivity
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class TopBarTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var cityRepository: ICityRepository

    @Inject
    lateinit var forecastRepository: IForecastRepository

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            JetWeatherfyTheme {
                ProvideWindowInsets {
                    JetWeatherfy(
                        forecastViewModel = ForecastViewModel(
                            forecastRepository,
                            cityRepository
                        ),
                        onSetMyLocationClick = {}
                    )
                }
            }
        }
    }

    @Test
    fun app_logo_initially_exists() {
        val appName = composeTestRule.activity.getString(R.string.app_name)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(appName).assertExists()
    }
}
