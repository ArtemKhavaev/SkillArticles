package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import ru.skillbranch.skillarticles.R



fun View.setMarginOptionally(bottom: Int) {

//    val p : ViewGroup.LayoutParams = this.getLayoutParams()
//    val scroll = findViewById<NestedScrollView>(R.id.scroll)
//
//    scroll.layoutParams= p as scroll.layoutParams;
//        if (_default) lp.setMargins(mc.oml, mc.omt, mc.omr, mc.omb);
//        else lp.setMargins(mc.ml, mc.mt, mc.mr, mc.mb);
//    this.layoutParams = lp;

    val params: ViewGroup.MarginLayoutParams = this.getLayoutParams() as ViewGroup.MarginLayoutParams
//    params.bottomMargin = bottom
    params.setMargins(0, 0, 0, bottom)


//    ViewGroup.MarginLayoutParams((this as ViewGroup).layoutParams)
//        .setMargins(0, 0, 0, bottom)
}

//fun  View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom){
//    val scroll = findViewById<NestedScrollView>(R.id.scroll)
//    scroll.bottom = bottom
//    scroll.top = top
//    scroll.left = left
//    scroll.right = right
//}