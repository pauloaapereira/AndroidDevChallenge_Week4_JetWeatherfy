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
package com.pp.jetweatherfy.utils

import androidx.annotation.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

@Size(3)
private fun Color.toHSL(@Size(3) hsl: FloatArray = FloatArray(3)): FloatArray {
    val max = red.coerceAtLeast(green.coerceAtLeast(blue))
    val min = red.coerceAtMost(green.coerceAtMost(blue))
    hsl[2] = (max + min) / 2

    if (max == min) {
        hsl[1] = 0f
        hsl[0] = hsl[1]
    } else {
        val d = max - min

        hsl[1] = if (hsl[2] > 0.5f) d / (2f - max - min) else d / (max + min)
        when (max) {
            red -> hsl[0] = (green - blue) / d + (if (green < blue) 6 else 0)
            green -> hsl[0] = (blue - red) / d + 2
            blue -> hsl[0] = (red - green) / d + 4
        }
        hsl[0] /= 6f
    }
    return hsl
}

private fun hslToColor(@Size(3) hsl: FloatArray): Color {
    val r: Float
    val g: Float
    val b: Float

    val h = hsl[0]
    val s = hsl[1]
    val l = hsl[2]

    if (s == 0f) {
        b = l
        g = b
        r = g
    } else {
        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        r = hue2rgb(p, q, h + 1f / 3)
        g = hue2rgb(p, q, h)
        b = hue2rgb(p, q, h - 1f / 3)
    }

    return Color(r, g, b)
}

private fun hue2rgb(p: Float, q: Float, t: Float): Float {
    var valueT = t
    if (valueT < 0) valueT += 1f
    if (valueT > 1) valueT -= 1f
    if (valueT < 1f / 6) return p + (q - p) * 6f * valueT
    if (valueT < 1f / 2) return q
    return if (valueT < 2f / 3) p + (q - p) * (2f / 3 - valueT) * 6f else p
}

fun Color.lightenColor(value: Float): Color {
    val hsl = toHSL()
    hsl[2] += value
    hsl[2] = 0f.coerceAtLeast(hsl[2].coerceAtMost(1f))
    return hslToColor(hsl)
}

fun Color.darkenColor(value: Float): Color {
    val hsl = toHSL()
    hsl[2] -= value
    hsl[2] = 0f.coerceAtLeast(hsl[2].coerceAtMost(1f))
    return hslToColor(hsl)
}

fun Color.isLightColor(hsl: FloatArray = FloatArray(3)): Boolean {
    ColorUtils.RGBToHSL((red * 255f).roundToInt(), (green * 255f).roundToInt(), (blue * 255f).roundToInt(), hsl)
    return hsl[2] < .5f
}

fun Color.isDarkColor(hsl: FloatArray = FloatArray(3)): Boolean {
    ColorUtils.RGBToHSL((red * 255f).roundToInt(), (green * 255f).roundToInt(), (blue * 255f).roundToInt(), hsl)
    return hsl[2] < .5f
}

fun generateGradientFeel(baseColor: Color, lightenFactor: Float = 0.3f): Brush {
    val lightFactor = lightenFactor.coerceIn(0f, 1f)

    return Brush.verticalGradient(
        colors = listOf(
            baseColor,
            baseColor.lightenColor(lightFactor)
        )
    )
}
