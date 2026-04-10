package com.voxcom.topplethetiki.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.databinding.ItemAvatarBinding

class AvatarAdapter : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    // 🔥 These should match what you assign in RoomActivity
    private val avatars = listOf("avatar_1", "avatar_2", "avatar_3", "avatar_4")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bind(avatars[position])
    }

    override fun getItemCount(): Int = avatars.size

    inner class AvatarViewHolder(
        private val binding: ItemAvatarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatar: String) {

            val avatarRes = when (avatar) {
                "avatar_1" -> R.drawable.avatar1
                "avatar_2" -> R.drawable.avatar2
                "avatar_3" -> R.drawable.avatar3
                "avatar_4" -> R.drawable.avatar1
                else -> R.drawable.avatar1
            }

            binding.imgAvatar.setImageResource(avatarRes)
        }
    }
}