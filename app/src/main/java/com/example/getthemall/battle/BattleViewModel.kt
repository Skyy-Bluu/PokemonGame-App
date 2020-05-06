package com.example.getthemall.battle

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.getthemall.extensions.damageCalculation
import com.example.getthemall.network.DefenceType
import com.example.getthemall.network.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 *
 *
 * This viewmodel is used for fetching pokemon for the battle state of the app
 *
 **/

class GetPokemonViewModel: ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _pokemon = MutableLiveData<Pokemon>()

    val pokemon: LiveData<Pokemon>
        get() = _pokemon

    private val _evilPokemon = MutableLiveData<Pokemon>()

    val evilPokemon: LiveData<Pokemon>
        get() = _evilPokemon

    private val _turn = MutableLiveData<Turn>()

    val turn: LiveData<Turn>
        get() = _turn

    var baseHPEnemy = 0
    var baseHpPlayer = 0

    init {
        coroutineScope.launch {
            _evilPokemon.value = getPokemon(3)
            ?.also {
                baseHPEnemy = it.stats[5].baseStat
            }
        }
        coroutineScope.launch {
            _pokemon.value = getPokemon(6)
                ?.also {
                baseHpPlayer = it.stats[5].baseStat
            }
        }
    }

    val displayEvilPokemonHP:LiveData<Int> = Transformations.map(evilPokemon) {
        ((it.stats[5].baseStat.toDouble() / baseHPEnemy.toDouble()) * 100).toInt()
    }
    val displayPokemonHP:LiveData<Int> = Transformations.map(pokemon) {
        ((it.stats[5].baseStat.toDouble() / baseHpPlayer.toDouble()) * 100).toInt()
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



    inner class BattleUtils{

        private val DELAY:Long = 2500

        private val _turn = MutableLiveData<Turn>()

        val turn: LiveData<Turn>
        get() = _turn

    private val _dmgTakenEnemy = MutableLiveData<Boolean>()

    val dmgTakenEnemy: LiveData<Boolean>
        get() = _dmgTakenEnemy

    private val _dmgTakenHero = MutableLiveData<Boolean>()

    val dmgTakenHero: LiveData<Boolean>
        get() = _dmgTakenHero


    private fun fightBack(sideAndStance: SideAndStance) {
        _turn.value = Turn.ENEMY
        Log.i("GameViewModel", "fightback called")
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
                _dmgTakenEnemy.value = true

                fightBack(sideAndStance)
                Handler().postDelayed({

                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true
                    _turn.value = Turn.PLAYER
                }, DELAY)
            }

            SideAndStance("Hero", "Normal") -> {
                fightBack(sideAndStance)
                Handler().postDelayed({
                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true
                    _turn.value = Turn.PLAYER
                }, DELAY)
            }

            SideAndStance("Enemy", "Special") -> {
                _pokemon.value?.specialAttack = true
                _evilPokemon.value?.damageCalculation(_pokemon.value)
                _evilPokemon.value = _evilPokemon.value
                _dmgTakenEnemy.value = true

                fightBack(sideAndStance)
                Handler().postDelayed({

                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true
                    _turn.value = Turn.PLAYER
                }, DELAY)

            }

            SideAndStance("Hero", "Special") -> {
                Handler().postDelayed({
                    fightBack(sideAndStance)
                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true
                    _turn.value = Turn.PLAYER
                }, DELAY)
            }
        }
    }



    fun defend(side: String, stance: String)  {

        val sideAndStance = SideAndStance(side, stance)

        when (sideAndStance) {
            SideAndStance("Enemy", "Normal") -> {
                _evilPokemon.value?.defence = DefenceType.NORMAL
                _evilPokemon.value = _evilPokemon.value
                _dmgTakenEnemy.value = true
                _turn.value = Turn.PLAYER
            }

            SideAndStance("Enemy", "Special") -> {
                _evilPokemon.value?.defence = DefenceType.SPECIAL_DEFENCE
                _evilPokemon.value = _evilPokemon.value
                _dmgTakenEnemy.value = true
                _turn.value = Turn.PLAYER
            }

            SideAndStance("Hero", "Normal") -> {
                _pokemon.value?.defence = DefenceType.NORMAL
                fightBack(sideAndStance)
                Handler().postDelayed({

                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true
                }, DELAY)
            }

            SideAndStance("Hero", "Special") -> {
                _pokemon.value?.defence = DefenceType.SPECIAL_DEFENCE
                fightBack(sideAndStance)
                Handler().postDelayed({

                    _pokemon.value = _pokemon.value
                    _dmgTakenHero.value = true

                }, DELAY)
            }
        }
    }

    fun dmgTakenDone(){
        _dmgTakenEnemy.value = null
        _dmgTakenHero.value = null
    }
}

}