package com.pp.jetweatherfy.fakes

import com.pp.jetweatherfy.domain.model.DailyForecast
import com.pp.jetweatherfy.domain.model.Forecast
import com.pp.jetweatherfy.domain.repositories.forecast.IForecastRepository

class FakeForecastRepository : IForecastRepository {

    private val lisbonForecast = Forecast(
        city = "Lisbon",
        dailyForecasts = listOf()
    )

    private val londonForecast = Forecast(
        city = "London",
        dailyForecasts = listOf(DailyForecast())
    )

    override suspend fun getForecast(city: String): Forecast {
        return when (city) {
            lisbonForecast.city -> lisbonForecast
            londonForecast.city -> londonForecast
            else -> Forecast()
        }
    }

}