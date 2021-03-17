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
package com.pp.jetweatherfy.domain

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.ui.theme.grayish_blue_900
import com.pp.jetweatherfy.ui.theme.white_50

enum class Weather(@StringRes val description: Int, @RawRes val animation: Int, val backgroundColor: Brush, val contentColor: Color) {
    Sunny(
        description = R.string.sunny,
        animation = R.raw.sunny,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF2994A),
                Color(0xFFF2C94C)
            )
        ),
        contentColor = grayish_blue_900
    ),
    Cloudy(
        description = R.string.cloudy,
        animation = R.raw.cloudy,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF8e9eab),
                Color(0xFFeef2f3)
            )
        ),
        contentColor = grayish_blue_900
    ),
    Rainy(
        description = R.string.rainy,
        animation = R.raw.rainy,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF457fca),
                Color(0xFF5691c8)
            )
        ),
        contentColor = white_50
    ),
    Thunderstorm(
        description = R.string.thunderstorm,
        animation = R.raw.thunderstorm,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1e3c72),
                Color(0xFF2a5298)
            )
        ),
        contentColor = white_50
    ),
    Windy(
        description = R.string.windy,
        animation = R.raw.windy,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF11998e),
                Color(0xFF38ef7d)
            )
        ),
        contentColor = grayish_blue_900
    )
}
