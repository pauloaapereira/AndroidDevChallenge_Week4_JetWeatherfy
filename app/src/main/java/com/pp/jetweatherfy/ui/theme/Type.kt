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
package com.pp.jetweatherfy.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pp.jetweatherfy.R

private val AppFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.comfortaa_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.comfortaa_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.comfortaa_semibold,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        )
    )
)

private val DefaultTypography = Typography()
val typography = Typography(
    h1 = DefaultTypography.h1.copy(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        letterSpacing = 0.sp
    ),
    h2 = DefaultTypography.h2.copy(
        fontFamily = AppFontFamily, fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (0.15).sp
    ),
    subtitle1 = DefaultTypography.subtitle1.copy(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 21.sp,
        letterSpacing = 0.sp
    ),
    subtitle2 = DefaultTypography.subtitle1.copy(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    body1 = DefaultTypography.body1.copy(
        fontFamily = AppFontFamily, fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    body2 = DefaultTypography.body2.copy(
        fontFamily = AppFontFamily, fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
    button = DefaultTypography.button.copy(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 1.sp
    ),
    caption = DefaultTypography.caption.copy(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    )
)
