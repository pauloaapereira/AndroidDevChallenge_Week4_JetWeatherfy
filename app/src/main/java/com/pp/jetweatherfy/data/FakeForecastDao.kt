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

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pp.jetweatherfy.domain.City
import com.pp.jetweatherfy.domain.Weather
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.Forecast
import com.pp.jetweatherfy.domain.models.HourlyForecast
import com.pp.jetweatherfy.utils.darkenColor
import com.pp.jetweatherfy.utils.isDarkColor
import com.pp.jetweatherfy.utils.isLightColor
import com.pp.jetweatherfy.utils.lightenColor
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
        val windSpeed = Random.nextInt(0, 100)
        val precipitation = Random.nextInt(0, 100)
        val description = when {
            temperature > 21 && precipitation < 50 -> Weather.Sunny
            windSpeed > 40 -> Weather.Windy
            precipitation < 25 && temperature > 18 -> Weather.Cloudy
            precipitation > 50 -> Weather.Rainy
            precipitation > 60 && windSpeed > 35 -> Weather.Thunderstorm
            else -> Weather.Sunny
        }

        val colorFeel = generateColorFeel(temperature, windSpeed, precipitation)

        return (day.hourOfDay..23).map { hourNumber ->
            val hour = day.withHourOfDay(hourNumber)

            HourlyForecast(
                timestamp = hour.toString(),
                temperature = temperature,
                precipitationProbability = precipitation,
                windSpeed = windSpeed,
                description = description,
                backgroundColor = generateFeelGradient(colorFeel),
                contentColor = when {
                    colorFeel.isLightColor() -> colorFeel.darkenColor(.3f)
                    colorFeel.isDarkColor()  -> colorFeel.lightenColor(.3f)
                    else -> Color.Black
                }
            )
        }
    }

    private fun generateColorFeel(temperature: Int, windSpeed: Int, precipitation: Int) =
        Color(
            red = (temperature * 255 / 35f) / 255f,
            green = (windSpeed * 255 / 100f) / 255f,
            blue = (precipitation * 255 / 100f) / 255f
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