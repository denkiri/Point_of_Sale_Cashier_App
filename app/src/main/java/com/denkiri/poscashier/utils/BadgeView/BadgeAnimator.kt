package com.denkiri.poscashier.utils.BadgeView
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import java.lang.ref.WeakReference
import java.util.Random
class BadgeAnimator(badgeBitmap: Bitmap, center: PointF, badge: BadgeView) : ValueAnimator() {
    private val mFragments: Array<Array<BitmapFragment?>>
    private val mWeakBadge: WeakReference<BadgeView>

    init {
        mWeakBadge = WeakReference(badge)
        setFloatValues(0f, 1f)
        duration = 500
        mFragments = getFragments(badgeBitmap, center)
        addUpdateListener {
            val badgeView = mWeakBadge.get()
            if (badgeView == null || !badgeView.isShown) {
                cancel()
            } else {
                badgeView.invalidate()
            }
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val badgeView = mWeakBadge.get()
                badgeView?.reset()
            }
        })
    }

    fun draw(canvas: Canvas) {
        for (i in mFragments.indices) {
            for (j in 0 until mFragments[i].size) {
                val bf = mFragments[i][j]
                val value = java.lang.Float.parseFloat(animatedValue.toString())
                bf?.updata(value, canvas)
            }
        }
    }

    private fun getFragments(badgeBitmap: Bitmap, center: PointF): Array<Array<BitmapFragment?>> {
        val width = badgeBitmap.width
        val height = badgeBitmap.height
        val fragmentSize = Math.min(width, height) / 6f
        val startX = center.x - badgeBitmap.width / 2f
        val startY = center.y - badgeBitmap.height / 2f
        var fragments = Array<Array<BitmapFragment?>>((height / fragmentSize).toInt()) { arrayOfNulls((width / fragmentSize)?.toInt()) }
        for (i in fragments.indices) {
            for (j in 0 until fragments[i].size) {
                val bf = BitmapFragment()
                bf.color = badgeBitmap.getPixel((j * fragmentSize).toInt(), (i * fragmentSize).toInt())
                bf.x = startX + j * fragmentSize
                bf.y = startY + i * fragmentSize
                bf.size = fragmentSize
                bf.maxSize = Math.max(width, height)
                fragments[i][j] = bf
            }
        }
        badgeBitmap.recycle()
        return fragments
    }

    private inner class BitmapFragment {
        internal var random: Random
        internal var x: Float = 0.toFloat()
        internal var y: Float = 0.toFloat()
        internal var size: Float = 0.toFloat()
        internal var color: Int = 0
        internal var maxSize: Int = 0
        internal var paint: Paint

        init {
            paint = Paint()
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            random = Random()
        }

        fun updata(value: Float, canvas: Canvas) {
            paint.color = color
            x = x + 0.1f * random.nextInt(maxSize).toFloat() * (random.nextFloat() - 0.5f)
            y = y + 0.1f * random.nextInt(maxSize).toFloat() * (random.nextFloat() - 0.5f)
            canvas.drawCircle(x, y, size - value * size, paint)
        }
    }
}
