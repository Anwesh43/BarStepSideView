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
