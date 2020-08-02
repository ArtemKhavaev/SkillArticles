package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup

fun View.setMarginOptionally(bottom: Int) {
    val params: ViewGroup.MarginLayoutParams = this.getLayoutParams() as ViewGroup.MarginLayoutParams
    params.setMargins(0, 0, 0, bottom)
}

fun View.setPaddingOptionally(left: Int, right: Int) {
//    val params: ViewGroup.LayoutParams = this.getLayoutParams()
    this.setPadding(left, 0, right, 0)
}
fun View.setPaddingOptionally(right: Int) {
//    val params: ViewGroup.LayoutParams = this.getLayoutParams()
    this.setPadding(0, 0, right, 0)
}

