package com.makeawitz.witzcraft.screen

class CalculatedTextSize(screenMetrics: ScreenMetrics) {
    private val density = screenMetrics.context.resources.displayMetrics.densityDpi
    val small: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (0.8 * screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (1.1 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (1.3 * screenMetrics.screenWidth / 100).toFloat()
        }
    }

    val regular: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (1.25 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (1.65 * screenMetrics.screenWidth / 100).toFloat()
        }
    }

    val subHeader: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (1.3 * screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (1.6 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (2 * screenMetrics.screenWidth / 100).toFloat()
        }
    }

    val header: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (1.8 * screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (1.95 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (2.5 * screenMetrics.screenWidth / 100).toFloat()
        }
    }

    val largeHeader: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (2.5 * screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (2.65 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (3.2 * screenMetrics.screenWidth / 100).toFloat()
        }
    }

    val huge: Float = when {
        screenMetrics.screenWidth / density <= 1.75 -> {
            (3 * screenMetrics.screenWidth / 100).toFloat()
        }
        screenMetrics.screenWidth / density <= 2.25 -> {
            (3.3 * screenMetrics.screenWidth / 100).toFloat()
        }
        else -> {
            (4 * screenMetrics.screenWidth / 100).toFloat()
        }
    }
}