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
package com.pp.jetweatherfy.data.forecast

import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.Weather
import org.joda.time.DateTime
import kotlin.random.Random

class FakeForecastDao : ForecastDao {

    companion object {
        const val MaxTemperature = 35
        const val MaxWindSpeed = 40
        const val MaxPrecipitation = 100
    }

    override suspend fun generateForecast() = Forecast(dailyForecasts = generateDailyForecasts())

    private fun generateDailyForecasts(): List<DailyForecast> {
        val today = DateTime.now().withMinuteOfHour(0)

        return (0..6).map { dayNumber ->
            val dayStartHour = if (dayNumber == 0) today.hourOfDay else 0
            val day = today.plusDays(dayNumber).withHourOfDay(dayStartHour)

            val temperature = Random.nextInt(0, MaxTemperature)
            val windSpeed = Random.nextInt(0, MaxWindSpeed)
            val precipitation = Random.nextInt(0, MaxPrecipitation)
            val weather = generateWeather(temperature, windSpeed, precipitation)

            DailyForecast(
                timestamp = day.toString(),
                hourlyForecasts = generateHourlyForecasts(temperature, day),
                temperature = temperature,
                precipitationProbability = precipitation,
                windSpeed = windSpeed,
                weather = weather
            )
        }
    }

    private fun generateHourlyForecasts(
        firstTemperature: Int,
        day: DateTime
    ): List<HourlyForecast> {
        val firstHour = day.hourOfDay

        return (firstHour..23).map { hourNumber ->
            val hour = day.withHourOfDay(hourNumber)
            val temperature = if (hourNumber == firstHour)
                firstTemperature
            else
                Random.nextInt(0, MaxTemperature)
            val windSpeed = Random.nextInt(0, MaxWindSpeed)
            val precipitation = Random.nextInt(0, MaxPrecipitation)
            val weather = generateWeather(temperature, windSpeed, precipitation)

            HourlyForecast(
                timestamp = hour.toString(),
                temperature = temperature,
                weather = weather
            )
        }
    }

    private fun generateWeather(temperature: Int, windSpeed: Int, precipitation: Int) =
        when {
            temperature > MaxTemperature / 2 && precipitation < MaxPrecipitation / 2 -> Weather.Sunny
            temperature > 28 -> Weather.Sunny
            temperature > 21 && precipitation < 50 -> Weather.Sunny
            precipitation > 50 -> Weather.Rainy
            precipitation < 15 && temperature > 18 -> Weather.Cloudy
            precipitation > 60 && windSpeed > 35 -> Weather.Thunderstorm
            windSpeed > 40 || windSpeed > 20 && temperature < 15 -> Weather.Windy
            else -> Weather.Sunny
        }
}
