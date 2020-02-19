package com.makeawitz.witzcraft.pin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class PasswordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }

    var textColor: Int = Color.DKGRAY
        set(value) {
            field = value
            maskPaint.color = value
            invalidate()
        }

    var mask = false
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mask) {
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width * 0.15).toFloat(), maskPaint)
            canvas?.save()
        }
    }
}