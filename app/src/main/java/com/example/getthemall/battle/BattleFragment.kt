package com.example.getthemall.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.getthemall.R
import com.example.getthemall.databinding.FragmentGameBinding
import com.example.getthemall.network.Pokemon

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGameBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        //TODO Reference PokemonGameViewModel
        binding.viewModel = viewModel

        val evilPokemonObserver = Observer<Pokemon> {
            if (it.stats[5].baseStat <= 0) {
                this.findNavController().navigate(R.id.action_gameFragment_to_winScreen)
            }
        }

        viewModel.evilPokemon.observe(this, evilPokemonObserver)

        viewModel.pokemon.observe(this, Observer {
            if (this.findNavController().currentDestination?.id == R.id.gameFragment && it.stats[5].baseStat <= 0) {
                this.findNavController().navigate(R.id.action_gameFragment_to_loseScreen)
            }
        })

        return binding.root
    }

}
