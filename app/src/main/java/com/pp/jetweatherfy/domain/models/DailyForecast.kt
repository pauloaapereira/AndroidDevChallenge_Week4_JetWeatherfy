package com.pp.jetweatherfy.domain.models

data class DailyForecast(
    val timestamp: String,
    val hourlyForecasts: List<HourlyForecast> = listOf()
)