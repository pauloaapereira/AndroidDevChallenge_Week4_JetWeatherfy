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
