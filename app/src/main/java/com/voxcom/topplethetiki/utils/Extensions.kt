package com.voxcom.topplethetiki.utils

fun String.isValidUsername(): Boolean {
    return this.length in 3..12 && this.matches(Regex("^[a-zA-Z0-9_]+$"))
}

fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in indices) this[index] else null
}