package com.pp.jetweatherfy.data.di

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
object DaoModule {

    @Provides
    @Singleton
    fun provideCityDao(): CityDao = FakeCityDao()

    @Provides
    @Singleton
    fun provideForecastDao(): ForecastDao = FakeForecastDao()
}