package com.makeawitz.witzcraft.animator

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.view.View
import android.view.animation.*

object AnimationControl {

    private const val defaultDuration: Long = 500

    fun fadeIn(view: View, duration: Long = defaultDuration, onComplete: () -> Unit = {}) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate().setListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                onComplete()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }).setDuration(duration).setInterpolator(LinearInterpolator()).alpha(1f).start()
    }

    fun fadeOut(view: View, duration: Long = defaultDuration, onComplete: () -> Unit = {}) {
        view.animate().setListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                view.alpha = 1f
                onComplete()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }).setDuration(duration).setInterpolator(LinearInterpolator()).alpha(0f).start()
    }

    fun exitToTop(view: View, duration: Long = defaultDuration, onComplete: () -> Unit = {}) {
        view.animate().setListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                view.translationY = 0f
                onComplete()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }).setDuration(duration).setInterpolator(LinearInterpolator()).translationY(-view.height.toFloat()).start()
    }

    fun fadeInEnterFromBottom(view: View, duration: Long = defaultDuration, onComplete: () -> Unit = {}) {
        val animationSet = AnimationSet(true)
        animationSet.interpolator = LinearInterpolator()
        animationSet.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                view.alpha = 1f
                onComplete()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
                with(view) {
                    alpha = 0f
                    visibility = View.VISIBLE
                    bringToFront()
                }
            }
        })

        val translation = TranslateAnimation(0f, 0f, view.height.toFloat(), 0f)
        translation.duration = duration

        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = duration

        with(animationSet) {
            addAnimation(translation)
            addAnimation(alpha)
        }
        view.startAnimation(animationSet)
    }

    fun fadeOutExitToBottom(view: View, duration: Long = defaultDuration, onComplete: () -> Unit = {}) {
        val animationSet = AnimationSet(true)
        animationSet.interpolator = LinearInterpolator()
        animationSet.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                with(view) {
                    visibility = View.GONE
                    translationY = 0f
                    alpha = 1f
                }
                onComplete()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        val translation = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
        translation.duration = duration

        val alpha = AlphaAnimation(1f, 0f)
        alpha.duration = duration

        with(animationSet) {
            addAnimation(translation)
            addAnimation(alpha)
        }
        view.startAnimation(animationSet)
    }
}