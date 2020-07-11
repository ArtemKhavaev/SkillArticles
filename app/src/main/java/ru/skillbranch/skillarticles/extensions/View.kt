package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup

fun View.setMarginOptionally(bottom: Int) {
    val params: ViewGroup.MarginLayoutParams = this.getLayoutParams() as ViewGroup.MarginLayoutParams
    params.setMargins(0, 0, 0, bottom)
}
