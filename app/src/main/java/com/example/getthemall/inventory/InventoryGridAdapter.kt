package com.example.getthemall.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.getthemall.databinding.GridViewItemBinding
import com.example.getthemall.network.Pokemon

class InventoryGridAdapter(private val onClickListener: OnClickListener) : ListAdapter<Pokemon,
        InventoryGridAdapter.PokemonViewHolder>(DiffCallback) {
    override fun onBindViewHolder(holder: InventoryGridAdapter.PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(pokemon)
        }
        holder.bind(pokemon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryGridAdapter.PokemonViewHolder {
        return PokemonViewHolder(GridViewItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Pokemon>(){
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }

    class PokemonViewHolder(private var binding: GridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon){
            binding.pokemon = pokemon
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (pokemon:Pokemon) -> Unit){
        fun onClick(pokemon: Pokemon) = clickListener(pokemon)
    }
}