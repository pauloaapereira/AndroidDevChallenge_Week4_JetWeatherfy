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
package com.pp.jetweatherfy.ui.components.content.simple

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.models.DailyForecast
import com.pp.jetweatherfy.domain.models.getFormattedTime
import com.pp.jetweatherfy.ui.components.content.SelectedAlpha
import com.pp.jetweatherfy.ui.components.content.UnselectedAlpha
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.SmallDimension

@Composable
fun SimpleContentDays(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    selectedDailyForecast: DailyForecast?,
    dailyForecasts: List<DailyForecast>,
    onMoreClick: () -> Unit,
    onDailyForecastSelected: (Int, DailyForecast) -> Unit,
) {
    val backgroundColor = (selectedDailyForecast?.generateWeatherColorFeel() ?: MaterialTheme.colors.primary).copy(alpha = UnselectedAlpha)

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BigDimension),
        state = scrollState
    ) {
        itemsIndexed(dailyForecasts) { index, dailyForecast ->
            val isSelectedAlpha =
                if (dailyForecast == selectedDailyForecast) SelectedAlpha else UnselectedAlpha
            Day(
                surfaceColor = backgroundColor.copy(alpha = isSelectedAlpha),
                text = dailyForecast.getFormattedTime(),
                onClick = { onDailyForecastSelected(index, dailyForecast) }
            )
        }
        item {
            Day(
                surfaceColor = backgroundColor,
                text = stringResource(R.string.more),
                onClick = { onMoreClick() }
            )
        }
    }
}

@Composable
private fun Day(
    surfaceColor: Color,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(surfaceColor)
            .clickable { onClick() }
            .padding(SmallDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.subtitle1)
    }
}
