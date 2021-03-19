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
package com.pp.jetweatherfy.di

import com.pp.jetweatherfy.data.city.CityDao
import com.pp.jetweatherfy.data.city.FakeCityDao
import com.pp.jetweatherfy.data.forecast.FakeForecastDao
import com.pp.jetweatherfy.data.forecast.ForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideForecastDao(): ForecastDao {
        return FakeForecastDao()
    }

    @Singleton
    @Provides
    fun provideCityDao(): CityDao {
        return FakeCityDao()
    }
}
