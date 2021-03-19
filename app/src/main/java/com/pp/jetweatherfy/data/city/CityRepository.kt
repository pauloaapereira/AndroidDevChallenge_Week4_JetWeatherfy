package com.pp.jetweatherfy.data.city

import java.util.Locale
import javax.inject.Inject

class CityRepository @Inject constructor(private val cityDao: CityDao) : ICityRepository {

    override suspend fun getCities(query: String) = cityDao.getCities().filter {
        it.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
    }.sorted()

    override suspend fun getDefaultCity() = cityDao.getDefaultCity()

}