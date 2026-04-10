package com.voxcom.topplethetiki.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.databinding.ItemPlayerBinding

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<Player>()

    fun submitList(newList: List<Player>) {
        players.clear()
        players.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int = players.size

    class PlayerViewHolder(
        private val binding: ItemPlayerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {

            binding.tvUsername.text = player.username

            // Ready status
            if (player.isReady) {
                binding.tvReady.text = "Ready"
                binding.tvReady.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_green_dark)
                )
            } else {
                binding.tvReady.text = "Not Ready"
                binding.tvReady.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_red_dark)
                )
            }

            // Avatar (placeholder for now)
            binding.imgAvatar.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}