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
package com.pp.jetweatherfy.presentation.forecast.components.surface

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.presentation.utils.generateColorBasedOnForecast
import com.pp.jetweatherfy.presentation.utils.generateGradientFeel

@Composable
fun JetWeatherfySurface(selectedDailyForecast: DailyForecast, content: @Composable () -> Unit) {
    val backgroundColorFeel by animateColorAsState(
        targetValue = selectedDailyForecast.generateColorBasedOnForecast(),
        animationSpec = tween(400)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(generateGradientFeel(backgroundColorFeel)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
