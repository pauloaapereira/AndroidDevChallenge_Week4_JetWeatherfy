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
import com.pp.jetweatherfy.R

enum class Weather(@StringRes val description: Int, @RawRes val animation: Int) {
    Sunny(
        description = R.string.sunny,
        animation = R.raw.sunny
    ),
    Cloudy(
        description = R.string.cloudy,
        animation = R.raw.cloudy
    ),
    Rainy(
        description = R.string.rainy,
        animation = R.raw.rainy
    ),
    Thunderstorm(
        description = R.string.thunderstorm,
        animation = R.raw.thunderstorm
    ),
    Windy(
        description = R.string.windy,
        animation = R.raw.windy
    )
}
