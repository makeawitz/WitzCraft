package com.makeawitz.witzcraft.theme

import android.animation.ValueAnimator
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object ThemeApplier {

    private val animatorList: MutableList<MutableMap<String, Any>> = ArrayList()
    private val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    private const val targetView = "targetView"
    private const val colorFrom = "colorFrom"
    private const val colorTo = "colorTo"

    fun getRootView(context: Context): ViewGroup {
        val activity = getActivity(context)
            ?: throw IllegalArgumentException("Cannot parse context to activity")
        return (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    }

    fun getActivity(c: Context): Activity? {
        var context = c
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    fun getActivity(view: View): Activity? {
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    fun theme(view: View, theme: Theme, duration: Long? = null) {
        animatorList.clear()
        applyTheme(view, theme, duration)
        if (duration != null) {
            valueAnimator.duration = duration
            for (map in animatorList) {
                val colorFrom = map[colorFrom] as Int
                val colorTo = map[colorTo] as Int
                applyColorChangeAnimation(map[targetView]!!, colorFrom, colorTo)
            }
            valueAnimator.start()
        }
    }

    private fun applyTheme(view: View, theme: Theme, duration: Long? = null) {
        when (view) {
            is ViewGroup -> {
                for (c in view.children) {
                    applyTheme(c, theme, duration)
                }
                when (if (view.tag is ThemeTag) view.tag as ThemeTag else null) {
                    ThemeTag.BUTTON -> {
                        animateBackgroundColor(view, duration, theme.baseColor)
                    }
                    ThemeTag.SPLASH -> {
                        animateBackgroundColor(view, duration, theme.statusBarColor)
                    }
                    ThemeTag.SKIP -> {
                        // SKIP
                    }
                    else -> {
                        animateBackgroundColor(view, duration, theme.backgroundColor)
                    }
                }
            }
            is EditText -> {
                view.setTextColor(theme.textColor)
                view.setHintTextColor(theme.hintTextColor)
            }
            is TextView -> {
                view.setTextColor(theme.textColor)
            }
        }
    }

    fun theme(view: View, theme: Theme, how: ((view: View, theme: Theme) -> Unit)) {
        if (view is ViewGroup) {
            for (c in view.children) {
                theme(view = c, theme = theme, how = how)
            }
        }
        how.invoke(view, theme)
    }

    fun theme(context: Context, theme: Theme, prevTheme: Theme = theme, duration: Long? = null) {
        val activity = getActivity(context)
            ?: throw IllegalArgumentException("Cannot parse context to activity")
        applyTheme(activity, prevTheme, theme, duration != null)
        theme(
            (activity.window.decorView.findViewById(android.R.id.content) as ViewGroup).getChildAt(
                0
            ), theme, duration
        )
    }

    fun theme(context: Context, theme: Theme, how: ((view: View, theme: Theme) -> Unit)) {
        val activity = getActivity(context)
            ?: throw IllegalArgumentException("Cannot parse context to activity")
        theme(
            (activity.window.decorView.findViewById(android.R.id.content) as ViewGroup).getChildAt(
                0
            ), theme, how
        )
    }

    private fun animateBackgroundColor(view: View, duration: Long?, color: Int) {
        if (duration == null) {
            view.setBackgroundColor(color)
        } else {
            val background = view.background
            if (background is ColorDrawable) {
                val oldColor = background.color
                val map = HashMap<String, Any>()
                map[targetView] = view
                map[colorFrom] = oldColor
                map[colorTo] = color
                animatorList.add(map)
            } else {
                val colorDrawable = ColorDrawable()
                colorDrawable.color = ColorUtils.blendARGB(color, Color.BLACK, 0.2f)
                view.background = colorDrawable

                val map = HashMap<String, Any>()
                map[targetView] = view
                map[colorFrom] = colorDrawable.color
                map[colorTo] = color
                animatorList.add(map)
            }
        }
    }

    private fun applyTheme(
        activity: Activity?,
        prevTheme: Theme,
        theme: Theme,
        isAnimated: Boolean
    ) {
        if (activity == null) {
            return
        }
        if (isAnimated) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                applyColorChangeAnimation(
                    activity.window,
                    activity.window.statusBarColor,
                    theme.statusBarColor,
                    "status"
                )
                applyColorChangeAnimation(
                    activity.window,
                    activity.window.navigationBarColor,
                    theme.navigationBarColor,
                    "navigation"
                )
            }
            if (activity is AppCompatActivity) {
                val actionBar = activity.supportActionBar
                if (actionBar != null) {
                    applyColorChangeAnimation(
                        actionBar,
                        prevTheme.actionBarColor,
                        theme.actionBarColor
                    )
                }
            } else {
                val actionBar = activity.actionBar
                if (actionBar != null) {
                    applyColorChangeAnimation(
                        actionBar,
                        prevTheme.actionBarColor,
                        theme.actionBarColor
                    )
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = theme.statusBarColor
                activity.window.navigationBarColor = theme.navigationBarColor
            }
            if (activity is AppCompatActivity) {
                activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(theme.actionBarColor))
            } else {
                activity.actionBar?.setBackgroundDrawable(ColorDrawable(theme.actionBarColor))
            }
        }
    }

    private fun applyColorChangeAnimation(
        target: Any,
        colorFrom: Int,
        colorTo: Int,
        tag: String = ""
    ) {
        val aF = Color.alpha(colorFrom)
        val rF = Color.red(colorFrom)
        val gF = Color.green(colorFrom)
        val bF = Color.blue(colorFrom)

        val aT = Color.alpha(colorTo)
        val rT = Color.red(colorTo)
        val gT = Color.green(colorTo)
        val bT = Color.blue(colorTo)

        val aD = aT - aF
        val rD = rT - rF
        val gD = gT - gF
        val bD = bT - bF

        when {
            target is ViewGroup -> {
                valueAnimator.addUpdateListener {
                    val v = it.animatedValue as Float
                    val tA = (aF + (v * aD)).toInt()
                    val tR = (rF + (v * rD)).toInt()
                    val tG = (gF + (v * gD)).toInt()
                    val tB = (bF + (v * bD)).toInt()
                    val color = Color.argb(tA, tR, tG, tB)
                    target.setBackgroundColor(color)
                }
            }
            target is Window && tag == "status" -> {
                valueAnimator.addUpdateListener {
                    val v = it.animatedValue as Float
                    val tA = (aF + (v * aD)).toInt()
                    val tR = (rF + (v * rD)).toInt()
                    val tG = (gF + (v * gD)).toInt()
                    val tB = (bF + (v * bD)).toInt()
                    val color = Color.argb(tA, tR, tG, tB)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        target.statusBarColor = color
                    }
                }
            }
            target is Window && tag == "navigation" -> {
                valueAnimator.addUpdateListener {
                    val v = it.animatedValue as Float
                    val tA = (aF + (v * aD)).toInt()
                    val tR = (rF + (v * rD)).toInt()
                    val tG = (gF + (v * gD)).toInt()
                    val tB = (bF + (v * bD)).toInt()
                    val color = Color.argb(tA, tR, tG, tB)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        target.navigationBarColor = color
                    }
                }
            }
            target is ActionBar -> {
                valueAnimator.addUpdateListener {
                    val v = it.animatedValue as Float
                    val tA = (aF + (v * aD)).toInt()
                    val tR = (rF + (v * rD)).toInt()
                    val tG = (gF + (v * gD)).toInt()
                    val tB = (bF + (v * bD)).toInt()
                    val color = Color.argb(tA, tR, tG, tB)
                    target.setBackgroundDrawable(ColorDrawable(color))
                }
            }
            target is androidx.appcompat.app.ActionBar -> {
                valueAnimator.addUpdateListener {
                    val v = it.animatedValue as Float
                    val tA = (aF + (v * aD)).toInt()
                    val tR = (rF + (v * rD)).toInt()
                    val tG = (gF + (v * gD)).toInt()
                    val tB = (bF + (v * bD)).toInt()
                    val color = Color.argb(tA, tR, tG, tB)
                    target.setBackgroundDrawable(ColorDrawable(color))
                }
            }
            else -> {
            }
        }
    }
}