package com.makeawitz.witzcraft.screen

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class ScreenMetrics(val context: Context) {

    val screenWidth: Int
    val screenHeight: Int
    val screenRawHeight: Int
    val dpi: Int

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenRawHeight = metrics.heightPixels
        screenHeight = metrics.heightPixels - getActionBarHeight() - getStatusBarHeight() - getNavigationBarHeight()
        dpi = metrics.densityDpi
    }


    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        val actionBar = (context as AppCompatActivity).supportActionBar
        return if (actionBar != null
            && context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else 0
    }

    fun getNavigationBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0 && hasSoftKeys()) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun getStatusBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun hasSoftKeys(): Boolean {
        val d = (context as Activity).windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }

}