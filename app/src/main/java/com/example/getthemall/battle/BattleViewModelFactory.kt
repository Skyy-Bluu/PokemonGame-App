package com.example.getthemall.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getthemall.network.Pokemon

class BattleViewModelFactory(
    private val pokemon: Pokemon) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BattleViewModel::class.java)) {
            return BattleViewModel(pokemon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}