package com.pp.jetweatherfy.domain.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime

data class DailyForecast(
    val timestamp: String,
    val hourlyForecasts: List<HourlyForecast> = listOf(),
    val temperature: Int,
    val precipitationProbability: Int,
    val windSpeed: Int,
    val weather: Weather,
    val backgroundColor: Brush,
    val contentColor: Color
) {

    private val timestampFormat = "E, d MMM"

    val formattedTimestamp: String
        get() = DateTime.parse(timestamp).toString(timestampFormat)

}