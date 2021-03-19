package com.pp.jetweatherfy.data.city

interface ICityRepository {
    suspend fun getCities(query: String): List<String>
    suspend fun getDefaultCity(): String
}