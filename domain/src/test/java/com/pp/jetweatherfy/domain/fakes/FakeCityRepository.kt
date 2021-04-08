package com.pp.jetweatherfy.domain.fakes

import com.pp.jetweatherfy.domain.repositories.cities.ICityRepository
import java.util.Locale

class FakeCityRepository : ICityRepository {
    private val cities = mutableListOf(
        "San Francisco",
        "London",
        "New York",
        "Paris",
        "Moscow",
        "Tokyo",
        "Dubai",
        "Toronto"
    )

    override suspend fun getCities(query: String): List<String> {
        return cities.filter {
            it.toLowerCase(Locale.getDefault())
                .startsWith(query.toLowerCase(Locale.getDefault()))
        }
    }

    override suspend fun addCity(city: String) {
        if (!cities.contains(city))
            cities.add(city)
    }
}