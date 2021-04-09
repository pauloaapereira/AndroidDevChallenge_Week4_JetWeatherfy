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
package com.pp.jetweatherfy.domain.fakes

import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.domain.model.Forecast
import com.pp.jetweatherfy.domain.repositories.forecast.IForecastRepository

class FakeForecastRepository : IForecastRepository {

    private val lisbonForecast = Forecast(
        city = "Lisbon",
        dailyForecasts = listOf()
    )

    private val londonForecast = Forecast(
        city = "London",
        dailyForecasts = listOf(DailyForecast())
    )

    override suspend fun getForecast(city: String): Forecast {
        return when (city) {
            lisbonForecast.city -> lisbonForecast
            londonForecast.city -> londonForecast
            else -> Forecast()
        }
    }
}
