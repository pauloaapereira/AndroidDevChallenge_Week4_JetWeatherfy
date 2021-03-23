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
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Loading
import com.pp.jetweatherfy.domain.JetWeatherfyState.LocationError
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
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

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5 * 1000
        }
    }

    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                locationProvider.removeLocationUpdates(locationCallback)
            }
        }
    }

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
        viewModel.setState(Loading)

        locationProvider.lastLocation
            .addOnSuccessListener { location ->
                selectCityFromLocation(location)
            }
            .addOnFailureListener {
                viewModel.setState(Idle)
            }
    }

    @SuppressLint("MissingPermission")
    private fun selectCityFromLocation(location: Location?) {
        when {
            location != null -> {
                geoCoder.getFromLocation(location.latitude, location.longitude, 1).firstOrNull()?.locality?.let { city ->
                    viewModel.selectCity(city, fromLocation = true)
                }
            }
            else -> {
                locationProvider.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    mainLooper
                )
                viewModel.setState(LocationError)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            when {
                viewModel.searchQuery.value.isNullOrBlank() -> viewModel.setState(Idle)
                else -> viewModel.setState(Running)
            }
        }
    }
}
