package com.anwesh.uiprojects.barstepsideview

/**
 * Created by anweshmishra on 21/01/20.
 */
import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val bars : Int = 3
val scGap : Float = 0.02f / bars
val delay : Long = 20L / bars
val sizeFactor : Float = 2.2f
val foreColor : Int = Color.parseColor("#311B92")
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawBarStep(j : Int, scale : Float, size : Float, w : Float, sj : Float, paint : Paint) {
    val hGap : Float = 2 * size / bars
    val sf : Float = scale.sinify().divideScale(j, bars)
    val x : Float = (w / 2 - size) * sf * sj
    save()
    translate(x, -size + hGap * j + hGap / 2)
    drawRect(RectF(-size, -hGap / 2, size, hGap / 2), paint)
    restore()
}

fun Canvas.drawBarSteps(scale : Float, size : Float, w : Float, sj : Float, paint : Paint) {
    for (j in 0..(bars - 1)) {
        drawBarStep(j, scale, size, w, sj, paint)
    }
}

fun Canvas.drawBSSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    save()
    translate(gap * (i + 1), h / 2)
    drawBarSteps(scale, size, w, 1f - 2 * i, paint)
    restore()
}

class BarStepSideView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class BSSNode(var i : Int, val state : State = State()) {

        private var next : BSSNode? = null
        private var prev : BSSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = BSSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBSSNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BSSNode {
            var curr : BSSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class BarStepSide(var i : Int) {

        private val root : BSSNode = BSSNode(0)
        private var curr : BSSNode = root
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            root.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }
}