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
package com.pp.jetweatherfy.data

import com.pp.jetweatherfy.domain.City
import com.pp.jetweatherfy.domain.Weather
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import org.joda.time.DateTime
import kotlin.random.Random

class FakeForecastDao : ForecastDao {

    override suspend fun getForecast(city: City) = getCityForecast(city)

    private fun getCityForecast(city: City) = Forecast(
        city = city.identification,
        dailyForecasts = getDailyForecasts()
    )

    private fun getDailyForecasts(): List<DailyForecast> {
        val today = DateTime.now()

        return (0..6).map { dayNumber ->
            val dayStartHour = if (dayNumber == 0) today.hourOfDay else 0
            val day = today.plusDays(dayNumber).withHourOfDay(dayStartHour)

            DailyForecast(
                timestamp = day.toString(),
                hourlyForecasts = getHourlyForecasts(day)
            )
        }
    }

    private fun getHourlyForecasts(day: DateTime): List<HourlyForecast> {
        val temperature = Random.nextInt(0, 35)
        val precipitationProb = Random.nextInt(0, 100)
        val windSpeed = Random.nextInt(0, 100)
        val description = when {
            precipitationProb > 85 && windSpeed > 50 -> Weather.Thunderstorm
            precipitationProb > 50 -> Weather.Rainy
            windSpeed > 40 -> Weather.Windy
            precipitationProb < 25 && temperature > 18 -> Weather.Cloudy
            else -> Weather.Sunny
        }
        return (day.hourOfDay..23).map { hourNumber ->
            val hour = day.withHourOfDay(hourNumber)

            HourlyForecast(
                timestamp = hour.toString(),
                temperature = temperature,
                precipitationProbability = precipitationProb,
                windSpeed = windSpeed,
                description = description
            )
        }
    }
}
