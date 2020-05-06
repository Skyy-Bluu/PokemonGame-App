package com.example.getthemall.battle

import android.util.Log
import com.example.getthemall.network.Pokemon
import com.example.getthemall.network.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

suspend fun getPokemon(id: Int)
        : Pokemon? = withContext(Dispatchers.IO){
    val getPokemonDeferred = PokemonApi.retrofitService.getPokemon(id)
    try {
        getPokemonDeferred.await().apply {
            this.stats[5].baseStat *= 2
        }
    } catch (e: Exception) {
        Log.e("GameViewModel", e.toString())
        null
    }
}