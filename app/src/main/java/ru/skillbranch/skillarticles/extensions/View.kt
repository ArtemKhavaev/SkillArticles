package ru.skillbranch.skillarticles.extensions

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.activity_root.view.*

fun View.setMarginOptionally(bottom: Int) {
    setBottom(bottom)
}

fun  View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom){
    setBottom(bottom)
    setTop(top)
    setLeft(left)
    setRight(right)
}