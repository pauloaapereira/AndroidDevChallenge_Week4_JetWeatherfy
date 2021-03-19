package com.pp.jetweatherfy.data.city

import java.util.Locale
import javax.inject.Inject

class CityRepository @Inject constructor(private val cityDao: CityDao) : ICityRepository {
    override suspend fun getCities(query: String) = filterCities(cityDao.getCities(), query)

    private fun filterCities(cities: List<String>, query: String) = cities.filter {
        it.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
    }
}