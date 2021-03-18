package com.pp.jetweatherfy.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pp.jetweatherfy.domain.models.HourlyForecast

fun Modifier.setForecastColor(forecast: HourlyForecast?, defaultColor: Color): Modifier {
    forecast?.backgroundColor?.let {
        return background(it)
    }
    return background(defaultColor)
}