package com.voxcom.topplethetiki.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.data.model.Player
import com.voxcom.topplethetiki.databinding.ItemPlayerBinding

class PlayerAdapter(
    private val onClick: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<Player>()

    // 🔥 Track current turn
    private var currentTurnUid: String? = null

    inner class PlayerViewHolder(
        val binding: ItemPlayerBinding
    ) : RecyclerView.ViewHolder(binding.root)

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

        // ✅ Username
        holder.binding.tvUsername.text = player.username

        // ✅ Avatar Mapping
        val avatarRes = when (player.avatar) {
            "avatar_1" -> R.drawable.avatar1
            "avatar_2" -> R.drawable.avatar2
            "avatar_3" -> R.drawable.avatar3
            "avatar_4" -> R.drawable.avatar1
            else -> R.drawable.avatar1
        }

        holder.binding.ivAvatar.setImageResource(avatarRes)

        // 🔥 Highlight current player turn
        val isCurrentPlayer = player.uid == currentTurnUid

        holder.binding.root.setBackgroundResource(
            if (isCurrentPlayer) R.drawable.player_turn_highlight
            else android.R.color.transparent
        )

        // ✅ Click
        holder.itemView.setOnClickListener {
            onClick(player)
        }
    }

    override fun getItemCount(): Int = players.size

    // 🔥 Update player list
    fun updatePlayers(newPlayers: List<Player>) {
        players.clear()
        players.addAll(newPlayers)
        notifyDataSetChanged()
    }

    // 🔥 Update current turn
    fun setCurrentTurn(uid: String) {
        currentTurnUid = uid
        notifyDataSetChanged()
    }
}