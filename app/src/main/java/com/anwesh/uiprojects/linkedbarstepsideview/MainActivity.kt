package com.anwesh.uiprojects.linkedbarstepsideview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.barstepsideview.BarStepSideView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarStepSideView.create(this)
    }
}
