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

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pp.jetweatherfy.domain.models.City
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.domain.models.Weather
import com.pp.jetweatherfy.domain.models.Weather.Sunny
import com.pp.jetweatherfy.utils.darkenColor
import com.pp.jetweatherfy.utils.isDarkColor
import com.pp.jetweatherfy.utils.isLightColor
import com.pp.jetweatherfy.utils.lightenColor
import org.joda.time.DateTime
import kotlin.random.Random

class ForecastController {

    companion object {
        const val MaxTemperature = 35
        const val MaxWindSpeed = 40
        const val MaxPrecipitation = 100
    }

    fun generateForecast(city: City) = Forecast(
        city = city,
        dailyForecasts = generateDailyForecasts()
    )

    private fun generateDailyForecasts(): List<DailyForecast> {
        val today = DateTime.now()

        return (0..6).map { dayNumber ->
            val dayStartHour = if (dayNumber == 0) today.hourOfDay else 0
            val day = today.plusDays(dayNumber).withHourOfDay(dayStartHour)

            DailyForecast(
                timestamp = day.toString(),
                hourlyForecasts = generateHourlyForecasts(day)
            )
        }
    }

    private fun generateHourlyForecasts(day: DateTime): List<HourlyForecast> {
        val temperature = Random.nextInt(0, MaxTemperature)
        val windSpeed = Random.nextInt(0, MaxWindSpeed)
        val precipitation = Random.nextInt(0, MaxPrecipitation)

        val weather = when {
            temperature > MaxTemperature / 2 && precipitation < MaxPrecipitation / 2 -> Sunny
            temperature > 28 -> Sunny
            temperature > 21 && precipitation < 50 -> Sunny
            precipitation > 50 -> Weather.Rainy
            precipitation < 15 && temperature > 18 -> Weather.Cloudy
            precipitation > 60 && windSpeed > 35 -> Weather.Thunderstorm
            windSpeed > 40 || windSpeed > 20 && temperature < 15 -> Weather.Windy
            else -> Sunny
        }

        val colorFeel = generateColorFeel(temperature, windSpeed, precipitation)

        return (day.hourOfDay..23).map { hourNumber ->
            val hour = day.withHourOfDay(hourNumber)

            HourlyForecast(
                timestamp = hour.toString(),
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

    private fun generateColorFeel(temperature: Int, windSpeed: Int, precipitation: Int) =
        Color(
            red = (temperature * 255 / MaxTemperature.toFloat()) / 255f,
            green = (windSpeed * 255 / MaxWindSpeed.toFloat()) / 255f,
            blue = (precipitation * 255 / MaxPrecipitation.toFloat()) / 255f
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
