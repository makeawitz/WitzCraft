package com.makeawitz.witzcraft.theme

import android.graphics.Color
import androidx.core.graphics.ColorUtils

open class Theme(
    val baseColor: Int,
    val statusBarColor: Int = ColorUtils.blendARGB(baseColor, Color.BLACK, 0.2f),
    val actionBarColor: Int = baseColor,
    val navigationBarColor: Int = Color.BLACK,
    val textColor: Int = {
        val a = Color.alpha(baseColor)
        val r = Color.red(baseColor)
        val g = Color.green(baseColor)
        val b = Color.blue(baseColor)
        if (a >= 128 && (r + g + b) / 3 < 110) {
            Color.WHITE
        } else {
            Color.DKGRAY
        }
    }.invoke(),
    val hintTextColor: Int = Color.LTGRAY,
    val backgroundColor: Int = {
        val a = Color.alpha(baseColor)
        val r = Color.red(baseColor)
        val g = Color.green(baseColor)
        val b = Color.blue(baseColor)
        if (a >= 128 && (r + g + b) / 3 < 110) {
            ColorUtils.blendARGB(baseColor, Color.WHITE, 0.1f)
        } else {
            ColorUtils.blendARGB(baseColor, Color.WHITE, 0.9f)
        }
    }.invoke()
)