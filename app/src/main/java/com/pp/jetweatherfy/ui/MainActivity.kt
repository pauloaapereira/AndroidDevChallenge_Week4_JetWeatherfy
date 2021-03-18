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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pp.jetweatherfy.domain.City
import com.pp.jetweatherfy.ui.theme.JetWeatherfyTheme
import com.pp.jetweatherfy.utils.Curtain
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ForecastViewModel>()

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
@Composable
fun JetWeatherfy(forecastViewModel: ForecastViewModel) {
    val forecast by forecastViewModel.forecast.observeAsState(null)
    var isCurtainOpened by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Curtain(
                    openedFromOutside = isCurtainOpened,
                    mainCell = {
                        Card(Modifier.clickable { isCurtainOpened = true }) {
                            Text(text = "Choose your city")
                        }
                    },
                    foldCells = listOf(
                        {
                            Button(onClick = { isCurtainOpened = false }) {
                                Text(text = City.SanFrancisco.identification)
                            }
                        },
                        {
                            Button(onClick = { isCurtainOpened = false }) {
                                Text(text = City.Lisbon.identification)
                            }
                        },
                        {
                            Button(onClick = { isCurtainOpened = false }) {
                                Text(text = City.London.identification)
                            }
                        }
                    )
                )
            }
        }
    }
}