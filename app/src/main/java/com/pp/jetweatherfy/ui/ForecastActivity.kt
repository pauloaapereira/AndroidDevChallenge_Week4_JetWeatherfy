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
package com.pp.jetweatherfy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.pp.jetweatherfy.utils.askPermissions
import com.pp.jetweatherfy.utils.hasPermissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class ForecastActivity : AppCompatActivity() {

    val viewModel by viewModels<ForecastViewModel>()

    @Inject
    lateinit var locationProvider: FusedLocationProviderClient

    @Inject
    lateinit var geoCoder: Geocoder

    protected fun getLocation() {
        if (hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            pingLocationProvider()
        } else {
            askPermissions(
                100,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun pingLocationProvider() {
        locationProvider.lastLocation.addOnSuccessListener {
            geoCoder.getFromLocation(it.latitude, it.longitude, 1).firstOrNull()?.locality?.let { city ->
                viewModel.selectCityFromLocation(city)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getLocation()
    }
}
