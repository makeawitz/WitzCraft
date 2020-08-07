package com.makeawitz.witzcraft.animator

import android.animation.ValueAnimator

class FloatValueAnimator(from: Float, to: Float) : ValueAnimator() {
    init {
        ofFloat(from, to)
    }
}