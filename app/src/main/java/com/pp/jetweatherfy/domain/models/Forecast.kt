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
package com.pp.jetweatherfy.domain.models

data class Forecast(
    var city: String = "",
    val dailyForecasts: List<DailyForecast> = listOf()
) {

    companion object {
        const val DailyTimestampFormat = "E, d MMM"
        const val HourlyTimestampFormat = "K:mm a"
    }

    fun getFirstDailyForecast() = dailyForecasts.firstOrNull()
}
