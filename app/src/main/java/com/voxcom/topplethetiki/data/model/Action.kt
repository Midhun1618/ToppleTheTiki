package com.voxcom.topplethetiki.data.model

data class Action(
    val type: String = "", // up1, up2, up3, toss, top, bottom
    val value: Int = 0     // optional (for movement)
)