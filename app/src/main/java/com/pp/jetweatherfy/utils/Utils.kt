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
package com.pp.jetweatherfy.utils

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.pp.jetweatherfy.domain.models.DailyForecast

fun Modifier.setForecastColor(forecast: DailyForecast?): Modifier = composed {
    forecast?.backgroundColor?.let {
        background(it)
    } ?: run {
        background(MaterialTheme.colors.background)
    }
}
