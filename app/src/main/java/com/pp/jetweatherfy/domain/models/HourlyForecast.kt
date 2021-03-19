package com.pp.jetweatherfy.domain.models

import org.joda.time.DateTime
import java.util.Locale

data class HourlyForecast(
    val timestamp: String,
    val temperature: Int
) {

    private val timestampFormat = "K:mm a"

    val formattedTimestamp: String
        get() = DateTime.parse(timestamp).toString(timestampFormat).toUpperCase(Locale.getDefault())

}