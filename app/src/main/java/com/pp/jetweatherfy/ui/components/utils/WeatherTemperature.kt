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
package com.pp.jetweatherfy.ui.components.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R

@Composable
fun WeatherTemperature(
    temperature: Int,
    maxTemperature: Int? = null,
    minTemperature: Int? = null,
    celsius: Boolean = true,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    temperatureStyle: TextStyle = MaterialTheme.typography.h1,
    maxAndMinStyle: TextStyle = MaterialTheme.typography.subtitle2
) {
    val type = if (celsius) "º" else "ºF"
    Row(verticalAlignment = alignment) {
        AverageTemperature(
            temperature = temperature,
            type = type,
            style = temperatureStyle
        )
        MinAndMaxTemperature(
            maxTemperature = maxTemperature,
            minTemperature = minTemperature,
            type = type,
            style = maxAndMinStyle
        )
    }
}

@Composable
private fun AverageTemperature(
    temperature: Int,
    type: String,
    style: TextStyle
) {
    Text(
        text = "$temperature$type",
        style = style
    )
}

@Composable
private fun MinAndMaxTemperature(
    maxTemperature: Int?,
    minTemperature: Int?,
    type: String,
    style: TextStyle
) {
    Column(modifier = Modifier.padding(top = 1.dp)) {
        if (maxTemperature != null && minTemperature != null) {
            MaxAndMaxTemperatureItem(
                temperature = maxTemperature,
                type = type,
                style = style,
                icon = painterResource(id = R.drawable.ic_arrow_up),
                contentDescription = stringResource(R.string.max_temperature)
            )
            MaxAndMaxTemperatureItem(
                temperature = minTemperature,
                type = type,
                style = style,
                icon = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = stringResource(R.string.min_temperature)
            )
        }
    }
}

@Composable
private fun MaxAndMaxTemperatureItem(
    temperature: Int,
    type: String,
    style: TextStyle,
    icon: Painter,
    contentDescription: String?
) {
    Row {
        Icon(
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = "$temperature$type",
            style = style
        )
    }
}
