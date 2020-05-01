package com.example.getthemall.network

import androidx.databinding.BaseObservable
import com.squareup.moshi.Json

enum class DefenceType {
    SPECIAL_DEFENCE,
    NORMAL,
    NONE
}

data class Pokemon(val stats: List<Stats>, val sprites: Sprite) {
    var defence = DefenceType.NONE
    var specialAttack: Boolean = false
}

data class Stats(@Json(name = "base_stat") var baseStat: Int, val effort: Int, val stat: Stat)

data class Stat(val name: String, val url: String)

data class Sprite(@Json(name = "front_default") val frontDefault: String)