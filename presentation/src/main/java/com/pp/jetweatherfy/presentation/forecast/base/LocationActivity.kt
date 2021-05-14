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
package com.pp.jetweatherfy.presentation.forecast.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.pp.jetweatherfy.presentation.utils.askPermissions
import com.pp.jetweatherfy.presentation.utils.hasPermissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class LocationActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            askPermissions(
                100,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    protected fun getLocation() {
        locationProvider.lastLocation
            .addOnSuccessListener {  location ->
                location?.let {
                    val cityName =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                            .firstOrNull()?.locality
                    if (cityName != null)
                        onLocationSuccess(cityName)
                    else
                        onLocationFailure()
                } ?: run {
                    locationProvider.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        mainLooper
                    )
                    onLocationFailure()
                }
            }
            .addOnFailureListener {
                onLocationFailure()
            }
    }

    abstract fun onLocationSuccess(cityName: String)
    abstract fun onLocationFailure()
    abstract fun onLocationRequestCanceled()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            onLocationRequestCanceled()
        }
    }
}
