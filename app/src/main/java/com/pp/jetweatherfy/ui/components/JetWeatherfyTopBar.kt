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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun JetWeatherfyTopBar(viewModel: ForecastViewModel, cities: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(MediumDimension),
        verticalArrangement = Arrangement.spacedBy(BigDimension),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title()
        JetWeatherfySearchBar(viewModel = viewModel, cities = cities)
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