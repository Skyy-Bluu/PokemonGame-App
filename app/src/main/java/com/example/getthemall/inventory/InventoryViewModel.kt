package com.example.getthemall.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getthemall.battle.getPokemon
import com.example.getthemall.network.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InventoryViewModel :ViewModel(){

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _pokemons = MutableLiveData<MutableList<Pokemon>>()

    val pokemons: LiveData<MutableList<Pokemon>>
        get() = _pokemons

    private val _navigateWithSelectedPokemon = MutableLiveData<Pokemon>()

    val navigateWithSelectedPokemon : LiveData<Pokemon>
        get() = _navigateWithSelectedPokemon

    init{
        val mutableList = mutableSetOf<Pokemon>()
        for (i in 1..40){
            coroutineScope.launch { getPokemon(i)
                ?.let {
                    mutableList.add(it)
                    _pokemons.value = mutableList.toMutableList() }
            }
        }
    }

    fun choosePokemon(pokemon: Pokemon) {
        _navigateWithSelectedPokemon.value = pokemon
    }

    fun pokemonChosen() {
        _navigateWithSelectedPokemon.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}