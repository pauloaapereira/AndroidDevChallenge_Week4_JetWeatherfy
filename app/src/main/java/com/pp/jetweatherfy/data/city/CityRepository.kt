/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pp.jetweatherfy.data.city

import java.util.Locale
import javax.inject.Inject

class CityRepository @Inject constructor(private val cityDao: CityDao) : ICityRepository {

    override suspend fun getCities(query: String) = cityDao.getCities().filter {
        it.toLowerCase(Locale.getDefault()).startsWith(query.toLowerCase(Locale.getDefault()))
    }.sorted()

    override suspend fun getDefaultCity() = cityDao.getDefaultCity()

    override suspend fun addCity(city: String) = cityDao.addCity(formatCity(city))

    private fun formatCity(city: String): String = city.toLowerCase(Locale.getDefault()).split(" ").joinToString(" ") { it.capitalize(Locale.getDefault()) }
}
