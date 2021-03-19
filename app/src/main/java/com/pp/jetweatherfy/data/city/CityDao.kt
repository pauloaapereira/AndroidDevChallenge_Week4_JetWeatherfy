package com.pp.jetweatherfy.data.city

interface CityDao {
    suspend fun getCities(): List<String>
}