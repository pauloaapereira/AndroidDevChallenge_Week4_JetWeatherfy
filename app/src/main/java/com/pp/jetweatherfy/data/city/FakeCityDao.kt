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

class FakeCityDao : CityDao {

    private val cities = mutableListOf(
        "London",
        "New York",
        "Paris",
        "Moscow",
        "Tokyo",
        "Dubai",
        "Singapore",
        "Barcelona",
        "Los Angeles",
        "San Francisco",
        "Madrid",
        "Rome",
        "Chicago",
        "Toronto",
        "Abu Dhabi",
        "St. Petersburg",
        "Amsterdam",
        "Berlin",
        "Prague",
        "Lisbon",
        "Washington",
        "Istanbul",
        "Las Vegas",
        "Seoul",
        "Sydney",
        "Miami",
        "Munich",
        "Milan",
        "San Diego",
        "Bangkok",
        "Vienna",
        "Dublin",
        "Vancouver",
        "Boston",
        "Zurich",
        "Budapest",
        "Houston",
        "Seattle",
        "Montreal",
        "Hong Kong",
        "Frankfurt",
        "São Paulo",
        "Copenhagen",
        "Atlanta",
        "Buenos Aires"
    )

    override suspend fun getCities() = cities

    override suspend fun getDefaultCity() = "San Francisco"

    override suspend fun addCity(city: String) {
        if (!cities.contains(city)) {
            cities.add(city)
        }
    }
}
