package com.example.getthemall.network

import android.os.Parcelable
import androidx.databinding.BaseObservable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

enum class DefenceType {
    SPECIAL_DEFENCE,
    NORMAL,
    NONE
}

// TODO use enums instead of passing in strings for the battle functions
enum class TypeOfAttack {
    MAJOR
}

enum class Side {
    HERO,
    ENEMY
}

enum class Stance {
    SPECIAL,
    NORMAL
}

@Parcelize
data class Pokemon(val id: Int, val stats: List<Stats>, val sprites: Sprite) : Parcelable {
    var defence = DefenceType.NONE
    var specialAttack: Boolean = false
}

@Parcelize
data class Stats(
    @Json(name = "base_stat") var baseStat: Int,
    val effort: Int,
    val stat: Stat
) : Parcelable

@Parcelize
data class Stat(
    val name: String,
    val url: String
) : Parcelable

@Parcelize
data class Sprite(@Json(name = "front_default") val frontDefault: String) : Parcelable