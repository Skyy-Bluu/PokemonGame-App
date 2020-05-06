package com.example.getthemall.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.getthemall.databinding.FragmentInventoryBinding


/**
 * A simple [Fragment] subclass.
 */
class InventoryFragment : Fragment() {

    private val viewModel: InventoryViewModel by lazy {
        ViewModelProviders.of(this).get(InventoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentInventoryBinding.inflate(inflater)
        binding.setLifecycleOwner(this)


        binding.viewModel = viewModel
        binding.inventoryGrid.adapter = InventoryGridAdapter(InventoryGridAdapter.OnClickListener{
            viewModel.choosePokemon(it)
        })

        viewModel.navigateWithSelectedPokemon.observe(this, Observer {
            if (it != null){
                this.findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryFragmentToGameFragment(it))

                viewModel.pokemonChosen()
            }
        })

        return binding.root
    }
}


