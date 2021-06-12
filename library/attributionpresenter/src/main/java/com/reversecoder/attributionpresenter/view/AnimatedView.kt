package com.reversecoder.attributionpresenter.view;

import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Created by Chatikyan on 17.09.2017.
 */
interface AnimatedView {

    fun <V : View> animate(view: V, duration: Long = 170, startDelay: Long, acton: V.() -> Unit) {
        val scaleFactor = 0.75f
        with(view) {
            clearAnimation()
            animate()
                    .alpha(0f)
                    .scaleX(scaleFactor)
                    .setDuration(duration)
                    .withLayer()
                    .setInterpolator(FastOutSlowInInterpolator())
                    .setStartDelay(startDelay)
                    .withEndAction {
                        acton(view)
                        scaleX = scaleFactor
                        animate()
                                .scaleX(1f)
                                .alpha(1f)
                                .setListener(null)
                                .withLayer()
                                .setDuration(duration)
                                .start()
                    }
                    .start()
        }
    }
}