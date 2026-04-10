package com.voxcom.topplethetiki.utils

object Constants {

    const val MAX_PLAYERS = 4

    const val STATUS_WAITING = "waiting"
    const val STATUS_PLAYING = "playing"
    const val STATUS_FINISHED = "finished"

    val INITIAL_TIKI_STACK = listOf(
        "t1","t2","t3","t4","t5","t6","t7","t8","t9"
    )
}

object Actions {
    const val UP1 = "up1"
    const val UP2 = "up2"
    const val UP3 = "up3"
    const val TOP = "top"
    const val BOTTOM = "bottom"
    const val TOSS = "toss"
}