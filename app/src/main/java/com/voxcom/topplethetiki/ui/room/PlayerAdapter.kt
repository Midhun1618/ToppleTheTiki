package com.voxcom.topplethetiki.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.databinding.ItemPlayerBinding

class PlayerAdapter(
    private val onClick: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<Player>()

    inner class PlayerViewHolder(val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {

        val player = players[position]

        holder.binding.tvUsername.text = player.username

        holder.itemView.setOnClickListener {
            onClick(player) // 🔥 important
        }
    }

    override fun getItemCount(): Int = players.size

    // 🔥 Update list
    fun updatePlayers(newPlayers: List<Player>) {
        players.clear()
        players.addAll(newPlayers)
        notifyDataSetChanged()
    }
}