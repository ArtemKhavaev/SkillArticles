package ru.skillbranch.skillarticles.extensions

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.activity_root.view.*
import ru.skillbranch.skillarticles.R

fun View.setMarginOptionally(bottom: Int) {
    val scroll = findViewById<NestedScrollView>(R.id.scroll)
//    scroll.bottom = bottom
    scroll.setBottom(bottom)
    this.setBottom(bottom)
}

fun  View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom){
    val scroll = findViewById<NestedScrollView>(R.id.scroll)
    scroll.bottom = bottom
    scroll.top = top
    scroll.left = left
    scroll.right = right
}