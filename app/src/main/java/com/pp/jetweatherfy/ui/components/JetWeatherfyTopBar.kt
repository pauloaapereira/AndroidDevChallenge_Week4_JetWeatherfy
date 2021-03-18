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
package com.pp.jetweatherfy.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.City
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension
import com.pp.jetweatherfy.utils.Curtain
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun JetWeatherfyTopBar(city: City, onCitySelected: (City) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(MediumDimension),
        verticalArrangement = Arrangement.spacedBy(BigDimension)
    ) {
        Title()
        Location(city, onCitySelected)
    }
}

@Composable
private fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.app_name),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2
    )
}

@ExperimentalAnimationApi
@Composable
private fun Location(city: City, onCitySelected: (City) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(SmallDimension),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = stringResource(R.string.location),
            modifier = Modifier.padding(top = SmallDimension * .85f)
        )
        CitySelection(city, onCitySelected)
    }
}

@ExperimentalAnimationApi
@Composable
private fun CitySelection(city: City, onCitySelected: (City) -> Unit) {
    val citySelectionScope = rememberCoroutineScope()
    var isCurtainOpened by remember { mutableStateOf(false) }

    fun closeCurtain() {
        val curtainCloseDelay = 250L

        citySelectionScope.launch {
            delay(curtainCloseDelay)
            isCurtainOpened = false
        }
    }

    fun selectCity(city: City) {
        if (isCurtainOpened) {
            onCitySelected(city)
            closeCurtain()
        }
    }

    Curtain(
        openedFromOutside = isCurtainOpened,
        foldingDuration = 100,
        mainCell = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SmallDimension),
                verticalAlignment = BiasAlignment.Vertical(.6f),
                modifier = Modifier.clickable { isCurtainOpened = true }
            ) {
                Text(text = city.identification, style = MaterialTheme.typography.subtitle1)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = stringResource(id = R.string.choose_city),
                    modifier = Modifier
                )
            }
        },
        foldCells = listOf(
            {
                CityItem(selectedCity = city, city = City.SanFrancisco) {
                    selectCity(City.SanFrancisco)
                }
            },
            {
                CityItem(selectedCity = city, city = City.Lisbon) {
                    selectCity(City.Lisbon)
                }
            },
            {
                CityItem(selectedCity = city, city = City.London) {
                    selectCity(City.London)
                }
            }
        )
    )
}

@ExperimentalAnimationApi
@Composable
fun CityItem(selectedCity: City, city: City, onSelected: () -> Unit) {
    val isSelected = selectedCity == city

    Row(
        horizontalArrangement = Arrangement.spacedBy(SmallDimension),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onSelected() }
    ) {
        Text(
            text = city.identification,
            style = MaterialTheme.typography.subtitle1
        )
        AnimatedVisibility(visible = isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = stringResource(R.string.selected)
            )
        }
    }
}
