package com.pp.jetweatherfy.domain.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class HourlyForecast(
    val timestamp: String,
    val temperature: Int,
    val precipitationProbability: Int,
    val windSpeed: Int,
    val weather: Weather,
    val backgroundColor: Brush,
    val contentColor: Color
)