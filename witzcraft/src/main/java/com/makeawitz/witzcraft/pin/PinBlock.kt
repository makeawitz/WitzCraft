package com.makeawitz.witzcraft.pin

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.makeawitz.witzcraft.R
import com.makeawitz.witzcraft.animator.AnimationControl
import com.makeawitz.witzcraft.screen.ScreenMetrics

class PinBlock @JvmOverloads constructor(
    context: Context,
    slotAmount: Int = 6,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    screenMetrics: ScreenMetrics = ScreenMetrics(context)
) : LinearLayout(context, attrs, defStyleAttr) {

    private val slots: MutableList<PasswordView> = arrayListOf()
    private val slotWidth = (screenMetrics.screenWidth * 0.1).toInt()
    private val slotHeight = (slotWidth * 1.3).toInt()
    private val numericPad = NumericPad(context, screenMetrics)
    private val slotAmount = if (slotAmount > 8 || slotAmount < 1) 6 else slotAmount
    private val root = ((context as Activity).findViewById<View>(android.R.id.content) as ViewGroup)
    private val numpadDuration: Long = 300

    private var onFilled: () -> Unit = {}
    var pin = ""
        private set

    var keyPadShown = false
        private set

    private var locked = false

    init {
        orientation = HORIZONTAL
        weightSum = this.slotAmount.toFloat()

        for (i in 0 until this.slotAmount) {
            val block = RelativeLayout(context)
            val blockParams = LayoutParams(
                0,
                slotHeight
            )
            blockParams.weight = 1f
            block.layoutParams = blockParams
            addView(block)

            val slot = PasswordView(context)
            val slotParams = RelativeLayout.LayoutParams(
                slotWidth,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            slotParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            with(slot) {
                layoutParams = slotParams
                setBackgroundResource(R.drawable.pin_input_background)
            }
            block.addView(slot)
            slots.add(slot)
        }
    }

    fun appendPin(char: CharSequence) {
        if (pin.length == slots.size) {
            return
        }
        slots[pin.length].mask = true
        pin += char
        if (pin.length == slots.size) {
            Handler(Looper.getMainLooper()).postDelayed({
                onFilled.invoke()
                clearPin()
                hidePad()
            }, 200)
        }
    }

    fun deleteLast() {
        if (pin.isEmpty()) {
            return
        }
        pin = pin.substring(0, pin.length - 1)
        slots[pin.length].mask = false
    }

    fun clearPin() {
        pin = ""
        for (slot in slots) {
            slot.mask = false
        }
    }

    fun setOnFilledListener(onFilled: () -> Unit) {
        this.onFilled = onFilled
    }

    fun showPad() {
        if (keyPadShown || locked) {
            return
        }
        locked = true
        root.addView(numericPad)
        AnimationControl.fadeInEnterFromBottom(numericPad.pad, numpadDuration) {
            keyPadShown = true
            locked = false
        }
    }

    fun hidePad() {
        if (!keyPadShown || locked) {
            return
        }
        locked = true
        AnimationControl.fadeOutExitToBottom(numericPad.pad, numpadDuration / 2) {
            keyPadShown = false
            locked = false
            root.removeView(numericPad)
        }
    }

    private inner class NumericPad(context: Context, screenMetrics: ScreenMetrics) : RelativeLayout(context) {
        val pad = RelativeLayout(context)

        init {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val padHeight = (screenMetrics.screenHeight * 0.32).toInt()
            val padParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                padHeight
            )
            padParams.addRule(ALIGN_PARENT_BOTTOM)
            with(pad) {
                layoutParams = padParams
                visibility = View.GONE
                setBackgroundColor(ContextCompat.getColor(context, R.color.buttonPadGray))
            }
            addView(pad)

            val buttonWidth = screenMetrics.screenWidth / 3
            val buttonHeight = padHeight / 4
            for (i in 1..12) {
                createKeyPad(pad, i, buttonWidth, buttonHeight, screenMetrics)
            }
        }

        private fun createKeyPad(
            pad: RelativeLayout,
            padNum: Int,
            buttonWidth: Int,
            buttonHeight: Int,
            screenMetrics: ScreenMetrics
        ) {
            val padButton = when {
                padNum <= 9 -> {
                    val button = createTextViewPad(pad, padNum, buttonWidth, buttonHeight)
                    with(button) {
                        text = "$padNum"
                        textSize = calTextSize(screenMetrics, 1.8f, 1.95f, 2.5f)
                    }
                    button.setOnClickListener {
                        appendPin(button.text)
                    }
                    button
                }
                padNum == 10 -> {
                    val button = createTextViewPad(pad, padNum, buttonWidth, buttonHeight)
                    with(button) {
                        text = context.getString(R.string.clear)
                        textSize = calTextSize(screenMetrics, 1.3f, 1.6f, 2f)
                        setOnClickListener {
                            clearPin()
                        }
                    }
                    button
                }
                padNum == 11 -> {
                    val button = createTextViewPad(pad, padNum, buttonWidth, buttonHeight)
                    with(button) {
                        text = "0"
                        textSize = calTextSize(screenMetrics, 1.8f, 1.95f, 2.5f)
                    }
                    button.setOnClickListener {
                        appendPin(button.text)
                    }
                    button
                }
                else -> {
                    val button = ImageView(context)
                    val params = LayoutParams(buttonWidth, buttonHeight)
                    params.addRule(RIGHT_OF, pad.getChildAt(padNum - 2).id)
                    params.addRule(BELOW, pad.getChildAt(padNum - 4).id)
                    with(button) {
                        layoutParams = params
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        setBackgroundResource(R.drawable.keypad_state)
                        setImageResource(R.drawable.ic_input_delete)
                    }
                    button.setOnClickListener {
                        deleteLast()
                    }
                    button
                }
            }
            pad.addView(padButton)
        }

        private fun createTextViewPad(pad: RelativeLayout, padNum: Int, buttonWidth: Int, buttonHeight: Int): TextView {
            val button = TextView(context)
            button.id = View.generateViewId()
            val params = LayoutParams(buttonWidth, buttonHeight)
            if (padNum % 3 != 1) {
                params.addRule(RIGHT_OF, pad.getChildAt(padNum - 2).id)
            }
            if (padNum > 3) {
                params.addRule(BELOW, pad.getChildAt(padNum - 4).id)
            }
            with(button) {
                layoutParams = params
                setBackgroundResource(R.drawable.keypad_state)
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
            }
            return button
        }

        private fun calTextSize(screenMetrics: ScreenMetrics, lowRate: Float, medRate: Float, highRate: Float): Float {
            return when {
                screenMetrics.screenWidth / screenMetrics.dpi <= 1.75 -> (lowRate * screenMetrics.screenWidth / 100)
                screenMetrics.screenWidth / screenMetrics.dpi <= 2.25 -> (medRate * screenMetrics.screenWidth / 100)
                else -> (highRate * screenMetrics.screenWidth / 100)
            }
        }
    }
}