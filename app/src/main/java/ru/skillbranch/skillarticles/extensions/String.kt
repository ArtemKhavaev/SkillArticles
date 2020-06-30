package ru.skillbranch.skillarticles.extensions

import android.widget.SearchView

fun String?.indexesOf(query: String) : List<Int>{
    if(query.isEmpty()) return emptyList()
    val rs : String = this!!.toLowerCase()
    return Regex(query).findAll(rs).map { it.range.first }.toList().filter { it  != -1 }
}

fun String?.indexesOf(query: String, isUpperCase: Boolean) : List<Int>{
    if(query.isEmpty()) return emptyList()
    val rs : String = if(isUpperCase) this!!.toLowerCase() else this!!
    return Regex(query).findAll(rs).map { it.range.first }.toList().filter { it  != -1 }
}