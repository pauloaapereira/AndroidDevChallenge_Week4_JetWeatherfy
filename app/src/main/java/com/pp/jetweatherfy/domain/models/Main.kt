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
package com.pp.jetweatherfy.domain.models

import com.squareup.moshi.Json

data class Main(
    @Json(name = "feels_like")
    val feelsLike: Double? = null,
    @Json(name = "grnd_level")
    val grndLevel: Int? = null,
    @Json(name = "humidity")
    val humidity: Int? = null,
    @Json(name = "pressure")
    val pressure: Int? = null,
    @Json(name = "sea_level")
    val seaLevel: Int? = null,
    @Json(name = "temp")
    val temp: Double? = null,
    @Json(name = "temp_kf")
    val tempKf: Double? = null,
    @Json(name = "temp_max")
    val tempMax: Double? = null,
    @Json(name = "temp_min")
    val tempMin: Double? = null
)
