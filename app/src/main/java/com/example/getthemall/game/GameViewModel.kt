package com.example.getthemall.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.getthemall.network.DefenceType
import com.example.getthemall.network.Pokemon
import com.example.getthemall.network.PokemonApi
import kotlinx.coroutines.*
import java.lang.Exception

data class SideAndStance(val side: String, val stance: String)

class GameViewModel : ViewModel() {
    private val _pokemon = MutableLiveData<Pokemon>()

    val pokemon: LiveData<Pokemon>
        get() = _pokemon

    private val _evilPokemon = MutableLiveData<Pokemon>()

    val evilPokemon: LiveData<Pokemon>
        get() = _evilPokemon

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var currentHp: Int = 0
    private var currentAttack: Int = 0
    private var currentDefence: Int = 0
    private var baseHp: Int = 0
    private var baseHpEnemy: Int = 0
    private var hpPerc: Double = 0.0

    init {
        getPokemon("Hero", 6)
        getPokemon("Enemy", 3)
    }

    val displayEvilPokemonHP: LiveData<Int> = Transformations.map(evilPokemon) {
        hpPerc = (it.stats[5].baseStat.toDouble()/baseHpEnemy.toDouble())
        //Log.i("GameViewModel","hp:${it.stats[5].baseStat} base:${baseHp} perc:${(hpPerc*100)}")
        (hpPerc*100).toInt()
    }

    val displayPokemonHP: LiveData<Int> = Transformations.map(pokemon) {
        hpPerc = (it.stats[5].baseStat.toDouble()/baseHp.toDouble())
            //Log.i("GameViewModel","hp:${it.stats[5].baseStat} base:${baseHp} perc:${(hpPerc*100)}")
           (hpPerc*100).toInt()
    }

    private fun getPokemon(side: String, id: Int) {
        coroutineScope.launch {
            val getPokemonDeferred = PokemonApi.retrofitService.getPokemon(id)

            try {
                //Log.i("GameViewModel","Trying to get pokemon deffered")
                val pokemonResult = getPokemonDeferred.await()
                pokemonResult.stats[5].baseStat *=2
                //Log.i("GameViewModel","Got Pokemon deffered ${pokemonResult.stats[5].baseStat}")

                if (side == "Hero") {
                    baseHp = pokemonResult.stats[5].baseStat
                    _pokemon.value = pokemonResult
                } else {
                    baseHpEnemy = pokemonResult.stats[5].baseStat
                    _evilPokemon.value = pokemonResult
                }

            } catch (e: Exception) {
                Log.e("GameViewModel", e.toString())
                _pokemon.value = null
                _evilPokemon.value = null
            }
        }

    }

    private fun fightBack(sideAndStance: SideAndStance) {

        when (sideAndStance.stance) {
            "Normal" -> {
                _pokemon.value?.damageCalculation(_evilPokemon.value)
            }

            "Special" -> {
                _evilPokemon.value?.specialAttack = true
                _pokemon.value?.damageCalculation(_evilPokemon.value)
            }
        }
    }

    fun attack(defendingSide: String, typeOfAttack: String) {

        val sideAndStance = SideAndStance(defendingSide, typeOfAttack)

        when (sideAndStance) {
            SideAndStance("Enemy", "Normal") -> {
                _evilPokemon.value?.damageCalculation(_pokemon.value)
                _evilPokemon.value = _evilPokemon.value
                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }

            SideAndStance("Hero", "Normal") -> {
                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }

            SideAndStance("Enemy", "Special") -> {
                _pokemon.value?.specialAttack = true
                _evilPokemon.value?.damageCalculation(_pokemon.value)
                _evilPokemon.value = _evilPokemon.value

                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }

            SideAndStance("Hero", "Special") -> {
                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }
        }
    }

    private fun Pokemon.damageCalculation(attacking: Pokemon?) {

        attacking?.apply {
            currentAttack = if (specialAttack) {
                specialAttack = false
                stats[2].baseStat
            } else {
                stats[4].baseStat
            }
        }

        currentHp = this.stats[5].baseStat

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

    fun defend(side: String, stance: String) {

        val sideAndStance = SideAndStance(side, stance)

        when (sideAndStance) {
            SideAndStance("Enemy", "Normal") -> {
                _evilPokemon.value?.defence = DefenceType.NORMAL
                _evilPokemon.value = _evilPokemon.value
            }

            SideAndStance("Enemy", "Special") -> {
                _evilPokemon.value?.defence = DefenceType.SPECIAL_DEFENCE
                _evilPokemon.value = _evilPokemon.value
            }

            SideAndStance("Hero", "Normal") -> {
                _pokemon.value?.defence = DefenceType.NORMAL
                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }

            SideAndStance("Hero", "Special") -> {
                _pokemon.value?.defence = DefenceType.SPECIAL_DEFENCE
                fightBack(sideAndStance)
                _pokemon.value = _pokemon.value
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}