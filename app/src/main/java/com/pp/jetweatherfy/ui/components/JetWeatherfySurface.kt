package com.pp.jetweatherfy.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.utils.setForecastColor

@Composable
fun JetWeatherfySurface(dailyForecast: DailyForecast?, content: @Composable () -> Unit) {
    val defaultBackgroundColor = MaterialTheme.colors.background
    val defaultContentColor = contentColorFor(defaultBackgroundColor)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .setForecastColor(dailyForecast),
        contentColor = dailyForecast?.contentColor ?: defaultContentColor,
        content = content
    )
}