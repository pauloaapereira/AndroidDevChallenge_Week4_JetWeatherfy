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

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.Weather
import com.pp.jetweatherfy.utils.darkenColor
import com.pp.jetweatherfy.utils.isDarkColor
import com.pp.jetweatherfy.utils.isLightColor
import com.pp.jetweatherfy.utils.lightenColor
import org.joda.time.DateTime
import kotlin.random.Random

class FakeForecastDao : ForecastDao {

    private val maxTemperature = 35
    private val maxWindSpeed = 40
    private val maxPrecipitation = 100

    override suspend fun generateForecast() = Forecast(dailyForecasts = generateDailyForecasts())

    private fun generateDailyForecasts(): List<DailyForecast> {
        val today = DateTime.now().withMinuteOfHour(0)

        val temperature = Random.nextInt(0, maxTemperature)
        val windSpeed = Random.nextInt(0, maxWindSpeed)
        val precipitation = Random.nextInt(0, maxPrecipitation)
        val colorFeel = generateColorFeel(temperature, windSpeed, precipitation)

        val weather = when {
            temperature > maxTemperature / 2 && precipitation < maxPrecipitation / 2 -> Weather.Sunny
            temperature > 28 -> Weather.Sunny
            temperature > 21 && precipitation < 50 -> Weather.Sunny
            precipitation > 50 -> Weather.Rainy
            precipitation < 15 && temperature > 18 -> Weather.Cloudy
            precipitation > 60 && windSpeed > 35 -> Weather.Thunderstorm
            windSpeed > 40 || windSpeed > 20 && temperature < 15 -> Weather.Windy
            else -> Weather.Sunny
        }


        return (0..6).map { dayNumber ->
            val dayStartHour = if (dayNumber == 0) today.hourOfDay else 0
            val day = today.plusDays(dayNumber).withHourOfDay(dayStartHour)

            DailyForecast(
                timestamp = day.toString(),
                hourlyForecasts = generateHourlyForecasts(temperature, day),
                temperature = temperature,
                precipitationProbability = precipitation,
                windSpeed = windSpeed,
                weather = weather,
                backgroundColor = generateFeelGradient(colorFeel),
                contentColor = when {
                    colorFeel.isLightColor() -> colorFeel.darkenColor(.3f)
                    colorFeel.isDarkColor() -> colorFeel.lightenColor(.3f)
                    else -> Color.Black
                }
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
                Random.nextInt(0, maxTemperature)

            HourlyForecast(
                timestamp = hour.toString(),
                temperature = temperature
            )
        }
    }

    private fun generateColorFeel(temperature: Int, windSpeed: Int, precipitation: Int) =
        Color(
            red = (temperature * 255 / maxTemperature.toFloat()) / 255f,
            green = (windSpeed * 255 / maxWindSpeed.toFloat()) / 255f,
            blue = (precipitation * 255 / maxPrecipitation.toFloat()) / 255f
        )

    private fun generateFeelGradient(baseColor: Color): Brush {
        val lightenFactor = .3f

        return Brush.verticalGradient(
            colors = listOf(
                baseColor,
                baseColor.lightenColor(lightenFactor)
            )
        )
    }
}
