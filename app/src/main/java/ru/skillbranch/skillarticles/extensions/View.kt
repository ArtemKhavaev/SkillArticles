package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import ru.skillbranch.skillarticles.R



fun View.setMarginOptionally(bottom: Int) {
    ViewGroup.MarginLayoutParams((this as ViewGroup).layoutParams)
        .setMargins(0, 0, 0, bottom)
}

//fun  View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom){
//    val scroll = findViewById<NestedScrollView>(R.id.scroll)
//    scroll.bottom = bottom
//    scroll.top = top
//    scroll.left = left
//    scroll.right = right
//}