package com.example.getthemall.battle

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.getthemall.R
import com.example.getthemall.databinding.FragmentBattleBinding
import com.example.getthemall.network.Pokemon

/**
 * A simple [Fragment] subclass.
 */
class BattleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBattleBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        val pokemon = arguments?.let { BattleFragmentArgs.fromBundle(it).selectedPokemon }

        val viewModelFactory = pokemon?.let { BattleViewModelFactory(it) }

        val viewModel: BattleViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(BattleViewModel::class.java)

        binding.viewModel = viewModel
        binding.battleUtil = viewModel.BattleUtils()

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

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,view!!.findNavController())
                ||super.onOptionsItemSelected(item)

    }
}
