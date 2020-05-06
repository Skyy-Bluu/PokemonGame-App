package com.example.getthemall.extensions

import android.util.Log
import com.example.getthemall.network.DefenceType
import com.example.getthemall.network.Pokemon

fun Pokemon.damageCalculation(attacking: Pokemon?) {

    var currentAttack = 0
    val currentDefence: Int

    attacking?.apply {
        currentAttack = if (specialAttack) {
            specialAttack = false
            stats[2].baseStat
        } else {
            stats[4].baseStat
        }
    }

    val currentHp: Int = this.stats[5].baseStat
    when (this.defence) {
        DefenceType.NORMAL -> {
            currentDefence = this.stats[3].baseStat

            if (currentDefence - currentAttack < 0) {
                this.stats[5].baseStat = currentHp + currentDefence - currentAttack
            }
            this.defence = DefenceType.NONE
        }

        DefenceType.SPECIAL_DEFENCE -> {
            currentDefence = this.stats[1].baseStat

            if (currentDefence - currentAttack < 0) {
                this.stats[5].baseStat = currentHp + currentDefence - currentAttack
            }
            this.defence = DefenceType.NONE
        }

        DefenceType.NONE -> {
            this.stats[5].baseStat = currentHp - currentAttack
        }
    }
}